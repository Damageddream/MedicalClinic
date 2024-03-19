package com.damageddream.medicalclinic.rest;

import com.damageddream.medicalclinic.dto.*;
import com.damageddream.medicalclinic.exception.FacilityAlreadyExistsException;
import com.damageddream.medicalclinic.exception.FacilityNotFoundException;
import com.damageddream.medicalclinic.service.FacilityServiceImpl;
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
public class FacilityRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacilityServiceImpl facilityService;

    @Test
    void postFacility_nameUnique_returnFacilityDTO() throws Exception {
        //given
        NewFacilityDTO newFacilityDTO = TestDataFactory.createNewFacilityDTO("Fac", "Warsaw");
        FacilityDTO facilityDTO = TestDataFactory.createFacilityDTO("Fac2", "Warsaw2");

        when(facilityService.save(newFacilityDTO)).thenReturn(facilityDTO);

        //when /then
        mockMvc.perform(post("/facilities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newFacilityDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Fac2"))
                .andExpect(jsonPath("$.city").value("Warsaw2"))
                .andExpect(jsonPath("$.zipCode").value("212Dto"))
                .andExpect(jsonPath("$.street").value("Dto avenue "))
                .andExpect(jsonPath("$.buildingNo").value("5"));
    }

    @Test
    void getFacilityById_facilityExists_returnFacilityDTO() throws Exception {
        //given
        FacilityDTO facilityDTO = TestDataFactory.createFacilityDTO("Fac", "Warsaw");

        when(facilityService.findById(1L)).thenReturn(facilityDTO);

        //when//then
        mockMvc.perform(get("/facilities/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fac"))
                .andExpect(jsonPath("$.city").value("Warsaw"))
                .andExpect(jsonPath("$.zipCode").value("212Dto"))
                .andExpect(jsonPath("$.street").value("Dto avenue "))
                .andExpect(jsonPath("$.buildingNo").value("5"));
    }

    @Test
    void getDoctorsByFacilityId_facilityExists_returnListOfDoctorDTO() throws Exception {
        //given
        DoctorDTO doctor1 = TestDataFactory.createDoctorDTO("doc@email.com", "DocOne");
        DoctorDTO doctor2 = TestDataFactory.createDoctorDTO("doc2@email.com", "DocTwo");

        List<DoctorDTO> doctors = List.of(doctor1, doctor2);

        when(facilityService.findDoctorsByFacility(1L)).thenReturn(doctors);

        //when //then
        mockMvc.perform(get("/facilities/{id}/doctors", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("DocOne"))
                .andExpect(jsonPath("$[0].email").value("doc@email.com"))
                .andExpect(jsonPath("$[1].firstName").value("DocTwo"))
                .andExpect(jsonPath("$[1].email").value("doc2@email.com"));
    }

    @Test
    void putDoctorToFacility_doctorAndFacilityExists_returnFacilityDTO() throws Exception {
        //given
        FacilityDTO facilityDTO = TestDataFactory.createFacilityDTO("Fac", "Warsaw");
        GetIdCommand getIdCommand = new GetIdCommand(1L);

        when(facilityService.addDoctorToFacility(1L,getIdCommand)).thenReturn(facilityDTO);

        //when //then
        mockMvc.perform(put("/facilities/{id}/doctors", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getIdCommand)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fac"))
                .andExpect(jsonPath("$.city").value("Warsaw"))
                .andExpect(jsonPath("$.zipCode").value("212Dto"))
                .andExpect(jsonPath("$.street").value("Dto avenue "))
                .andExpect(jsonPath("$.buildingNo").value("5"));
    }

    @Test
    void postFacility_nameNotUnique_returnConflictStatus() throws Exception {
        //given
        NewFacilityDTO newFacilityDTO = TestDataFactory.createNewFacilityDTO("Fac", "Warsaw");

        when(facilityService.save(newFacilityDTO)).thenThrow(FacilityAlreadyExistsException.class);

        //when //then
        mockMvc.perform(post("/facilities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newFacilityDTO)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void getDoctor_doctorNotExists_returnNotFoundStatus() throws Exception {
        //given
        when(facilityService.findById(1L)).thenThrow(FacilityNotFoundException.class);

        //when //then
        mockMvc.perform(get("/facilities/{id}", 1L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getDoctorsByFacility_facilityNotExists_returnNotFoundStatus() throws Exception {
        //given
        when(facilityService.findDoctorsByFacility(1L)).thenThrow(FacilityNotFoundException.class);

        //when //then
        mockMvc.perform(get("/facilities/{id}/doctors", 1L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void putDoctorToFacility_facilityNotExists_returnNotFoundStatus() throws Exception {
        //given
        GetIdCommand getIdCommand = new GetIdCommand(1L);

        when(facilityService.addDoctorToFacility(1L, getIdCommand)).thenThrow(FacilityNotFoundException.class);

        //when //then
        mockMvc.perform(put("/facilities/{id}/doctors", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getIdCommand)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }




}
