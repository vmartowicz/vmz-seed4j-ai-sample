package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.booking;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.Locale;
import java.util.regex.Pattern;

public record EmployeeEmail(String email) {
  private static final Pattern FORMAT = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

  public EmployeeEmail {
    Assert.notBlank("email", email);

    email = email.trim().toLowerCase(Locale.ROOT);
    if (!FORMAT.matcher(email).matches()) {
      throw new InvalidEmployeeEmailException(email);
    }
  }

  public String get() {
    return email();
  }
}
