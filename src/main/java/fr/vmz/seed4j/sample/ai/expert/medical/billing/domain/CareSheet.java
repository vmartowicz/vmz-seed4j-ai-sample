package fr.vmz.seed4j.sample.ai.expert.medical.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public final class CareSheet {

  private final CareSheetId id;
  private final UUID consultation;
  private final PatientId patient;
  private final PatientId insured;
  private final DoctorId doctor;
  private final LocalDate date;
  private final BigDecimal baseFee;
  private final BigDecimal feeOverrun;
  private final BigDecimal reimbursement;
  private final boolean thirdPartyPayment;
  private @Nullable LocalDate sentOn;

  private CareSheet(ActToBill act, Insurance insurance, Tariff tariff) {
    id = CareSheetId.newId();
    consultation = act.consultation();
    patient = act.patient();
    insured = insurance.insured();
    doctor = act.doctor();
    date = act.date();
    baseFee = tariff.baseFee();
    feeOverrun = tariff.feeOverrun();
    reimbursement = baseFee.multiply(insurance.reimbursementRate()).setScale(2, RoundingMode.HALF_UP);
    thirdPartyPayment = insurance.thirdPartyPayment();
  }

  public static CareSheet issue(ActToBill act, Insurance insurance, Tariff tariff) {
    return new CareSheet(act, insurance, tariff);
  }

  public void sent(LocalDate today) {
    sentOn = today;
  }

  public boolean isPending() {
    return sentOn == null;
  }

  public BigDecimal paidByPatient() {
    return thirdPartyPayment ? BigDecimal.ZERO : baseFee.add(feeOverrun);
  }

  public CareSheetStatus status() {
    return isPending() ? CareSheetStatus.PENDING : CareSheetStatus.SENT;
  }

  public CareSheetId id() {
    return id;
  }

  public UUID consultation() {
    return consultation;
  }

  public PatientId patient() {
    return patient;
  }

  public PatientId insured() {
    return insured;
  }

  public DoctorId doctor() {
    return doctor;
  }

  public LocalDate date() {
    return date;
  }

  public BigDecimal baseFee() {
    return baseFee;
  }

  public BigDecimal feeOverrun() {
    return feeOverrun;
  }

  public BigDecimal reimbursement() {
    return reimbursement;
  }

  public boolean thirdPartyPayment() {
    return thirdPartyPayment;
  }

  public Optional<LocalDate> sentOn() {
    return Optional.ofNullable(sentOn);
  }
}
