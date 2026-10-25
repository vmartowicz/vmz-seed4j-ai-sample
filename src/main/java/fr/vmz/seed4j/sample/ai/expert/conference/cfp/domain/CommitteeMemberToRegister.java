package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;

public record CommitteeMemberToRegister(String name, String company) {
  public CommitteeMemberToRegister {
    Assert.field("name", name).notBlank().maxLength(100);
    Assert.field("company", company).notBlank().maxLength(100);
  }

  public CommitteeMember register() {
    return new CommitteeMember(CommitteeMemberId.newId(), name.trim(), company.trim());
  }
}
