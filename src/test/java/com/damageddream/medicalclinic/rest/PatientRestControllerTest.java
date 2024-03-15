package com.damageddream.medicalclinic.rest;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
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
    void postPatient_EmailUniqe_returnsPatientDTO() throws Exception {

        //given
        NewPatientDTO newPatientDTO = TestDataFactory.getDefault_NEWPATIENTDTO();
        PatientDTO patientDTO = TestDataFactory.getDefault_PATIENTDTO();

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

    }

    @Test
    void getPatientByEmail_emailExists_returnsPatientDTO() throws Exception {
        //given
        String email = "mar@email.com";
        PatientDTO patientDTO = TestDataFactory.getDefault_PATIENTDTO();

        when(patientService.findByEmail(email)).thenReturn(patientDTO);

        //when then
        mockMvc.perform(get("/patients/{patientEmail}",email))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("MarDTO"))
                .andExpect(jsonPath("$.lastName").value("GrabDTO"))
                .andExpect(jsonPath("$.phoneNumber").value("222222"))
                .andExpect(jsonPath("$.email").value("marDTO@email.com"))
                .andExpect(jsonPath("$.birthday").value("1902-02-02"));
    }

    @Test
    void getAllPatients_patientsExists_returnPatientsDTOList() throws Exception {
        //given
        PatientDTO patientDTOfirst = TestDataFactory.getDefault_PATIENTDTO();
        PatientDTO patientDTOsecond = TestDataFactory.createPatientDTO("lesz@mail.com", "Leszek");
        List<PatientDTO> patientsDTO = List.of(patientDTOfirst, patientDTOsecond);

        when(patientService.findAll()).thenReturn(patientsDTO);

        //when then
        mockMvc.perform(get("/patients"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("MarDTO"))
                .andExpect(jsonPath("$[0].email").value("marDTO@email.com"))
                .andExpect(jsonPath("$[1].firstName").value("Leszek"))
                .andExpect(jsonPath("$[1].email").value("lesz@mail.com"));

    }

    @Test
    void updatePatient_patientInDb_returnsPatientDTO() throws Exception {
        //given
        String email = "mar@email.com";
        NewPatientDTO newPatientDTO = TestDataFactory.getDefault_NEWPATIENTDTO();
        PatientDTO patientDTO = TestDataFactory.getDefault_PATIENTDTO();

        when(patientService.update(email,newPatientDTO)).thenReturn(patientDTO);

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
    }
}
