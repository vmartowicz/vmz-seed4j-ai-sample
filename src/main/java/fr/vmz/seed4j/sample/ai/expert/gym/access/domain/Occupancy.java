package fr.vmz.seed4j.sample.ai.expert.gym.access.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;

public record Occupancy(Gym gym, long inside, int capacity) {}
