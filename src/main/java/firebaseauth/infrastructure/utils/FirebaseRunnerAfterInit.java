package firebaseauth.infrastructure.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseRunnerAfterInit {

    @Bean
    public CommandLineRunner runner() {
        return args -> FirebaseUtils.init();
    }
}
