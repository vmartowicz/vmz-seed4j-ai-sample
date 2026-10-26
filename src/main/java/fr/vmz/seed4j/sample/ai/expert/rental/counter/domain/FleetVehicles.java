package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.time.LocalDate;
import java.util.List;

public interface FleetVehicles {
  List<String> rentable(RentalPoint point, Category category, LocalDate date);

  RentedVehicle pickUp(String plate);

  void giveBack(String plate, RentalPoint point, int mileage, boolean damaged, LocalDate date);
}
