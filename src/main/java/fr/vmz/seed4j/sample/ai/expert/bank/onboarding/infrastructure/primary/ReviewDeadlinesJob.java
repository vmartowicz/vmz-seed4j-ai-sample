package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.application.OnboardingApplicationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
class ReviewDeadlinesJob {

  private final OnboardingApplicationService onboarding;

  ReviewDeadlinesJob(OnboardingApplicationService onboarding) {
    this.onboarding = onboarding;
  }

  @Scheduled(cron = "0 0 1 * * *", zone = "Europe/Paris")
  void freeze() {
    onboarding.freezeOverdueReviews();
  }
}
