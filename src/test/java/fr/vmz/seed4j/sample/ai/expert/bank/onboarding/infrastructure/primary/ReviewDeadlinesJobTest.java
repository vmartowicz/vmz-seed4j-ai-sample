package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.infrastructure.primary;

import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.application.OnboardingApplicationService;
import org.junit.jupiter.api.Test;

@UnitTest
class ReviewDeadlinesJobTest {

  @Test
  void shouldFreezeOverdueReviews() {
    var onboarding = mock(OnboardingApplicationService.class);

    new ReviewDeadlinesJob(onboarding).freeze();

    verify(onboarding).freezeOverdueReviews();
  }
}
