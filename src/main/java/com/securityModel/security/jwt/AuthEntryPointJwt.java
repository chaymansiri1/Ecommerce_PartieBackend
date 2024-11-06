package com.securityModel.security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

//@Component elle sera automatiquement détectée et gérée par le conteneur Spring.
// Elle sera donc injectable là où elle est nécessaire
@Component
//AuthEntryPointJwt est une classe qui gère les réponses lorsque l'utilisateur non
// authentifié tente d'accéder à des ressources protégées
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

  //Le Logger est utilisé pour enregistrer les messages d'erreur. Ici, il permet de capturer et de consigner
  // toute tentative d'accès non autorisée, en affichant le message d'erreur via logger.error.
  private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
    logger.error("Unauthorized error: {}", authException.getMessage());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    final Map<String, Object> body = new HashMap<>();
    body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
    body.put("error", "Unauthorized");
    body.put("message", authException.getMessage());
    body.put("path", request.getServletPath());
//ObjectMapper : Cet objet est utilisé pour convertir le contenu de la map
// en JSON et l'envoyer dans la réponse HTTP.
    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), body);

//    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
  }

}
