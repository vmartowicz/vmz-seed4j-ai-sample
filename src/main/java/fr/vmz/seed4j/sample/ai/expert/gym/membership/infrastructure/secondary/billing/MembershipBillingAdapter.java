package fr.vmz.seed4j.sample.ai.expert.gym.membership.infrastructure.secondary.billing;

import fr.vmz.seed4j.sample.ai.expert.gym.billing.infrastructure.primary.JavaBilling;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.FirstInvoice;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.Membership;
import fr.vmz.seed4j.sample.ai.expert.gym.membership.domain.MembershipBilling;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class MembershipBillingAdapter implements MembershipBilling {

  private final JavaBilling billing;

  // Lazy: billing calls memberships back, the contexts depend on each other
  MembershipBillingAdapter(@Lazy JavaBilling billing) {
    this.billing = billing;
  }

  @Override
  public FirstInvoice openAccount(Membership membership) {
    var invoice = billing.openAccount(membership.id().get(), membership.monthlyFee(), membership.signUpFee());

    return new FirstInvoice(invoice.id(), invoice.amount());
  }
}
