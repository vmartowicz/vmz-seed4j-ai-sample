package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import java.time.LocalDateTime;

public record Slot(LocalDateTime start, LocalDateTime end, boolean emergency) {}
