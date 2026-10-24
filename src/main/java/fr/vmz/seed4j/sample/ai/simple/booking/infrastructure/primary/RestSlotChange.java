package fr.vmz.seed4j.sample.ai.simple.booking.infrastructure.primary;

import java.time.LocalDateTime;
import org.jspecify.annotations.Nullable;

record RestSlotChange(@Nullable LocalDateTime start) {}
