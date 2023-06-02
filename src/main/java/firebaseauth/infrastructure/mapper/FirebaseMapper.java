package firebaseauth.infrastructure.mapper;

import com.google.firebase.auth.FirebaseToken;
import firebaseauth.infrastructure.dto.FirebaseUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FirebaseMapper {

  FirebaseMapper INSTANCE = Mappers.getMapper(FirebaseMapper.class);

  @Mapping(target = "role", expression = "java((String) token.getClaims().get(\"role\"))")
  FirebaseUser toFirebaseUser(FirebaseToken token);
}
