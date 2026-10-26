package fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.primary;

import java.math.BigDecimal;
import org.jspecify.annotations.Nullable;

record RestAmount(@Nullable BigDecimal amount, @Nullable String country) {}
