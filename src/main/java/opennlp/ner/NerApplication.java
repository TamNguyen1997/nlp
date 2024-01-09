package opennlp.ner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NerApplication {
  public static void main(String[] args) {
    SpringApplicationBuilder builder = new SpringApplicationBuilder(NerApplication.class);
    builder.headless(false);
    builder.run(args);
  }
}
