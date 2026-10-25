package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

public enum MealService {
  LUNCH(LocalTime.of(12, 0), LocalTime.of(14, 0)),
  DINNER(LocalTime.of(19, 0), LocalTime.of(22, 0));

  private static final int ARRIVAL_STEP_MINUTES = 30;

  private final LocalTime start;
  private final List<LocalTime> arrivalTimes;

  MealService(LocalTime start, LocalTime end) {
    this.start = start;
    arrivalTimes = Stream.iterate(start, time -> time.isBefore(end), time -> time.plusMinutes(ARRIVAL_STEP_MINUTES)).toList();
  }

  public static MealService of(LocalTime arrival) {
    return Stream.of(values())
      .filter(service -> service.arrivalTimes().contains(arrival))
      .findFirst()
      .orElseThrow(() -> new InvalidArrivalTimeException(arrival));
  }

  public LocalTime start() {
    return start;
  }

  public List<LocalTime> arrivalTimes() {
    return arrivalTimes;
  }
}
