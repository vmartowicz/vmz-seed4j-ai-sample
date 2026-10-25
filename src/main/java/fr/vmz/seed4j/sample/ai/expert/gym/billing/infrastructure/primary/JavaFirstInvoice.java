package fr.vmz.seed4j.sample.ai.expert.gym.billing.infrastructure.primary;

import java.math.BigDecimal;
import java.util.UUID;

public record JavaFirstInvoice(UUID id, BigDecimal amount) {}
