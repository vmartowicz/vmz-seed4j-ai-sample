package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;

public record MemberPlan(boolean active, boolean unlimitedClasses, Gym homeGym) {}
