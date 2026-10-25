package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain;

import java.util.UUID;

public interface Workshops {
  boolean exists(UUID workshop);
}
