package fr.vmz.seed4j.sample.ai.shared.conference.domain;

public enum Room {
  AMPHI(500),
  SAONE(200),
  RHONE(200),
  FOURVIERE(80),
  CROIX_ROUSSE(40);

  private final int capacity;

  Room(int capacity) {
    this.capacity = capacity;
  }

  public int capacity() {
    return capacity;
  }
}
