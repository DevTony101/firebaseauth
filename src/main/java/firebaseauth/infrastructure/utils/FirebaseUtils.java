package firebaseauth.infrastructure.utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import javax.servlet.http.HttpServletRequest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FirebaseUtils {

  private static final String AUTH_HEADER = "Authorization";
  private static final String ENV_FIREBASE_CONFIG_PATH = "FIREBASE_CONFIG_PATH";
  private static final String ENV_FIREBASE_DATABASE_URL = "FIREBASE_DATABASE_URL";

  private static String GOOGLE_APPLICATION_CREDENTIALS;
  static {
    try {
      GOOGLE_APPLICATION_CREDENTIALS = ResourceUtils.getFile(System.getenv(ENV_FIREBASE_CONFIG_PATH)).getPath();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static final String DATABASE_URL = System.getenv(ENV_FIREBASE_DATABASE_URL);

  public static void init() {
    try {
      FileInputStream serviceAccount = new FileInputStream(GOOGLE_APPLICATION_CREDENTIALS);
      FirebaseOptions options = FirebaseOptions.builder()
              .setCredentials(GoogleCredentials.fromStream(serviceAccount))
              .setDatabaseUrl(DATABASE_URL)
              .build();

      FirebaseApp.initializeApp(options);
      log.info("Firebase initialized correctly");
    } catch (IOException e) {
      log.error("Error while trying to initialize firebase", e);
    }
  }

  public static String getBearerToken(HttpServletRequest request) {
    String bearerToken = null;
    String authorization = request.getHeader(AUTH_HEADER);
    if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
      bearerToken = authorization.substring(7);
    }
    return bearerToken;
  }
}
