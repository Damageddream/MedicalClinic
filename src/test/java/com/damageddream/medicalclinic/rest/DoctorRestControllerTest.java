package com.damageddream.medicalclinic.rest;

import com.damageddream.medicalclinic.dto.DoctorDTO;
import com.damageddream.medicalclinic.dto.NewDoctorDTO;
import com.damageddream.medicalclinic.service.DoctorService;
import com.damageddream.medicalclinic.service.DoctorServiceImpl;
import com.damageddream.medicalclinic.util.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

public class DoctorRestControllerTest {
//    NewDoctorDTO newDoctorDTO = TestDataFactory.createNewDoctorDTO("doc@email.com", "Doc");
//    DoctorDTO doctorDTO = TestDataFactory.createDoctorDTO("docTwo@email.com", "DocTwo");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DoctorServiceImpl doctorService;


}
