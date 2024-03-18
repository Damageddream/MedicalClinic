package com.damageddream.medicalclinic.dto.mapper;


import com.damageddream.medicalclinic.dto.NewDoctorDTO;
import com.damageddream.medicalclinic.entity.Doctor;
import com.damageddream.medicalclinic.entity.Facility;
import com.damageddream.medicalclinic.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DoctorMapperTest {
    private DoctorMapper doctorMapper;

    @BeforeEach
    void setup() {
        this.doctorMapper = Mappers.getMapper(DoctorMapper.class);
    }

    @Test
    void fromDoctor_validDoctor_DoctorDTOReturned() {
        //given
        Doctor doctor = TestDataFactory.createDoctor("doc@email.com", "Doc");

        //when
        var result = doctorMapper.toDTO(doctor);

        //then
        assertNotNull(result);
        assertEquals("doc@email.com", result.getEmail());
        assertEquals("Doc", result.getFirstName());
        assertEquals("Doctor", result.getLastName());
        assertEquals("surgeon", result.getSpecialization());
    }

    @Test
    void fromDoctorDTO_validDoctorDTO_doctorReturned() {
        //given
        NewDoctorDTO newDoctorDTO = TestDataFactory.createNewDoctorDTO("doc@email.com", "Doc");
        Facility facility1 = TestDataFactory.createFacility("Fac", "Warsaw");
        Facility facility2 = TestDataFactory.createFacility("Ility", "Carcow");
        TestDataFactory.addFacilitiesToNewDoctorDto(newDoctorDTO, List.of(facility1,facility2));

        //when
        var result = doctorMapper.fromDTO(newDoctorDTO);

        //then
        assertNotNull(result);
        assertEquals("doc@email.com", result.getEmail());
        assertEquals("Doc", result.getFirstName());
        assertEquals("passNewDto", result.getPassword());
        assertEquals("NewDoctorDTO", result.getLastName());
        assertEquals("surgeonNewDto", result.getSpecialization());
        assertEquals("Fac", result.getFacilities().get(0).getName());
        assertEquals("Warsaw", result.getFacilities().get(0).getCity());
        assertEquals("Ility", result.getFacilities().get(1).getName());
        assertEquals("Carcow", result.getFacilities().get(1).getCity());

    }
}
