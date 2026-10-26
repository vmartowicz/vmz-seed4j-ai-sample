package fr.vmz.seed4j.sample.ai.expert.bank.lending.domain;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public final class LoanTerms {

  private static final MathContext PRECISION = MathContext.DECIMAL64;
  private static final BigDecimal MONTHLY_RATE = new BigDecimal("0.05").divide(BigDecimal.valueOf(12), PRECISION);
  private static final BigDecimal MONTHLY_INSURANCE_RATE = new BigDecimal("0.003").divide(BigDecimal.valueOf(12), PRECISION);

  private LoanTerms() {}

  public static BigDecimal monthlyInstalment(BigDecimal amount, int months) {
    var discount = BigDecimal.ONE.subtract(BigDecimal.ONE.add(MONTHLY_RATE).pow(-months, PRECISION));
    var repayment = amount.multiply(MONTHLY_RATE).divide(discount, PRECISION);

    return repayment.add(amount.multiply(MONTHLY_INSURANCE_RATE)).setScale(2, RoundingMode.HALF_UP);
  }
}
