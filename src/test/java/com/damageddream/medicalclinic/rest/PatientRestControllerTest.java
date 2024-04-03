package com.damageddream.medicalclinic.rest;

import com.damageddream.medicalclinic.dto.ChangePasswordCommand;
import com.damageddream.medicalclinic.dto.NewPatientDTO;
import com.damageddream.medicalclinic.dto.PatientDTO;
import com.damageddream.medicalclinic.service.PatientService;
import com.damageddream.medicalclinic.util.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PatientService patientService;

    @Test
    void postPatient_EmailUnique_returnsPatientDTO() throws Exception {

        //given
        NewPatientDTO newPatientDTO = TestDataFactory
                .createNewPatientDTO("marNewDto@email.com", "MarNewDto", "654321");
        PatientDTO patientDTO = TestDataFactory.createPatientDTO("marDTO@email.com", "MarDTO");


        when(patientService.save(newPatientDTO)).thenReturn(patientDTO);

        //when then
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPatientDTO))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("MarDTO"))
                .andExpect(jsonPath("$.lastName").value("GrabDTO"))
                .andExpect(jsonPath("$.phoneNumber").value("222222"))
                .andExpect(jsonPath("$.email").value("marDTO@email.com"))
                .andExpect(jsonPath("$.birthday").value("1902-02-02"));

        verify(patientService, times(1)).save(newPatientDTO);
    }

    @Test
    void getPatientByEmail_emailExists_returnsPatientDTO() throws Exception {
        //given
        String email = "mar@email.com";
        PatientDTO patientDTO = TestDataFactory.createPatientDTO("marDTO@email.com","MarDTO");

        when(patientService.findByEmail(email)).thenReturn(patientDTO);

        //when then
        mockMvc.perform(get("/patients/{patientEmail}", email))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("MarDTO"))
                .andExpect(jsonPath("$.lastName").value("GrabDTO"))
                .andExpect(jsonPath("$.phoneNumber").value("222222"))
                .andExpect(jsonPath("$.email").value("marDTO@email.com"))
                .andExpect(jsonPath("$.birthday").value("1902-02-02"));

        verify(patientService, times(1)).findByEmail(email);
    }

    @Test
    void getAllPatients_patientsExists_returnPatientsDTOList() throws Exception {
        //given
        PatientDTO patientDTOfirst = TestDataFactory.createPatientDTO("marDTO@email.com","MarDTO");
        PatientDTO patientDTOsecond = TestDataFactory.createPatientDTO("lesz@mail.com", "Leszek");
        List<PatientDTO> patientsDTO = List.of(patientDTOfirst, patientDTOsecond);
        Pageable pageable = PageRequest.of(0, 2,
                Sort.by(Sort.Direction.ASC, "firstName"));

        when(patientService.findAll(any())).thenReturn(patientsDTO);

        //when then
        mockMvc.perform(get("/patients?page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("MarDTO"))
                .andExpect(jsonPath("$[0].email").value("marDTO@email.com"))
                .andExpect(jsonPath("$[1].firstName").value("Leszek"))
                .andExpect(jsonPath("$[1].email").value("lesz@mail.com"));

        verify(patientService, times(1)).findAll(pageable);
    }

    @Test
    void putPatient_patientInDb_returnsPatientDTO() throws Exception {
        //given
        String email = "mar@email.com";
        NewPatientDTO newPatientDTO = TestDataFactory
                .createNewPatientDTO("marNewDto@email.com", "MarNewDto", "654321");
        PatientDTO patientDTO = TestDataFactory.createPatientDTO("marDTO@email.com","MarDTO");

        when(patientService.update(email, newPatientDTO)).thenReturn(patientDTO);

        //when then
        mockMvc.perform(put("/patients/{patientEmail}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPatientDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("MarDTO"))
                .andExpect(jsonPath("$.lastName").value("GrabDTO"))
                .andExpect(jsonPath("$.phoneNumber").value("222222"))
                .andExpect(jsonPath("$.email").value("marDTO@email.com"))
                .andExpect(jsonPath("$.birthday").value("1902-02-02"));

        verify(patientService, times(1)).update(email, newPatientDTO);
    }

    @Test
    void deletePatient_patientExists_returnPatientDTO() throws Exception {
        //given
        String email = "mar@email.com";
        PatientDTO patientDTO = TestDataFactory.createPatientDTO("marDTO@email.com","MarDTO");

        when(patientService.delete(email)).thenReturn(patientDTO);

        //when then
        mockMvc.perform(delete("/patients/{patientEmail}", email))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("MarDTO"))
                .andExpect(jsonPath("$.lastName").value("GrabDTO"))
                .andExpect(jsonPath("$.phoneNumber").value("222222"))
                .andExpect(jsonPath("$.email").value("marDTO@email.com"))
                .andExpect(jsonPath("$.birthday").value("1902-02-02"));

        verify(patientService, times(1)).delete(email);
    }

    @Test
    void patchPatientPassword_patientExists_returnPatientDTO() throws Exception {
        //given
        String email = "marDTO@email.com";
        ChangePasswordCommand newPassword = new ChangePasswordCommand("newPassword");
        PatientDTO patientDTO = TestDataFactory.createPatientDTO(email,"MarDTO");

        when(patientService.editPassword(newPassword, email)).thenReturn(patientDTO);

        //when then
        mockMvc.perform(patch("/patients/{patientEmail}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassword)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("MarDTO"))
                .andExpect(jsonPath("$.lastName").value("GrabDTO"))
                .andExpect(jsonPath("$.phoneNumber").value("222222"))
                .andExpect(jsonPath("$.email").value("marDTO@email.com"))
                .andExpect(jsonPath("$.birthday").value("1902-02-02"));

        verify(patientService, times(1)).editPassword(newPassword, email);

    }
}
