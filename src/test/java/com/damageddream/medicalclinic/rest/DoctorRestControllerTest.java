package com.damageddream.medicalclinic.rest;

import com.damageddream.medicalclinic.dto.DoctorDTO;
import com.damageddream.medicalclinic.dto.FacilityDTO;
import com.damageddream.medicalclinic.dto.GetIdCommand;
import com.damageddream.medicalclinic.dto.NewDoctorDTO;
import com.damageddream.medicalclinic.exception.DoctorAlreadyExistsException;
import com.damageddream.medicalclinic.exception.DoctorNotFoundException;
import com.damageddream.medicalclinic.service.DoctorServiceImpl;
import com.damageddream.medicalclinic.util.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@SpringBootTest
@AutoConfigureMockMvc
public class DoctorRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DoctorServiceImpl doctorService;

    @Test
    void postDoctor_emailUnique_returnDoctorDTO() throws Exception {
        //given
        NewDoctorDTO newDoctorDTO = TestDataFactory.createNewDoctorDTO("doc@email.com", "Doc");
        DoctorDTO doctorDTO = TestDataFactory.createDoctorDTO("docTwo@email.com", "DocTwo");

        when(doctorService.save(newDoctorDTO)).thenReturn(doctorDTO);

        //when //then
        mockMvc.perform(post("/doctors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newDoctorDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("DocTwo"))
                .andExpect(jsonPath("$.lastName").value("doctorDTO"))
                .andExpect(jsonPath("$.email").value("docTwo@email.com"))
                .andExpect(jsonPath("$.specialization").value("surgeonDTO"));
    }

    @Test
    void getDoctorById_doctorExists_returnDoctorDTO() throws Exception {
        //given
        DoctorDTO doctorDTO = TestDataFactory.createDoctorDTO("doc@email.com", "Doc");

        when(doctorService.findById(1L)).thenReturn(doctorDTO);

        //when //then
        mockMvc.perform(get("/doctors/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Doc"))
                .andExpect(jsonPath("$.lastName").value("doctorDTO"))
                .andExpect(jsonPath("$.email").value("doc@email.com"))
                .andExpect(jsonPath("$.specialization").value("surgeonDTO"));
    }

    @Test
    void getFacilitiesByDoctorId_doctorExists_returnListOfFacilityDTO() throws Exception {
        //given
        FacilityDTO facility1 = TestDataFactory.createFacilityDTO("Fac", "Warsaw");
        FacilityDTO facility2 = TestDataFactory.createFacilityDTO("Fac2", "Warsaw2");

        List<FacilityDTO> facilities = List.of(facility1, facility2);

        when(doctorService.findFacilitiesByDoctor(1L)).thenReturn(facilities);

        //when //then
        mockMvc.perform(get("/doctors/{id}/facilities", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Fac"))
                .andExpect(jsonPath("$[0].city").value("Warsaw"))
                .andExpect(jsonPath("$[1].name").value("Fac2"))
                .andExpect(jsonPath("$[1].city").value("Warsaw2"));

    }

    @Test
    void putFacilityToDoctor_doctorAndFacilityExists_returnDoctorDTO() throws Exception {
        //given
        DoctorDTO doctorDTO = TestDataFactory.createDoctorDTO("doc@email.com", "Doc");
        GetIdCommand getIdCommand = new GetIdCommand(1L);

        when(doctorService.addFacilityToDoctor(1L, getIdCommand)).thenReturn(doctorDTO);

        //when //then
        mockMvc.perform(put("/doctors/{id}/facilities", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getIdCommand)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Doc"))
                .andExpect(jsonPath("$.lastName").value("doctorDTO"))
                .andExpect(jsonPath("$.email").value("doc@email.com"))
                .andExpect(jsonPath("$.specialization").value("surgeonDTO"));
    }

    @Test
    void postDoctor_emailNotUnique_returnConflictStatus() throws Exception {
        //given
        NewDoctorDTO newDoctorDTO = TestDataFactory.createNewDoctorDTO("doc@email.com", "Doc");

        when(doctorService.save(newDoctorDTO)).thenThrow(DoctorAlreadyExistsException.class);

        //when //then
        mockMvc.perform(post("/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDoctorDTO)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void getDoctor_doctorNotExists_returnNotFoundStatus() throws Exception {
        //given
        when(doctorService.findById(1L)).thenThrow(DoctorNotFoundException.class);

        //when //then
        mockMvc.perform(get("/doctors/{id}", 1L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getFacilitiesByDoctor_doctorNotExists_returnNotFoundStatus() throws Exception {
        //given
        when(doctorService.findFacilitiesByDoctor(1L)).thenThrow(DoctorNotFoundException.class);

        //when //then
        mockMvc.perform(get("/doctors/{id}/facilities", 1L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void putFacilitiesToDoctor_doctorNotExists_returnNotFoundStatus() throws Exception {
        //given
        GetIdCommand getIdCommand = new GetIdCommand(1L);

        when(doctorService.addFacilityToDoctor(1L, getIdCommand)).thenThrow(DoctorNotFoundException.class);

        //when //then
        mockMvc.perform(put("/doctors/{id}/facilities", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getIdCommand)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
