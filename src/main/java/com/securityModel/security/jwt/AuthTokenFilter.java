package com.securityModel.security.jwt;

import java.io.IOException;

import com.securityModel.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

//classe est un filtre d'authentification qui vérifie si une requête HTTP contient un token JWT valide
//Si le token est valide, il authentifie l'utilisateur et enregistre cette authentification dans le contexte de sécurité de Spring
//sécuriser les API en s'assurant que seules les requêtes provenant d'utilisateurs authentifiés peuvent accéder aux ressources protégées.
public class AuthTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      //La méthode parseJwt(request) est appelée pour extraire le token à partir de l'en-tête "Authorization"
      String jwt = parseJwt(request);
      //Si un token est trouvé et qu'il est valide (grâce à la méthode jwtUtils.validateJwtToken(jwt)),
      // le filtre extrait le nom d'utilisateur du token (jwtUtils.getUserNameFromJwtToken(jwt)).
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        //Un objet UsernamePasswordAuthenticationToken est créé pour
        // authentifier l'utilisateur dans le contexte de sécurité Spring.
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      logger.error("Cannot set user authentication: {}", e.getMessage());
    }

    filterChain.doFilter(request, response);
  }

  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7, headerAuth.length());
    }

    return null;
  }
}
