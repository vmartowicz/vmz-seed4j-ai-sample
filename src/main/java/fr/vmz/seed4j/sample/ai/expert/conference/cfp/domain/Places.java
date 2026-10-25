package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record Places(int talks, int quickies, int workshops) {
  public Places {
    Assert.field("talks", talks).min(0);
    Assert.field("quickies", quickies).min(0);
    Assert.field("workshops", workshops).min(0);
  }

  int of(Format format) {
    return switch (format) {
      case TALK -> talks;
      case QUICKIE -> quickies;
      case WORKSHOP -> workshops;
    };
  }
}
