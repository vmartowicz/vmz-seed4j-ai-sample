package fr.vmz.seed4j.sample.ai.expert.gym.membership.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

public record MembershipToSubscribe(
  String fullName,
  LocalDate birthDate,
  boolean parentalConsent,
  boolean studentCard,
  Plan plan,
  boolean committed,
  Gym homeGym
) {
  private static final int MINIMUM_AGE = 16;
  private static final int ADULT_AGE = 18;
  private static final BigDecimal STUDENT_RATE = new BigDecimal("0.70");
  private static final BigDecimal NO_COMMITMENT_SURCHARGE = BigDecimal.TEN;

  public MembershipToSubscribe {
    Assert.field("fullName", fullName).notBlank().maxLength(100);
    Assert.notNull("birthDate", birthDate);
    Assert.notNull("plan", plan);
    Assert.notNull("homeGym", homeGym);
  }

  public Membership subscribe(LocalDate today) {
    var age = Period.between(birthDate, today).getYears();
    if (age < MINIMUM_AGE) {
      throw new MemberTooYoungException(age);
    }

    if (age < ADULT_AGE && !parentalConsent) {
      throw new ParentalConsentRequiredException(age);
    }

    return new Membership(this, today);
  }

  public BigDecimal monthlyFee() {
    var fee = plan.price();
    if (studentCard && plan == Plan.ESSENTIAL) {
      fee = fee.multiply(STUDENT_RATE);
    }

    if (!committed) {
      fee = fee.add(NO_COMMITMENT_SURCHARGE);
    }

    return fee.setScale(2, RoundingMode.HALF_UP);
  }
}
