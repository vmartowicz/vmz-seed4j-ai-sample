package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

public record Options(boolean childSeat, boolean additionalDriver, boolean gps) {
  public static final Options NONE = new Options(false, false, false);
}
