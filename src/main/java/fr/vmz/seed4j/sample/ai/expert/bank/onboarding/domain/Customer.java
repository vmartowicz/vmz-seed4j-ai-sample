package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public final class Customer {

  private static final int ADULT_AGE = 18;
  private static final int REVIEW_YEARS = 3;
  private static final int HIGH_RISK_REVIEW_YEARS = 1;

  private final CustomerId id;
  private final String fullName;
  private final LocalDate birthDate;
  private final BigDecimal monthlyIncome;
  private final boolean politicallyExposed;
  private final @Nullable String legalGuardian;
  private final LocalDate customerSince;
  private RiskLevel risk;
  private LocalDate lastReview;

  Customer(ProspectToOnboard prospect, LocalDate today) {
    id = CustomerId.newId();
    fullName = prospect.fullName().trim();
    birthDate = prospect.birthDate();
    monthlyIncome = prospect.monthlyIncome();
    politicallyExposed = prospect.politicallyExposed();
    legalGuardian = prospect.legalGuardian();
    customerSince = today;
    lastReview = today;
    risk = riskOf(prospect.risk());
  }

  public void review(RiskLevel assessed, LocalDate today) {
    risk = riskOf(assessed);
    lastReview = today;
  }

  public LocalDate nextReviewOn() {
    return lastReview.plusYears(risk == RiskLevel.HIGH ? HIGH_RISK_REVIEW_YEARS : REVIEW_YEARS);
  }

  public boolean isReviewOverdueOn(LocalDate date) {
    return date.isAfter(nextReviewOn());
  }

  public boolean isMinorOn(LocalDate date) {
    return Period.between(birthDate, date).getYears() < ADULT_AGE;
  }

  private RiskLevel riskOf(RiskLevel assessed) {
    return politicallyExposed ? RiskLevel.HIGH : assessed;
  }

  public CustomerId id() {
    return id;
  }

  public String fullName() {
    return fullName;
  }

  public LocalDate birthDate() {
    return birthDate;
  }

  public BigDecimal monthlyIncome() {
    return monthlyIncome;
  }

  public Optional<String> legalGuardian() {
    return Optional.ofNullable(legalGuardian);
  }

  public LocalDate customerSince() {
    return customerSince;
  }

  public RiskLevel risk() {
    return risk;
  }
}
