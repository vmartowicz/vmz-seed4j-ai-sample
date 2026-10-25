package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.time.LocalDateTime;

public record BookableSession(SessionId id, Gym gym, LocalDateTime start, int capacity, boolean cancelled) {}
