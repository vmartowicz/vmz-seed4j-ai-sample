package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public final class Diner {

  private static final int LOYALTY_HONORED_BOOKINGS = 5;
  private static final int BLACKLIST_NO_SHOWS = 3;
  private static final int MAX_YEARLY_CANCELLATIONS = 3;

  private final DinerEmail email;
  private final List<LocalDate> cancellations = new ArrayList<>();
  private int honoredBookings;
  private int noShows;

  private Diner(DinerEmail email) {
    Assert.notNull("email", email);

    this.email = email;
  }

  public static Diner newcomer(DinerEmail email) {
    return new Diner(email);
  }

  public void assertCanBook(LocalDate today) {
    if (status() == DinerStatus.BLACKLISTED) {
      throw new BlacklistedDinerException(email);
    }

    var year = Year.from(today);
    if (cancellationsIn(year) >= MAX_YEARLY_CANCELLATIONS) {
      throw new CancellationLimitReachedException(email, year);
    }
  }

  public void honor() {
    honoredBookings++;
  }

  public void noShow() {
    noShows++;
  }

  public void cancel(LocalDate on) {
    cancellations.add(on);
  }

  public DinerStatus status() {
    if (noShows >= BLACKLIST_NO_SHOWS) {
      return DinerStatus.BLACKLISTED;
    }

    if (honoredBookings >= LOYALTY_HONORED_BOOKINGS) {
      return DinerStatus.LOYAL;
    }

    return DinerStatus.REGULAR;
  }

  public boolean isLoyal() {
    return status() == DinerStatus.LOYAL;
  }

  public long cancellationsIn(Year year) {
    return cancellations
      .stream()
      .filter(date -> Year.from(date).equals(year))
      .count();
  }

  public DinerEmail email() {
    return email;
  }

  public int honoredBookings() {
    return honoredBookings;
  }

  public int noShows() {
    return noShows;
  }
}
