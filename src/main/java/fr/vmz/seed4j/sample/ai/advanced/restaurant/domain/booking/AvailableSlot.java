package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.booking;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.table.DiningTable;
import java.time.LocalTime;
import java.util.List;

public record AvailableSlot(LocalTime arrival, List<DiningTable> tables) {}
