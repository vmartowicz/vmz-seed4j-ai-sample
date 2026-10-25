package fr.vmz.seed4j.sample.ai.shared.conference.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.Locale;
import java.util.regex.Pattern;

public record Email(String email) {
  private static final Pattern FORMAT = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

  public Email {
    Assert.field("email", email).notBlank().maxLength(255);

    email = email.trim().toLowerCase(Locale.ROOT);
    if (!FORMAT.matcher(email).matches()) {
      throw new InvalidEmailException(email);
    }
  }

  public String get() {
    return email();
  }
}
