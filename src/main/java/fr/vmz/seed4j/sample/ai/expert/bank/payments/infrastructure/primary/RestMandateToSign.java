package fr.vmz.seed4j.sample.ai.expert.bank.payments.infrastructure.primary;

import org.jspecify.annotations.Nullable;

record RestMandateToSign(@Nullable String creditor) {}
