package fr.vmz.seed4j.sample.ai.advanced.library.domain.member;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.Locale;
import java.util.regex.Pattern;

public record MemberEmail(String email) {
  private static final Pattern FORMAT = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

  public MemberEmail {
    Assert.notBlank("email", email);

    email = email.trim().toLowerCase(Locale.ROOT);
    if (!FORMAT.matcher(email).matches()) {
      throw new InvalidMemberEmailException(email);
    }
  }

  public String get() {
    return email();
  }
}
