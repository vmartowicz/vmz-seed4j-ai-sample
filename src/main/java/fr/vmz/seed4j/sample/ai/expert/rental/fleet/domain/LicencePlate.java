package fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.Locale;
import java.util.regex.Pattern;

public record LicencePlate(String plate) {
  private static final Pattern FORMAT = Pattern.compile("^[A-Z]{2}-\\d{3}-[A-Z]{2}$");

  public LicencePlate {
    Assert.notBlank("plate", plate);

    plate = plate.trim().toUpperCase(Locale.ROOT);
    if (!FORMAT.matcher(plate).matches()) {
      throw new InvalidLicencePlateException(plate);
    }
  }

  public String get() {
    return plate();
  }
}
