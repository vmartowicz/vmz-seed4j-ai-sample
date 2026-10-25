package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import java.util.Collection;

public final class CoachAgenda {

  private static final int MAX_WEEKLY_MINUTES = 25 * 60;

  private CoachAgenda() {}

  public static void assertCanTeach(Coach coach, Discipline discipline, TeachingSlot candidate, Collection<TeachingSlot> taught) {
    if (!coach.isCertifiedFor(discipline)) {
      throw new CoachNotCertifiedException(coach, discipline);
    }

    var weeklyMinutes = candidate.minutes() + taught.stream().mapToLong(TeachingSlot::minutes).sum();
    if (weeklyMinutes > MAX_WEEKLY_MINUTES) {
      throw new CoachOverbookedException(coach);
    }

    if (taught.stream().anyMatch(candidate::isTooCloseTo)) {
      throw new CoachNotAvailableException(coach, candidate);
    }
  }
}
