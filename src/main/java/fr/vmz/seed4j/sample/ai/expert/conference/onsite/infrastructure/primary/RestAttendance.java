package fr.vmz.seed4j.sample.ai.expert.conference.onsite.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain.Attendance;
import java.util.UUID;

record RestAttendance(UUID talkId, String title, long attendees) {
  static RestAttendance from(Attendance attendance) {
    return new RestAttendance(attendance.talk(), attendance.title(), attendance.attendees());
  }
}
