package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.Locale;
import java.util.regex.Pattern;

public record DinerEmail(String email) {
  private static final Pattern FORMAT = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

  public DinerEmail {
    Assert.notBlank("email", email);

    email = email.trim().toLowerCase(Locale.ROOT);
    if (!FORMAT.matcher(email).matches()) {
      throw new InvalidDinerEmailException(email);
    }
  }

  public String get() {
    return email();
  }
}
