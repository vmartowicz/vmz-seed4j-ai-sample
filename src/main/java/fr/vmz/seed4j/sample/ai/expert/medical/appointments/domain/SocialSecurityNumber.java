package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.regex.Pattern;

public record SocialSecurityNumber(String number) {
  private static final Pattern FORMAT = Pattern.compile("^\\d{15}$");
  private static final long MODULUS = 97;

  public SocialSecurityNumber {
    Assert.notBlank("socialSecurityNumber", number);

    number = number.replace(" ", "");
    if (!FORMAT.matcher(number).matches() || MODULUS - Long.parseLong(number.substring(0, 13)) % MODULUS != Long.parseLong(number.substring(13))) {
      throw new InvalidSocialSecurityNumberException(number);
    }
  }

  public String get() {
    return number();
  }
}
