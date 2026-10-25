package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;

public record RoomOccupancy(Room room, long inside, int capacity) {}
