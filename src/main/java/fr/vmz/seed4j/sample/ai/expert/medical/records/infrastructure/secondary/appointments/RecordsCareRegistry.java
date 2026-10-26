package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.secondary.appointments;

import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.CareRegistry;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary.JavaDoctors;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary.JavaPatients;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import org.springframework.stereotype.Component;

@Component
class RecordsCareRegistry implements CareRegistry {

  private final JavaPatients patients;
  private final JavaDoctors doctors;

  RecordsCareRegistry(JavaPatients patients, JavaDoctors doctors) {
    this.patients = patients;
    this.doctors = doctors;
  }

  @Override
  public boolean patientExists(PatientId patient) {
    return patients.exists(patient.get());
  }

  @Override
  public boolean doctorExists(DoctorId doctor) {
    return doctors.exists(doctor.get());
  }
}
