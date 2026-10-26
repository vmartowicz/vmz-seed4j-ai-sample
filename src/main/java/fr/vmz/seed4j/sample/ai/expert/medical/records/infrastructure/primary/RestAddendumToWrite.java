package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.primary;

import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestAddendumToWrite(@Nullable UUID doctorId, @Nullable String text) {}
