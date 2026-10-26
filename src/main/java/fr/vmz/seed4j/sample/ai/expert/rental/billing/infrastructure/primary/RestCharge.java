package fr.vmz.seed4j.sample.ai.expert.rental.billing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.Charge;
import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.ChargeKind;
import java.math.BigDecimal;

record RestCharge(ChargeKind kind, BigDecimal amount) {
  static RestCharge from(Charge charge) {
    return new RestCharge(charge.kind(), charge.amount());
  }
}
