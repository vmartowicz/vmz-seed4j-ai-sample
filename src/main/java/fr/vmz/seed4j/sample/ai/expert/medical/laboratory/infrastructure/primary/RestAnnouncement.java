package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.infrastructure.primary;

import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestAnnouncement(@Nullable UUID doctorId) {}
