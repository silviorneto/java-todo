package com.silvioromano.todo.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.silvioromano.todo.user.IUserRepository;
import com.silvioromano.todo.user.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;


@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       var servletPath = request.getServletPath();
       if (servletPath.startsWith("/tasks")) {
           var authorization = request.getHeader("Authorization");
           if (authorization == null || authorization.isEmpty()) {
               response.sendError(HttpStatus.UNAUTHORIZED.value());
               return;
           }

           var authEncoded = authorization.substring("Basic".length()).trim();
           byte[] authDecode = Base64.getDecoder().decode(authEncoded);
           var authString = new String(authDecode);

           String[] credentials = authString.split(":");
           String username = credentials[0];
           String password = credentials[1];

           Optional<UserModel> userFound = userRepository.findByUsername(username);
           if (userFound.isPresent()) {
               var passwordCheck = BCrypt.verifyer().verify(password.toCharArray(), userFound.get().getPassword());
               if (passwordCheck.verified) {
                   request.setAttribute("userID", userFound.get().getUuid());
                   filterChain.doFilter(request, response);
                   return;
               }
           }
           response.sendError(HttpStatus.UNAUTHORIZED.value());
       } else {
           filterChain.doFilter(request, response);
       }
    }
}
