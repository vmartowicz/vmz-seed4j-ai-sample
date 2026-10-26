package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

import java.math.BigDecimal;

public record Charge(ChargeKind kind, BigDecimal amount) {}
