package com.securityModel.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.securityModel.security.services.UserDetailsImpl;

import io.jsonwebtoken.*;

//L'annotation @Component permet à cette classe d'être détectée et gérée par le conteneur Spring.
// Cela signifie qu'elle pourra être injectée dans d'autres composants.
@Component
//pour créer, manipuler et valider des tokens JWT
//Elle utilise des configurations comme une clé secrète et une durée d'expiration
// définies dans le fichier de configuration de l'application.
public class JwtUtils {

  //Le logger est utilisé pour enregistrer les erreurs lors de la validation des tokens JWT
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);


//@Value("${bezkoder.app.jwtSecret}") : La clé secrète (jwtSecret) utilisée pour signer et vérifier l'intégrité des tokens
// JWT est injectée depuis le fichier de configuration de l'application (application.properties ou application.yml).
//@Value("${bezkoder.app.jwtExpirationMs}") : La durée d'expiration du token JWT, exprimée en millisecondes, est également injectée à partir des propriétés de l'application.
  @Value("${bezkoder.app.jwtSecret}")
  private String jwtSecret;

  @Value("${bezkoder.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  public String generateJwtToken(UserDetailsImpl userPrincipal) {
    return generateTokenFromUsername(userPrincipal.getUsername());
  }

  public String generateTokenFromUsername(String username) {
    return Jwts.builder().setSubject(username).setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }

}
