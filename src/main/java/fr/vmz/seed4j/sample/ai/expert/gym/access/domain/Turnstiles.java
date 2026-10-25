package fr.vmz.seed4j.sample.ai.expert.gym.access.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.LocalDateTime;
import java.util.Optional;

public class Turnstiles {

  private final BadgesRepository badges;
  private final PresencesRepository presences;
  private final AccessRightsProvider rights;

  public Turnstiles(BadgesRepository badges, PresencesRepository presences, AccessRightsProvider rights) {
    this.badges = badges;
    this.presences = presences;
    this.rights = rights;
  }

  public Badge issueBadge(MemberId member) {
    var badge = new Badge(BadgeNumber.generate(), member);

    badges.save(badge);

    return badge;
  }

  public Entry enter(BadgeNumber number, Gym gym, LocalDateTime now) {
    var member = badges.badge(number).member();

    var refusal = refusal(member, gym, now);
    if (refusal.isPresent()) {
      return Entry.refused(refusal.orElseThrow());
    }

    presences.save(new Presence(member, gym, now));
    return Entry.welcome();
  }

  public Presence exit(BadgeNumber number, LocalDateTime now) {
    var member = badges.badge(number).member();
    var presence = presences.openOf(member).orElseThrow(() -> new NotInsideException(number));

    presence.exit(now);
    presences.save(presence);

    return presence;
  }

  public Occupancy occupancy(Gym gym, LocalDateTime now) {
    var inside = presences
      .openIn(gym)
      .stream()
      .filter(presence -> !presence.isExpiredAt(now))
      .count();

    return new Occupancy(gym, inside, GymCapacity.of(gym));
  }

  private Optional<RefusalReason> refusal(MemberId member, Gym gym, LocalDateTime now) {
    var memberRights = rights.rightsOf(member);
    if (memberRights.status() == MemberStatus.SUSPENDED) {
      return Optional.of(RefusalReason.SUSPENDED);
    }

    if (memberRights.status() == MemberStatus.INACTIVE) {
      return Optional.of(RefusalReason.MEMBERSHIP_INACTIVE);
    }

    if (memberRights.inArrears()) {
      return Optional.of(RefusalReason.IN_ARREARS);
    }

    if (!memberRights.gyms().contains(gym)) {
      return Optional.of(RefusalReason.GYM_NOT_INCLUDED);
    }

    var current = presences.openOf(member);
    if (current.isPresent()) {
      var presence = current.orElseThrow();
      if (!presence.isExpiredAt(now)) {
        return Optional.of(RefusalReason.ALREADY_INSIDE);
      }

      presence.exit(now);
      presences.save(presence);
    }

    var occupancy = occupancy(gym, now);
    if (occupancy.inside() >= occupancy.capacity()) {
      return Optional.of(RefusalReason.GYM_FULL);
    }

    return Optional.empty();
  }
}
