package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

import java.util.UUID;

public interface WorkshopRegistrations {
  boolean isRegistered(UUID workshop, String ticket);
}
