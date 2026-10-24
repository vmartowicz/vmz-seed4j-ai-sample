package fr.vmz.seed4j.sample.ai.simple.booking.application;

import static fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.BookingsFixture.*;
import static fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.CatalogFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.BookingsRepository;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.SmsSender;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.ServicesCatalog;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

@UnitTest
class BookingsApplicationServiceTest {

  // 2026-03-09 at 23:30 UTC is already 2026-03-10 in Paris
  private static final Clock LATE_EVENING_IN_UTC = Clock.fixed(Instant.parse("2026-03-09T23:30:00Z"), ZoneId.of("Europe/Paris"));

  private final ServicesCatalog catalog = mock(ServicesCatalog.class, CALLS_REAL_METHODS);
  private final BookingsRepository bookings = mock(BookingsRepository.class);
  private final SmsSender sms = mock(SmsSender.class);
  private final BookingsApplicationService service = new BookingsApplicationService(catalog, bookings, sms, LATE_EVENING_IN_UTC);

  @Test
  void shouldRemindBookingsOfTomorrowInParis() {
    doReturn(Optional.of(MASSAGE)).when(catalog).get(MASSAGE.id());
    when(bookings.confirmedOn(TOMORROW)).thenReturn(List.of(massageTomorrowAt10()));

    service.sendReminders();

    verify(sms).send(any());
  }
}
