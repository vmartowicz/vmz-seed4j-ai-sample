package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.regex.Pattern;

public record Telephone(String telephone) {
  private static final Pattern FORMAT = Pattern.compile("^\\d{10}$");

  public Telephone {
    Assert.notBlank("telephone", telephone);

    if (!FORMAT.matcher(telephone).matches()) {
      throw new InvalidTelephoneException(telephone);
    }
  }

  public String get() {
    return telephone();
  }
}
