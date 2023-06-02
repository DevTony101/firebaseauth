package firebaseauth.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseUser implements Serializable {

  private String uid;
  private String name;
  private String email;
  private String picture;
  private String issuer;
  private boolean isEmailVerified;
  private String role;
}
