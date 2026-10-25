package fr.vmz.seed4j.sample.ai.expert.gym.booking.infrastructure.secondary.notification;

import fr.vmz.seed4j.sample.ai.expert.gym.booking.domain.MemberNotifications;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class LoggingMemberNotifications implements MemberNotifications {

  private static final Logger log = LoggerFactory.getLogger(LoggingMemberNotifications.class);

  @Override
  public void notify(MemberId member, String message) {
    log.info("Notification to member {}: {}", member.get(), message);
  }
}
