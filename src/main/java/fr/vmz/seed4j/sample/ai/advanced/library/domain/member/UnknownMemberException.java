package fr.vmz.seed4j.sample.ai.advanced.library.domain.member;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.UnknownLibraryElementException;

public class UnknownMemberException extends UnknownLibraryElementException {

  UnknownMemberException(MemberId member) {
    super("Unknown member " + member.get());
  }
}
