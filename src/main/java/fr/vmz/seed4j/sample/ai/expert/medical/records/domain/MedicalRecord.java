package fr.vmz.seed4j.sample.ai.expert.medical.records.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class MedicalRecord {

  private final PatientId patient;
  private final List<String> allergies = new ArrayList<>();
  private final List<String> history = new ArrayList<>();
  private final List<String> treatments = new ArrayList<>();
  private boolean pacemaker;

  private MedicalRecord(PatientId patient) {
    Assert.notNull("patient", patient);

    this.patient = patient;
  }

  public static MedicalRecord of(PatientId patient) {
    return new MedicalRecord(patient);
  }

  public void addAllergy(String allergy) {
    Assert.notBlank("allergy", allergy);

    allergies.add(normalized(allergy));
  }

  public void addHistory(String event) {
    Assert.notBlank("history", event);

    history.add(event);
  }

  public void addTreatment(String treatment) {
    Assert.notBlank("treatment", treatment);

    treatments.add(treatment);
  }

  public void declarePacemaker() {
    pacemaker = true;
  }

  public boolean isAllergicTo(String medicine) {
    return allergies.contains(normalized(medicine));
  }

  private static String normalized(String value) {
    return value.strip().toLowerCase(Locale.ROOT);
  }

  public PatientId patient() {
    return patient;
  }

  public List<String> allergies() {
    return List.copyOf(allergies);
  }

  public List<String> history() {
    return List.copyOf(history);
  }

  public List<String> treatments() {
    return List.copyOf(treatments);
  }

  public boolean hasPacemaker() {
    return pacemaker;
  }
}
