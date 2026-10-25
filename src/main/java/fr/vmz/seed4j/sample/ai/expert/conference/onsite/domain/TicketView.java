package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;

public record TicketView(String number, String holderName, Email holderEmail, BadgeType type, boolean valid) {}
