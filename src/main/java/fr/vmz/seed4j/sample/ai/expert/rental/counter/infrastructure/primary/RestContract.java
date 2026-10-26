package fr.vmz.seed4j.sample.ai.expert.rental.counter.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.Contract;
import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.ContractStatus;
import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.ReturnInspection;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestContract(
  UUID id,
  UUID reservationId,
  String customer,
  Category bookedCategory,
  String plate,
  Category vehicleCategory,
  RentalPoint pickUpPoint,
  LocalDateTime pickedUpAt,
  LocalDateTime plannedReturnAt,
  int mileageOut,
  BigDecimal deposit,
  ContractStatus status,
  @Nullable LocalDateTime returnedAt,
  @Nullable String damage,
  @Nullable UUID invoiceId
) {
  static RestContract from(Contract contract) {
    return new RestContract(
      contract.id().get(),
      contract.reservation(),
      contract.customer().get(),
      contract.bookedCategory(),
      contract.plate(),
      contract.vehicleCategory(),
      contract.pickUpPoint(),
      contract.pickedUpAt(),
      contract.plannedReturnAt(),
      contract.mileageOut(),
      contract.deposit(),
      contract.status(),
      contract.returnedAt().orElse(null),
      contract.returnInspection().flatMap(ReturnInspection::damage).orElse(null),
      contract.invoice().orElse(null)
    );
  }
}
