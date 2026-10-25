package fr.vmz.seed4j.sample.ai.expert.gym.booking.infrastructure.secondary.booking;

import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.ClassBooking;
import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.ClassBookingId;
import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.ClassBookingsRepository;
import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.SessionId;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryClassBookingsRepository implements ClassBookingsRepository {

  private final Map<ClassBookingId, ClassBooking> bookings = new ConcurrentHashMap<>();

  @Override
  public void save(ClassBooking booking) {
    bookings.put(booking.id(), booking);
  }

  @Override
  public Optional<ClassBooking> get(ClassBookingId booking) {
    return Optional.ofNullable(bookings.get(booking));
  }

  @Override
  public List<ClassBooking> of(MemberId member) {
    return bookings
      .values()
      .stream()
      .filter(booking -> booking.member().equals(member))
      .toList();
  }

  @Override
  public List<ClassBooking> ofSession(SessionId session) {
    return bookings
      .values()
      .stream()
      .filter(booking -> booking.session().id().equals(session))
      .toList();
  }
}
