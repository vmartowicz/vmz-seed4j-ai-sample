package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain.RiskLevel;
import org.jspecify.annotations.Nullable;

record RestReview(@Nullable RiskLevel risk) {}
