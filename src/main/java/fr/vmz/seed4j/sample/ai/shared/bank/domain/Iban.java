package fr.vmz.seed4j.sample.ai.shared.bank.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.regex.Pattern;

public record Iban(String iban) {
  private static final Pattern FRENCH_FORMAT = Pattern.compile("^FR\\d{2}[0-9A-Z]{23}$");
  private static final BigInteger MODULUS = BigInteger.valueOf(97);
  private static final String OUR_BANK_CODE = "17806";
  private static final SecureRandom RANDOM = new SecureRandom();

  public Iban {
    Assert.notBlank("iban", iban);

    iban = iban.replace(" ", "").toUpperCase(Locale.ROOT);
    if (!FRENCH_FORMAT.matcher(iban).matches() || !numeric(iban.substring(4) + iban.substring(0, 4)).mod(MODULUS).equals(BigInteger.ONE)) {
      throw new InvalidIbanException(iban);
    }
  }

  public static Iban generate() {
    var bban = OUR_BANK_CODE + "00001" + "%011d".formatted(RANDOM.nextLong(100_000_000_000L)) + "%02d".formatted(RANDOM.nextInt(100));
    var check = 98 - numeric(bban + "FR00").mod(MODULUS).intValue();

    return new Iban("FR%02d%s".formatted(check, bban));
  }

  public boolean isOurs() {
    return iban.startsWith(OUR_BANK_CODE, 4);
  }

  public String get() {
    return iban();
  }

  private static BigInteger numeric(String text) {
    var digits = new StringBuilder();
    text.chars().forEach(character -> digits.append(Character.getNumericValue(character)));

    return new BigInteger(digits.toString());
  }
}
