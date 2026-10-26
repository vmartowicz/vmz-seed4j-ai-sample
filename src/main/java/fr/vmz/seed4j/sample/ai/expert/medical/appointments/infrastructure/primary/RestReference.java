package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary;

import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestReference(@Nullable UUID doctorId, @Nullable UUID insuredId) {}
