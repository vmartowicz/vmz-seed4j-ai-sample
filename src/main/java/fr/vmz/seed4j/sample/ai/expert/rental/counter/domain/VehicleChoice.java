package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;

public final class VehicleChoice {

  private VehicleChoice() {}

  public static List<Category> categoriesFor(Category booked, boolean gold, boolean youngDriver) {
    var categories = new LinkedHashSet<Category>();
    if (gold) {
      booked.upgrades().stream().findFirst().ifPresent(categories::add);
    }
    categories.add(booked);
    categories.addAll(booked.upgrades());

    return categories
      .stream()
      .filter(category -> category == booked || !(youngDriver && category.isForbiddenToYoungDrivers()))
      .toList();
  }

  public static BigDecimal depositOf(Category category) {
    return switch (category) {
      case ECO, COMFORT -> new BigDecimal("800");
      case SUV, VAN -> new BigDecimal("1500");
      case PREMIUM -> new BigDecimal("2500");
    };
  }
}
