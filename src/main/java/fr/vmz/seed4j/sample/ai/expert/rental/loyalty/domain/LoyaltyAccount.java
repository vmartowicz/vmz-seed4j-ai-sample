package fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class LoyaltyAccount {

  private static final int FREE_DAY_POINTS = 500;
  private static final int VALIDITY_MONTHS = 24;
  private static final int STATUS_MONTHS = 12;
  private static final int SILVER_POINTS = 1_000;
  private static final int GOLD_POINTS = 3_000;

  private final Email customer;
  private final List<Earning> earnings = new ArrayList<>();
  private final Map<UUID, Integer> redemptions = new HashMap<>();

  private LoyaltyAccount(Email customer) {
    Assert.notNull("customer", customer);

    this.customer = customer;
  }

  public static LoyaltyAccount open(Email customer) {
    return new LoyaltyAccount(customer);
  }

  public void credit(int points, LocalDate on, String reference) {
    Assert.field("points", points).min(0);
    Assert.notBlank("reference", reference);

    earnings.add(new Earning(points, on));
  }

  public void redeemFreeDay(UUID reservation, LocalDate today) {
    var balance = balanceOn(today);
    if (balance < FREE_DAY_POINTS) {
      throw new NotEnoughPointsException(balance);
    }

    redemptions.put(reservation, FREE_DAY_POINTS);
  }

  public void cancelRedemption(UUID reservation) {
    redemptions.remove(reservation);
  }

  public int balanceOn(LocalDate date) {
    var valid = earnings
      .stream()
      .filter(earning -> date.isBefore(earning.on().plusMonths(VALIDITY_MONTHS)))
      .mapToInt(Earning::points)
      .sum();
    var redeemed = redemptions.values().stream().mapToInt(Integer::intValue).sum();

    return Math.max(0, valid - redeemed);
  }

  public int earnedInLast12MonthsOn(LocalDate date) {
    var since = date.minusMonths(STATUS_MONTHS);

    return earnings
      .stream()
      .filter(earning -> earning.on().isAfter(since))
      .mapToInt(Earning::points)
      .sum();
  }

  public LoyaltyStatus statusOn(LocalDate date) {
    var earned = earnedInLast12MonthsOn(date);
    if (earned >= GOLD_POINTS) {
      return LoyaltyStatus.GOLD;
    }

    return earned >= SILVER_POINTS ? LoyaltyStatus.SILVER : LoyaltyStatus.MEMBER;
  }

  public Email customer() {
    return customer;
  }

  private record Earning(int points, LocalDate on) {}
}
