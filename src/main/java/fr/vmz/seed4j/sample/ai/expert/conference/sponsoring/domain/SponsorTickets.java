package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import java.util.List;

public interface SponsorTickets {
  List<String> issue(String contactName, Email contactEmail, int count);
}
