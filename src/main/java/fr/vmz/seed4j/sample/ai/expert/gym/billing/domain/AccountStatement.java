package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

import java.util.List;

public record AccountStatement(BillingAccount account, List<Invoice> invoices) {}
