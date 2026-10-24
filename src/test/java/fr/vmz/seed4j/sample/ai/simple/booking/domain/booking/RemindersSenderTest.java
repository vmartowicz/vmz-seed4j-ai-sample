package fr.vmz.seed4j.sample.ai.simple.booking.domain.booking;

import static fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.BookingsFixture.*;
import static fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.CatalogFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.ServicesCatalog;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

@UnitTest
class RemindersSenderTest {

  private final ServicesCatalog catalog = mock(ServicesCatalog.class, CALLS_REAL_METHODS);
  private final BookingsRepository bookings = mock(BookingsRepository.class);
  private final SmsSender sms = mock(SmsSender.class);
  private final RemindersSender reminders = new RemindersSender(catalog, bookings, sms);

  @Test
  void shouldSendReminderForBookingsOfTheDay() {
    doReturn(Optional.of(MASSAGE)).when(catalog).get(MASSAGE.id());
    var booking = massageTomorrowAt10();
    when(bookings.confirmedOn(TOMORROW)).thenReturn(List.of(booking));

    reminders.sendRemindersFor(TOMORROW);

    verify(sms).send(new Sms(new MobilePhone("0612345678"), "Rappel : votre rendez-vous Massage demain à 10:00"));
    assertThat(booking.reminderSent()).isTrue();
    verify(bookings).save(booking);
  }

  @Test
  void shouldNotSendReminderTwice() {
    var booking = massageTomorrowAt10();
    booking.markReminderSent();
    when(bookings.confirmedOn(TOMORROW)).thenReturn(List.of(booking));

    reminders.sendRemindersFor(TOMORROW);

    verify(sms, never()).send(any());
  }
}
