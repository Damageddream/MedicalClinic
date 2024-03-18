package com.damageddream.medicalclinic.rest;

import com.damageddream.medicalclinic.dto.FacilityDTO;
import com.damageddream.medicalclinic.dto.NewFacilityDTO;
import com.damageddream.medicalclinic.service.FacilityService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void postDoctor_EmailUnique_returnDoctorDTO() throws Exception {
        //given
        NewFacilityDTO newFacilityDTO = TestDataFactory.createNewFacilityDTO("Fac", "Warsaw");
        FacilityDTO facilityDTO = TestDataFactory.createFacilityDTO("Fac2", "Warsaw2");

        when(facilityService.save(newFacilityDTO)).thenReturn(facilityDTO);

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
}
