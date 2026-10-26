package fr.vmz.seed4j.sample.ai.expert.medical.billing.application;

import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.ActToBill;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.CareSheet;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.CareSheetId;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.CareSheetsDesk;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.CareSheetsRepository;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.HealthInsurer;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.Insureds;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.Practitioners;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MedicalBillingApplicationService {

  private final CareSheetsDesk desk;
  private final Clock clock;

  public MedicalBillingApplicationService(
    CareSheetsRepository sheets,
    Insureds insureds,
    Practitioners practitioners,
    HealthInsurer insurer,
    Clock clock
  ) {
    desk = new CareSheetsDesk(sheets, insureds, practitioners, insurer);
    this.clock = clock;
  }

  public CareSheet bill(ActToBill act) {
    return desk.bill(act);
  }

  public void sendPendingCareSheets() {
    desk.sendPending(LocalDate.now(clock));
  }

  public List<CareSheet> careSheetsOf(PatientId patient) {
    return desk.careSheetsOf(patient);
  }

  public CareSheet careSheet(CareSheetId sheet) {
    return desk.careSheet(sheet);
  }
}
