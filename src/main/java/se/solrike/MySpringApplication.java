package se.solrike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import se.solrike.configuration.JerseyConfig;
import se.solrike.resources.ComponentMarker;

@SpringBootApplication(scanBasePackageClasses = {ComponentMarker.class, JerseyConfig.class})
@PropertySource("classpath:myapi-app.properties")
public class MySpringApplication {

  public static void main(String[] args) {
    SpringApplication.run(MySpringApplication.class, args);
  }
}
