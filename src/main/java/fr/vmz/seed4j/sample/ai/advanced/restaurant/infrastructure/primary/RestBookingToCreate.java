package fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.BookingToCreate;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking.Slot;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.Contact;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.DinerEmail;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.PhoneNumber;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.PartySize;
import java.time.LocalDate;
import java.time.LocalTime;
import org.jspecify.annotations.Nullable;

record RestBookingToCreate(
  @Nullable String fullName,
  @Nullable String phone,
  @Nullable String email,
  @Nullable LocalDate date,
  @Nullable LocalTime time,
  int people,
  @Nullable String comments
) {
  BookingToCreate toDomain() {
    return new BookingToCreate(
      new Contact(fullName, new DinerEmail(email), new PhoneNumber(phone)),
      new Slot(date, time),
      new PartySize(people),
      comments
    );
  }
}
