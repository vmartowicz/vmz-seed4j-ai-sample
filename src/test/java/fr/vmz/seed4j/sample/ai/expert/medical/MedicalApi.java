package fr.vmz.seed4j.sample.ai.expert.medical;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public final class MedicalApi {

  public static final LocalDate MONDAY = LocalDate.of(2026, 6, 1);
  public static final LocalDateTime NOW = MONDAY.atTime(8, 0);
  public static final String SSN = "190057512345622";

  private final MockMvc rest;

  public MedicalApi(MockMvc rest) {
    this.rest = rest;
  }

  public String doctor(String kind, String centre, String overrun, boolean onCall) throws Exception {
    var doctor = postJson(
      "/api/medical/doctors",
      "{\"name\":\"Dr House\",\"kind\":\"%s\",\"centre\":\"%s\",\"feeOverrun\":%s,\"onCall\":%s}".formatted(kind, centre, overrun, onCall)
    );

    return read(doctor.andExpect(status().isCreated()), "$.id");
  }

  public String generalPractitioner() throws Exception {
    return doctor("GENERAL_PRACTITIONER", "LYON_7", "0", false);
  }

  public ResultActions registerPatient(String birthDate, String sex, String extra) throws Exception {
    return postJson(
      "/api/medical/patients",
      """
      {"lastName":"Doe","firstName":"Jane","birthDate":"%s","sex":"%s","socialSecurityNumber":"%s","phone":"0612345678"%s}
      """.formatted(birthDate, sex, SSN, extra)
    );
  }

  public String patient(String extra) throws Exception {
    return read(registerPatient("1990-05-04", "FEMALE", extra).andExpect(status().isCreated()), "$.id");
  }

  public String patient() throws Exception {
    return patient("");
  }

  public ResultActions book(String patient, String doctor, LocalDateTime start, String channel, String companion) throws Exception {
    var companionJson = companion == null ? "" : ",\"companion\":\"" + companion + "\"";

    return postJson(
      "/api/medical/appointments",
      "{\"patientId\":\"%s\",\"doctorId\":\"%s\",\"start\":\"%s\",\"channel\":\"%s\"%s}".formatted(patient, doctor, start, channel, companionJson)
    );
  }

  public String consultation(String patient, String doctor) throws Exception {
    var consultation = postJson("/api/medical/consultations", "{\"patientId\":\"%s\",\"doctorId\":\"%s\",\"reason\":\"Check-up\"}".formatted(patient, doctor));

    return read(consultation.andExpect(status().isCreated()), "$.id");
  }

  public ResultActions prescribe(String consultation, String kind, String code) throws Exception {
    return postJson("/api/medical/consultations/" + consultation + "/prescription", "{\"items\":[{\"kind\":\"%s\",\"code\":\"%s\"}]}".formatted(kind, code));
  }

  public String prescription(String patient, String doctor, String kind, String code) throws Exception {
    var consultation = consultation(patient, doctor);

    return read(prescribe(consultation, kind, code).andExpect(status().isCreated()), "$.id");
  }

  public ResultActions close(String consultation) throws Exception {
    return postJson("/api/medical/consultations/" + consultation + "/closing", "{}");
  }

  public ResultActions sample(String patient, String prescription, String test, boolean fasting) throws Exception {
    return postJson(
      "/api/medical/laboratory/samples",
      "{\"patientId\":\"%s\",\"prescriptionId\":\"%s\",\"test\":\"%s\",\"fasting\":%s}".formatted(patient, prescription, test, fasting)
    );
  }

  public String validatedSample(String patient, String test, String value) throws Exception {
    var prescription = prescription(patient, generalPractitioner(), "LAB_TEST", test);
    var sample = read(sample(patient, prescription, test, true).andExpect(status().isCreated()), "$.id");
    postJson("/api/medical/laboratory/samples/" + sample + "/analysis", "{\"value\":" + value + "}").andExpect(status().isOk());
    postJson("/api/medical/laboratory/samples/" + sample + "/validation", "{\"biologist\":\"Dr Bio\"}").andExpect(status().isOk());

    return sample;
  }

  public ResultActions examination(String patient, String prescription, String exam, boolean negativePregnancyTest) throws Exception {
    return postJson(
      "/api/medical/imaging/examinations",
      "{\"patientId\":\"%s\",\"prescriptionId\":\"%s\",\"exam\":\"%s\",\"negativePregnancyTest\":%s}".formatted(
          patient,
          prescription,
          exam,
          negativePregnancyTest
        )
    );
  }

  public ResultActions get(String url) throws Exception {
    return rest.perform(MockMvcRequestBuilders.get(url));
  }

  public ResultActions postJson(String url, String body) throws Exception {
    return rest.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body));
  }

  public static String read(ResultActions result, String path) throws Exception {
    return JsonPath.read(result.andReturn().getResponse().getContentAsString(), path).toString();
  }
}
