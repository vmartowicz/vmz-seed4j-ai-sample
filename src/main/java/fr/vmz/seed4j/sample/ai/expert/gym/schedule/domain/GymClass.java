package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public final class GymClass {

  private final GymClassId id;
  private final Discipline discipline;
  private final CoachId coach;
  private final Studio studio;
  private final DayOfWeek day;
  private final LocalTime start;
  private final int minutes;
  private final int capacity;

  GymClass(GymClassId id, ClassToSchedule gymClass) {
    this.id = id;
    discipline = gymClass.discipline();
    coach = gymClass.coach();
    studio = gymClass.studio();
    day = gymClass.day();
    start = gymClass.start();
    minutes = gymClass.minutes();
    capacity = gymClass.capacity();
  }

  public TeachingSlot slotIn(LocalDate monday) {
    var at = monday.plusDays(day.ordinal()).atTime(start);

    return new TeachingSlot(studio.gym(), at, at.plusMinutes(minutes));
  }

  public ClassSession sessionIn(LocalDate monday) {
    return new ClassSession(this, slotIn(monday));
  }

  public GymClassId id() {
    return id;
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

  public DayOfWeek day() {
    return day;
  }

  public LocalTime start() {
    return start;
  }

  public int minutes() {
    return minutes;
  }

  public int capacity() {
    return capacity;
  }
}
