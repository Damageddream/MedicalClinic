package com.damageddream.medicalclinic.integration;

import com.damageddream.medicalclinic.dto.ChangePasswordCommand;
import com.damageddream.medicalclinic.dto.NewPatientDTO;
import com.damageddream.medicalclinic.util.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"file:src/test/resources/scripts/insert_data.sql"},
        config = @SqlConfig(encoding = "utf-8", transactionMode = SqlConfig.TransactionMode.ISOLATED),
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"file:src/test/resources/scripts/clear_data.sql"},
        config = @SqlConfig(encoding = "utf-8", transactionMode = SqlConfig.TransactionMode.ISOLATED),
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

public class PatientIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getPatientByEmail_patientExists_responsePatientDtoJson() throws Exception {
        mockMvc.perform(get("/patients/{patientEmail}", "john@gmail.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("J"))
                .andExpect(jsonPath("$.lastName").value("D"))
                .andExpect(jsonPath("$.phoneNumber").value("999999"))
                .andExpect(jsonPath("$.email").value("john@gmail.com"))
                .andExpect(jsonPath("$.birthday").value("1900-12-20"));
    }

    @Test
    void getPatientByEmail_emailNotInDb_responseNotFound() throws Exception {
        mockMvc.perform(get("/patients/{patientEmail}", "notexists@gmail.com"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Patient not found"));
    }

    @Test
    void getAllPatients_patientsExists_responseListPatientsDto() throws Exception {
        mockMvc.perform(get("/patients"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("J"))
                .andExpect(jsonPath("$[0].email").value("john@gmail.com"))
                .andExpect(jsonPath("$[1].firstName").value("Z"))
                .andExpect(jsonPath("$[1].email").value("wir@gmail.com"));
    }

    @Test
    void postPatient_patientNotInDb_responsePatientDto() throws Exception {
        //given
        NewPatientDTO patientDTO = TestDataFactory.getNewPatientDTO();

        //when then
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("MarNewDto"))
                .andExpect(jsonPath("$.lastName").value("GrabNewDto"))
                .andExpect(jsonPath("$.phoneNumber").value("333333333"))
                .andExpect(jsonPath("$.email").value("marNewDto@email.com"))
                .andExpect(jsonPath("$.birthday").value("1903-03-03"));

    }

    @Test
    void postPatient_patientAlreadyInDb_responseConflict() throws Exception {
        //given
        NewPatientDTO newPatientDTO = TestDataFactory
                .createNewPatientDTO("john@gmail.com", "J", "123456");

        //when then
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPatientDTO))
                )
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Patient with that email already exists"));
    }

    @Test
    void putPatient_patientExists_responsePatientDto() throws Exception {
        //given
        NewPatientDTO newPatientDTO = TestDataFactory
                .createNewPatientDTO("updated@gmail.com", "Updated", "654321");

        //when then
        mockMvc.perform(put("/patients/{patientEmail}", "john@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPatientDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("GrabNewDto"))
                .andExpect(jsonPath("$.phoneNumber").value("333333333"))
                .andExpect(jsonPath("$.email").value("updated@gmail.com"))
                .andExpect(jsonPath("$.birthday").value("1903-03-03"));
    }

    @Test
    void putPatient_idCardNoChanged_responseForbidden() throws Exception {
        //given
        NewPatientDTO newPatientDTO = TestDataFactory
                .createNewPatientDTO("updated@gmail.com", "Updated", "1111");

        //when then
        mockMvc.perform(put("/patients/{patientEmail}", "john@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPatientDTO)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Changes to Id Card No are forbidden"));

    }

    @Test
    void putPatient_nullField_responseBadRequest() throws Exception {
        //given
        NewPatientDTO newPatientDTO = TestDataFactory
                .createNewPatientDTO("updated@gmail.com", null, "654321");

        //when then
        mockMvc.perform(put("/patients/{patientEmail}", "john@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPatientDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("All fields of new Patient must be completed"));

    }

    @Test
    void putPatient_emailAlreadyTaken_responseConflict() throws Exception {
        //given
        NewPatientDTO newPatientDTO = TestDataFactory
                .createNewPatientDTO("wir@gmail.com", "Updated", "654321");

        //when then
        mockMvc.perform(put("/patients/{patientEmail}", "john@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPatientDTO)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("New email is not available."));

    }

    @Test
    void deletePatient_patientExists_responsePatientDto() throws Exception {
        mockMvc.perform(delete("/patients/{patientEmail}", "john@gmail.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("J"))
                .andExpect(jsonPath("$.lastName").value("D"))
                .andExpect(jsonPath("$.phoneNumber").value("999999"))
                .andExpect(jsonPath("$.email").value("john@gmail.com"))
                .andExpect(jsonPath("$.birthday").value("1900-12-20"));
    }

    @Test
    void deletePatient_emailNotInDb_responseNotFound() throws Exception {
        mockMvc.perform(delete("/patients/{patientEmail}", "notexists@gmail.com"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Patient not found"));
    }

    @Test
    void patchPatientPassword_patientExists_responsePatientDto() throws Exception {
        ChangePasswordCommand newPassword = new ChangePasswordCommand("changedPassword");
        mockMvc.perform(delete("/patients/{patientEmail}", "john@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassword))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("J"))
                .andExpect(jsonPath("$.lastName").value("D"))
                .andExpect(jsonPath("$.phoneNumber").value("999999"))
                .andExpect(jsonPath("$.email").value("john@gmail.com"))
                .andExpect(jsonPath("$.birthday").value("1900-12-20"));
    }

    @Test
    void patchPatientPassword_emailNotInDb_responseNotFound() throws Exception {
        ChangePasswordCommand newPassword = new ChangePasswordCommand("changedPassword");
        mockMvc.perform(patch("/patients/{patientEmail}", "notexists@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassword))
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Patient not found"));
    }

}
