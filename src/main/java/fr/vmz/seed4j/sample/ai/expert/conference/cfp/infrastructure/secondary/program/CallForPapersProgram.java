package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.secondary.program;

import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.ProgramSchedule;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.ProposalId;
import fr.vmz.seed4j.sample.ai.expert.conference.program.infrastructure.primary.JavaProgram;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class CallForPapersProgram implements ProgramSchedule {

  private final JavaProgram program;

  // Lazy: the program reads confirmed talks, the contexts depend on each other
  CallForPapersProgram(@Lazy JavaProgram program) {
    this.program = program;
  }

  @Override
  public void withdrawn(ProposalId proposal) {
    program.talkWithdrawn(proposal.get());
  }
}
