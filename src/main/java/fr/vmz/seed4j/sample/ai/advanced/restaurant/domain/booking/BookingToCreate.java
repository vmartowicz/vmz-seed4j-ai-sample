package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.Contact;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.DiningTable;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.PartySize;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public record BookingToCreate(Contact contact, Slot slot, PartySize party, @Nullable String comments) {
  public BookingToCreate {
    Assert.notNull("contact", contact);
    Assert.notNull("slot", slot);
    Assert.notNull("party", party);
    Assert.field("comments", comments).maxLength(500);

    comments = comments == null || comments.isBlank() ? null : comments.trim();
  }

  TableBooking create(List<DiningTable> tables, boolean loyal, LocalDateTime now) {
    Optional<Deposit> deposit = party.requiresDeposit() && !loyal ? Optional.of(Deposit.expected(party, now, slot.at())) : Optional.empty();

    return new TableBooking(TableBookingId.newId(), this, tables, deposit);
  }
}
