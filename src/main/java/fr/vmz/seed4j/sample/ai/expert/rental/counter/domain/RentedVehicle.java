package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;

public record RentedVehicle(String plate, Category category, int mileage, boolean electric) {}
