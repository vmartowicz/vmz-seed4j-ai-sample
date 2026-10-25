package fr.vmz.seed4j.sample.ai.expert.gym.booking.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.util.List;
import java.util.Optional;

public interface ClassBookingsRepository {
  void save(ClassBooking booking);

  Optional<ClassBooking> get(ClassBookingId booking);

  List<ClassBooking> of(MemberId member);

  List<ClassBooking> ofSession(SessionId session);

  default ClassBooking booking(ClassBookingId booking) {
    return get(booking).orElseThrow(() -> new UnknownClassBookingException(booking));
  }
}
