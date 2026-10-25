package fr.vmz.seed4j.sample.ai.expert.conference.program.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.program.application.ProgramApplicationService;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conference/program")
class ProgramResource {

  private final ProgramApplicationService program;

  ProgramResource(ProgramApplicationService program) {
    this.program = program;
  }

  @PostMapping("/sessions")
  @ResponseStatus(HttpStatus.CREATED)
  RestProgramSession place(@RequestBody RestSessionToPlace session) {
    Assert.notNull("talkId", session.talkId());
    Assert.notNull("room", session.room());

    return RestProgramSession.from(program.place(session.talkId(), session.room(), session.toSlot()));
  }

  @GetMapping("/days/{day}")
  List<RestProgramSession> day(@PathVariable LocalDate day) {
    return program.day(day).stream().map(RestProgramSession::from).toList();
  }
}
