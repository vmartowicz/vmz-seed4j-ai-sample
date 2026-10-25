package fr.vmz.seed4j.sample.ai.expert.conference.onsite.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.RoomOccupancy;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;

record RestRoomOccupancy(Room room, long inside, int capacity) {
  static RestRoomOccupancy from(RoomOccupancy occupancy) {
    return new RestRoomOccupancy(occupancy.room(), occupancy.inside(), occupancy.capacity());
  }
}
