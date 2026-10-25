package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

import java.util.UUID;

public record Attendance(UUID talk, String title, long attendees) {}
