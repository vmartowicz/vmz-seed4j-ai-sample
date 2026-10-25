package fr.vmz.seed4j.sample.ai.advanced.library.domain.member;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.InvalidLibraryValueException;

class InvalidMemberEmailException extends InvalidLibraryValueException {

  InvalidMemberEmailException(String email) {
    super("Invalid email: " + email);
  }
}
