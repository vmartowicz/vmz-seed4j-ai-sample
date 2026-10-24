package fr.vmz.seed4j.sample.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import fr.vmz.seed4j.sample.ai.shared.generation.domain.ExcludeFromGeneratedCodeCoverage;

@SpringBootApplication
@ExcludeFromGeneratedCodeCoverage(reason = "Not testing logs")
public class VmzSeed4JAiSampleApplicationApp {

  private static final Logger log = LoggerFactory.getLogger(VmzSeed4JAiSampleApplicationApp.class);

  public static void main(String[] args) {
    Environment env = SpringApplication.run(VmzSeed4JAiSampleApplicationApp.class, args).getEnvironment();

    if (log.isInfoEnabled()) {
      log.info(ApplicationStartupTraces.of(env));
    }
  }
}
