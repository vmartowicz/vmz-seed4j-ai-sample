package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import java.time.LocalDateTime;

public final class ClassSession {

  private final SessionId id;
  private final GymClassId gymClass;
  private final Discipline discipline;
  private final Studio studio;
  private final TeachingSlot slot;
  private final int capacity;
  private CoachId coach;
  private boolean cancelled;

  ClassSession(GymClass gymClass, TeachingSlot slot) {
    id = SessionId.newId();
    this.gymClass = gymClass.id();
    discipline = gymClass.discipline();
    studio = gymClass.studio();
    capacity = gymClass.capacity();
    coach = gymClass.coach();
    this.slot = slot;
  }

  public void replaceCoach(CoachId replacement) {
    assertScheduled();

    coach = replacement;
  }

  public void cancel() {
    assertScheduled();

    cancelled = true;
  }

  private void assertScheduled() {
    if (cancelled) {
      throw new SessionCancelledException(id);
    }
  }

  public SessionId id() {
    return id;
  }

  public GymClassId gymClass() {
    return gymClass;
  }

  public Discipline discipline() {
    return discipline;
  }

  public CoachId coach() {
    return coach;
  }

  public Studio studio() {
    return studio;
  }

  public TeachingSlot slot() {
    return slot;
  }

  public LocalDateTime start() {
    return slot.start();
  }

  public LocalDateTime end() {
    return slot.end();
  }

  public int capacity() {
    return capacity;
  }

  public boolean isCancelled() {
    return cancelled;
  }
}
