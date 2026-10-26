package fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.CardType;
import org.jspecify.annotations.Nullable;

record RestCardRequest(@Nullable CardType type) {}
