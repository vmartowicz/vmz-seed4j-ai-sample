package fr.vmz.seed4j.sample.ai.expert.medical.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import java.util.List;

public class CareSheetsDesk {

  private final CareSheetsRepository sheets;
  private final Insureds insureds;
  private final Practitioners practitioners;
  private final HealthInsurer insurer;

  public CareSheetsDesk(CareSheetsRepository sheets, Insureds insureds, Practitioners practitioners, HealthInsurer insurer) {
    this.sheets = sheets;
    this.insureds = insureds;
    this.practitioners = practitioners;
    this.insurer = insurer;
  }

  public CareSheet bill(ActToBill act) {
    var insurance = insureds.insuranceOf(act.patient()).orElseThrow(() -> new UnknownBillingPartyException("patient " + act.patient().get()));
    var tariff = practitioners.tariffOf(act.doctor()).orElseThrow(() -> new UnknownBillingPartyException("doctor " + act.doctor().get()));

    var sheet = CareSheet.issue(act, insurance, tariff);
    sheets.save(sheet);

    return sheet;
  }

  public void sendPending(LocalDate today) {
    sheets
      .all()
      .stream()
      .filter(CareSheet::isPending)
      .forEach(sheet -> {
        insurer.send(sheet);
        sheet.sent(today);
        sheets.save(sheet);
      });
  }

  public List<CareSheet> careSheetsOf(PatientId patient) {
    return sheets
      .all()
      .stream()
      .filter(sheet -> sheet.patient().equals(patient))
      .toList();
  }

  public CareSheet careSheet(CareSheetId sheet) {
    return sheets.careSheetOf(sheet);
  }
}
