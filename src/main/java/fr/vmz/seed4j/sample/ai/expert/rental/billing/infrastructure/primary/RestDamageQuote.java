package fr.vmz.seed4j.sample.ai.expert.rental.billing.infrastructure.primary;

import java.math.BigDecimal;
import org.jspecify.annotations.Nullable;

record RestDamageQuote(@Nullable BigDecimal amount) {}
