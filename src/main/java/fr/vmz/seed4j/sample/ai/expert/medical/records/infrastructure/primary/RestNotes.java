package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.primary;

import org.jspecify.annotations.Nullable;

record RestNotes(@Nullable String examination, @Nullable String diagnosis) {}
