package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import static fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.AppointmentsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class AppointmentDeskTest {

  private final PatientsRepository patients = mock(PatientsRepository.class, CALLS_REAL_METHODS);
  private final DoctorsRepository doctors = mock(DoctorsRepository.class, CALLS_REAL_METHODS);
  private final AppointmentsRepository appointments = mock(AppointmentsRepository.class, CALLS_REAL_METHODS);
  private final AppointmentDesk desk = new AppointmentDesk(patients, doctors, appointments);

  private final List<Appointment> booked = new ArrayList<>();
  private Patient patient;
  private Doctor doctor;

  @BeforeEach
  void loadAgenda() {
    patient = known(patient());
    doctor = known(generalPractitioner());
    doReturn(booked).when(appointments).ofDoctor(doctor.id());
    doReturn(booked).when(appointments).ofPatient(patient.id());
  }

  @Nested
  class Booking {

    @Test
    void shouldBookFreeSlotOnline() {
      var appointment = desk.book(request(MONDAY.atTime(10, 0), Channel.ONLINE, null), NOW);

      assertThat(appointment.patient()).isEqualTo(patient.id());
      assertThat(appointment.doctor()).isEqualTo(doctor.id());
      assertThat(appointment.start()).isEqualTo(MONDAY.atTime(10, 0));
      assertThat(appointment.end()).isEqualTo(MONDAY.atTime(10, 15));
      assertThat(appointment.status()).isEqualTo(AppointmentStatus.BOOKED);
      verify(appointments).save(appointment);
    }

    @Test
    void shouldNotBookOutsideSlots() {
      assertThatThrownBy(() -> desk.book(request(MONDAY.atTime(10, 5), Channel.ONLINE, null), NOW)).isExactlyInstanceOf(NotASlotException.class);
    }

    @Test
    void shouldNotBookPastSlots() {
      assertThatThrownBy(() -> desk.book(request(MONDAY.atTime(9, 0), Channel.ONLINE, null), MONDAY.atTime(9, 0))).isExactlyInstanceOf(
        NotASlotException.class
      );
    }

    @Test
    void shouldNotBookTakenSlot() {
      booked.add(desk.book(request(MONDAY.atTime(10, 0), Channel.ONLINE, null), NOW));

      assertThatThrownBy(() -> desk.book(request(MONDAY.atTime(10, 0), Channel.PHONE, null), NOW)).isExactlyInstanceOf(SlotTakenException.class);
      assertThat(desk.book(request(MONDAY.atTime(10, 15), Channel.PHONE, null), NOW).status()).isEqualTo(AppointmentStatus.BOOKED);
    }

    @Test
    void shouldReuseCancelledSlot() {
      var cancelled = desk.book(request(MONDAY.atTime(10, 0), Channel.ONLINE, null), NOW);
      cancelled.cancel(NOW.minusDays(1));
      booked.add(cancelled);

      assertThat(desk.book(request(MONDAY.atTime(10, 0), Channel.PHONE, null), NOW).status()).isEqualTo(AppointmentStatus.BOOKED);
    }

    @Test
    void shouldBookEmergencySlotsByPhoneTheSameDayOnly() {
      var emergency = MONDAY.atTime(17, 30);

      assertThat(desk.book(request(emergency, Channel.PHONE, null), NOW).start()).isEqualTo(emergency);
      assertThatThrownBy(() -> desk.book(request(emergency.plusMinutes(15), Channel.ONLINE, null), NOW)).isExactlyInstanceOf(
        EmergencySlotException.class
      );
      assertThatThrownBy(() -> desk.book(request(emergency.plusDays(1), Channel.PHONE, null), NOW)).isExactlyInstanceOf(EmergencySlotException.class);
    }

    @Test
    void shouldBlockOnlineBookingAfter3NoShowsIn12Months() {
      booked.addAll(List.of(noShow(NOW.minusMonths(11)), noShow(NOW.minusMonths(5)), noShow(NOW.minusDays(3)), noShow(NOW.minusMonths(13))));

      assertThatThrownBy(() -> desk.book(request(MONDAY.atTime(10, 0), Channel.ONLINE, null), NOW)).isExactlyInstanceOf(
        OnlineBookingBlockedException.class
      );
      assertThat(desk.book(request(MONDAY.atTime(10, 0), Channel.PHONE, null), NOW).channel()).isEqualTo(Channel.PHONE);
    }

    @Test
    void shouldAllowOnlineBookingWith2NoShows() {
      var past = NOW.minusMonths(2);
      var honored = new AppointmentToBook(patient.id(), doctor.id(), past, Channel.PHONE, null).book(new Slot(past, past.plusMinutes(15), false));
      honored.honor();
      booked.addAll(List.of(noShow(NOW.minusMonths(5)), noShow(NOW.minusMonths(13)), noShow(NOW.minusDays(3)), honored));

      assertThat(desk.book(request(MONDAY.atTime(10, 0), Channel.ONLINE, null), NOW).channel()).isEqualTo(Channel.ONLINE);
    }

    @Test
    void shouldNeedCompanionForChildrenUnder16() {
      var child = known(child(MONDAY.minusYears(10)).register());
      doReturn(List.of()).when(appointments).ofPatient(child.id());

      assertThatThrownBy(() -> desk.book(new AppointmentToBook(child.id(), doctor.id(), MONDAY.atTime(10, 0), Channel.ONLINE, " "), NOW)).isExactlyInstanceOf(
        CompanionRequiredException.class
      );
      assertThat(desk.book(new AppointmentToBook(child.id(), doctor.id(), MONDAY.atTime(10, 0), Channel.ONLINE, "Mum"), NOW).companion()).contains("Mum");
    }

    @Test
    void shouldNotBookForUnknownPatientOrDoctor() {
      var unknownPatient = PatientId.newId();
      doReturn(Optional.empty()).when(patients).get(unknownPatient);
      var unknownDoctor = DoctorId.newId();
      doReturn(Optional.empty()).when(doctors).get(unknownDoctor);

      assertThatThrownBy(() -> desk.book(new AppointmentToBook(unknownPatient, doctor.id(), MONDAY.atTime(10, 0), Channel.ONLINE, null), NOW))
        .isExactlyInstanceOf(UnknownPatientException.class);
      assertThatThrownBy(() -> desk.book(new AppointmentToBook(patient.id(), unknownDoctor, MONDAY.atTime(10, 0), Channel.ONLINE, null), NOW))
        .isExactlyInstanceOf(UnknownDoctorException.class);
    }
  }

  @Nested
  class FollowUp {

    @Test
    void shouldCancelUntil24HoursBefore() {
      var appointment = knownAppointment(desk.book(request(MONDAY.plusDays(1).atTime(10, 0), Channel.ONLINE, null), NOW));

      desk.cancel(appointment.id(), MONDAY.atTime(10, 0));

      assertThat(appointment.status()).isEqualTo(AppointmentStatus.CANCELLED);
    }

    @Test
    void shouldNotCancelLessThan24HoursBefore() {
      var appointment = knownAppointment(desk.book(request(MONDAY.plusDays(1).atTime(10, 0), Channel.ONLINE, null), NOW));

      assertThatThrownBy(() -> desk.cancel(appointment.id(), MONDAY.atTime(10, 1))).isExactlyInstanceOf(CancellationTooLateException.class);
    }

    @Test
    void shouldRecordHonoredAndMissedAppointments() {
      var honored = knownAppointment(desk.book(request(MONDAY.atTime(10, 0), Channel.ONLINE, null), NOW));
      var missed = knownAppointment(desk.book(request(MONDAY.atTime(10, 15), Channel.ONLINE, null), NOW));

      desk.honor(honored.id());
      desk.markNoShow(missed.id(), MONDAY.atTime(10, 15));

      assertThat(honored.status()).isEqualTo(AppointmentStatus.HONORED);
      assertThat(missed.status()).isEqualTo(AppointmentStatus.NO_SHOW);
      assertThatThrownBy(() -> desk.honor(missed.id())).isExactlyInstanceOf(InvalidAppointmentTransitionException.class);
    }

    @Test
    void shouldNotMarkNoShowBeforeAppointment() {
      var appointment = knownAppointment(desk.book(request(MONDAY.atTime(10, 0), Channel.ONLINE, null), NOW));

      assertThatThrownBy(() -> desk.markNoShow(appointment.id(), MONDAY.atTime(9, 59))).isExactlyInstanceOf(InvalidAppointmentTransitionException.class);
    }

    @Test
    void shouldListFreeSlotsBookableOnline() {
      booked.add(desk.book(request(MONDAY.atTime(9, 15), Channel.ONLINE, null), NOW));

      var free = desk.freeSlots(doctor.id(), MONDAY, MONDAY.atTime(9, 0));

      assertThat(free).hasSize(24).noneMatch(slot -> slot.start().equals(MONDAY.atTime(9, 15))).noneMatch(Slot::emergency);
    }

    @Test
    void shouldKeepAppointmentsWhenReferringDoctorChanges() {
      var other = known(specialist());

      desk.changeReferringDoctor(patient.id(), other.id());

      assertThat(patient.referringDoctor()).contains(other.id());
      verify(patients).save(patient);
      verify(appointments, never()).save(any());
    }

    @Test
    void shouldAttachChildToAnotherInsured() {
      var parent = known(patient());

      desk.attach(patient.id(), parent.id());

      assertThat(patient.insured()).isEqualTo(parent.id());
    }
  }

  @Test
  void shouldRegisterDoctorsAndPatients() {
    var registered = desk.registerPatient(adult());
    var practitioner = desk.registerDoctor(new DoctorToRegister("Dr Who", DoctorKind.SPECIALIST, Centre.VAULX_EN_VELIN, null, true));

    assertThat(practitioner.feeOverrun()).isEqualByComparingTo("0");
    assertThat(practitioner.onCall()).isTrue();
    verify(patients).save(registered);
    verify(doctors).save(practitioner);
    assertThat(desk.doctor(doctor.id())).isSameAs(doctor);
  }

  @Test
  void shouldFindOnCallDoctorOfCentre() {
    var onCall = new DoctorToRegister("Dr Night", DoctorKind.GENERAL_PRACTITIONER, Centre.LYON_7, null, true).register();
    var elsewhere = new DoctorToRegister("Dr Away", DoctorKind.GENERAL_PRACTITIONER, Centre.VENISSIEUX, null, true).register();
    doReturn(List.of(doctor, elsewhere, onCall)).when(doctors).all();

    assertThat(desk.onCallDoctorOf(Centre.LYON_7)).contains(onCall);
    assertThat(desk.onCallDoctorOf(Centre.VAULX_EN_VELIN)).isEmpty();
  }

  private AppointmentToBook request(LocalDateTime start, Channel channel, String companion) {
    return new AppointmentToBook(patient.id(), doctor.id(), start, channel, companion);
  }

  private Appointment noShow(LocalDateTime at) {
    var appointment = new AppointmentToBook(patient.id(), doctor.id(), at, Channel.PHONE, null).book(new Slot(at, at.plusMinutes(15), false));
    appointment.markNoShow(at.plusYears(1));

    return appointment;
  }

  private Appointment knownAppointment(Appointment appointment) {
    doReturn(Optional.of(appointment)).when(appointments).get(appointment.id());

    return appointment;
  }

  private Patient known(Patient known) {
    doReturn(Optional.of(known)).when(patients).get(known.id());

    return known;
  }

  private Doctor known(Doctor known) {
    doReturn(Optional.of(known)).when(doctors).get(known.id());

    return known;
  }
}
