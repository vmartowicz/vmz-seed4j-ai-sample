package fr.vmz.seed4j.sample.ai.intermediate.parking.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.List;
import java.util.stream.IntStream;

public record SpaceNumber(int number) {
  private static final int VISITOR_SPACES = 10;

  public SpaceNumber {
    Assert.field("number", number).min(1).max(VISITOR_SPACES);
  }

  public static List<SpaceNumber> all() {
    return IntStream.rangeClosed(1, VISITOR_SPACES).mapToObj(SpaceNumber::new).toList();
  }

  public int get() {
    return number();
  }
}
