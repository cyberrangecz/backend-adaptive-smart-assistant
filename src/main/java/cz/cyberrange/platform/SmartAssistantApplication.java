package cz.cyberrange.platform;

import cz.cyberrange.platform.config.ValidationMessagesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(value = {ValidationMessagesConfig.class})
@SpringBootApplication
public class SmartAssistantApplication {

  public static void main(String[] args) {
    SpringApplication.run(SmartAssistantApplication.class, args);
  }
}
