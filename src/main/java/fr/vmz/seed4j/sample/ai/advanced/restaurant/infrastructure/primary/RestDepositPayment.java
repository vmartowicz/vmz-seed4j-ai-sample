package fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.primary;

import java.math.BigDecimal;
import org.jspecify.annotations.Nullable;

record RestDepositPayment(@Nullable BigDecimal amount) {}
