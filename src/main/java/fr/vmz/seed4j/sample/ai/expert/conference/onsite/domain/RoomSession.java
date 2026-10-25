package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import java.util.List;
import java.util.UUID;

public record RoomSession(UUID talk, String title, boolean workshop, List<Email> speakers) {}
