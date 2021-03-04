package cz.muni.ics.kypo;

import cz.muni.ics.kypo.config.ValidationMessagesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(value = {
        ValidationMessagesConfig.class
})
@SpringBootApplication
public class KypoSmartAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(KypoSmartAssistantApplication.class, args);
    }

}
