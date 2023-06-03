package firebaseauth.annotation;

import firebaseauth.infrastructure.utils.FirebaseRunnerAfterInit;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({FirebaseRunnerAfterInit.class})
public @interface EnableFirebaseAuth {
}
