package fr.vmz.seed4j.sample.ai.simple.meetingroom.domain.room;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record RoomName(String name) {
  public RoomName {
    Assert.notBlank("name", name);

    name = name.trim();
    Assert.field("name", name).maxLength(50);
  }

  public boolean sameAs(RoomName other) {
    return name.equalsIgnoreCase(other.name());
  }

  public String get() {
    return name();
  }
}
