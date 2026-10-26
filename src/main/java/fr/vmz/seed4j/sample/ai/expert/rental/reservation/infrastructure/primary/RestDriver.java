package fr.vmz.seed4j.sample.ai.expert.rental.reservation.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.Driver;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;

record RestDriver(@Nullable String name, @Nullable String email, @Nullable LocalDate birthDate, @Nullable LocalDate licenceDate) {
  Driver toDomain() {
    return new Driver(name, new Email(email), birthDate, licenceDate);
  }
}
