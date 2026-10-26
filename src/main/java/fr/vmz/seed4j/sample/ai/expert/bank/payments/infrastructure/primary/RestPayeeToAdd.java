package fr.vmz.seed4j.sample.ai.expert.bank.payments.infrastructure.primary;

import org.jspecify.annotations.Nullable;

record RestPayeeToAdd(@Nullable String name, @Nullable String iban) {}
