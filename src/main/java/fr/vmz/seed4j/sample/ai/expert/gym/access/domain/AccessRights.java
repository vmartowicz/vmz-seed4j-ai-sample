package fr.vmz.seed4j.sample.ai.expert.gym.access.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.util.Set;

public record AccessRights(MemberStatus status, boolean inArrears, Set<Gym> gyms) {}
