package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain;

import java.math.BigDecimal;

public enum Pack {
  GOLD(new BigDecimal("8000"), 5, 3),
  SILVER(new BigDecimal("4000"), 2, 8),
  BRONZE(new BigDecimal("1500"), 1, Integer.MAX_VALUE);

  private final BigDecimal price;
  private final int tickets;
  private final int maxSponsors;

  Pack(BigDecimal price, int tickets, int maxSponsors) {
    this.price = price;
    this.tickets = tickets;
    this.maxSponsors = maxSponsors;
  }

  public boolean isAvailableWith(long sponsors) {
    return sponsors < maxSponsors;
  }

  public BigDecimal price() {
    return price;
  }

  public int tickets() {
    return tickets;
  }
}
