package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record BookedRental(
  UUID reservation,
  Email customer,
  boolean youngDriver,
  Category category,
  RentalPoint pickUpPoint,
  LocalDateTime pickUpAt,
  LocalDateTime returnAt,
  int days,
  BigDecimal dailyRate,
  BigDecimal paid,
  boolean confirmed
) {}
