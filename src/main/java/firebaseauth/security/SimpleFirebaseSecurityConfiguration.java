package firebaseauth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

public interface SimpleFirebaseSecurityConfiguration {

  @Bean
  default AuthenticationEntryPoint restAuthenticationEntryPoint() {
    return new AuthenticationEntryPoint() {
      @Autowired
      private ObjectMapper objectMapper;

      @Override
      public void commence(
          HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
          AuthenticationException e
      ) throws IOException {
        Map<String, Object> errorObject = new HashMap<>();
        int errorCode = 401;
        errorObject.put("message", "Unauthorized access of protected resource, invalid credentials");
        errorObject.put("error", HttpStatus.UNAUTHORIZED);
        errorObject.put("code", errorCode);
        errorObject.put("timestamp", new Timestamp(new Date().getTime()));
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setStatus(errorCode);
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(errorObject));
      }
    };
  }

  @Bean
  default CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Collections.singletonList("*"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
