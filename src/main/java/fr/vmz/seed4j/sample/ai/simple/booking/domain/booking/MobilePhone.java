package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.regex.Pattern;

public record MobilePhone(String phone) {
  private static final Pattern FORMAT = Pattern.compile("^0[67]\\d{8}$");

  public MobilePhone {
    Assert.notBlank("phone", phone);

    if (!FORMAT.matcher(phone).matches()) {
      throw new InvalidMobilePhoneException(phone);
    }
  }

  public String get() {
    return phone();
  }
}
