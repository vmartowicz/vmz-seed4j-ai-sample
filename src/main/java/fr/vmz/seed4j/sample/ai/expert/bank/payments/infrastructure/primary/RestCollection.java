package fr.vmz.seed4j.sample.ai.expert.bank.payments.infrastructure.primary;

import java.math.BigDecimal;
import org.jspecify.annotations.Nullable;

record RestCollection(@Nullable BigDecimal amount) {}
