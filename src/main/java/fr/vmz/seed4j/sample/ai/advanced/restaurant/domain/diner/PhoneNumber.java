package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.regex.Pattern;

public record PhoneNumber(String number) {
  private static final Pattern SEPARATORS = Pattern.compile("[\\s.-]");
  private static final Pattern FRENCH_FORMAT = Pattern.compile("^0[1-9]\\d{8}$");

  public PhoneNumber {
    Assert.notBlank("phone", number);

    var digits = SEPARATORS.matcher(number).replaceAll("");
    if (!FRENCH_FORMAT.matcher(digits).matches()) {
      throw new InvalidPhoneNumberException(number);
    }

    number = String.join(
      " ",
      digits.substring(0, 2),
      digits.substring(2, 4),
      digits.substring(4, 6),
      digits.substring(6, 8),
      digits.substring(8)
    );
  }

  public String get() {
    return number();
  }
}
