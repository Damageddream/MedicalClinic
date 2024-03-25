package com.damageddream.medicalclinic.dto.mapper;


import com.damageddream.medicalclinic.dto.NewDoctorDTO;
import com.damageddream.medicalclinic.entity.Doctor;
import com.damageddream.medicalclinic.entity.Facility;
import com.damageddream.medicalclinic.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        TestDataFactory.addFacilitiesToNewDoctorDto(newDoctorDTO, List.of(facility1, facility2));

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

    @Test
    void updateDoctorFromDTO_validDoctorDTO_doctorUpdated() {
        //given
        Doctor doctor = TestDataFactory.createDoctor("doc@email.com", "Doc");
        NewDoctorDTO newDoctorDTO = TestDataFactory.createNewDoctorDTO("docDTO@email.com", "DocDTO");
        Facility facility = TestDataFactory.createFacility("Fac", "Warsaw");
        doctor.getFacilities().add(facility);

        //when
        doctorMapper.updateDoctorFromDTO(newDoctorDTO, doctor);

        //then
        assertNotNull(doctor);
        assertEquals("docDTO@email.com", doctor.getEmail());
        assertEquals("DocDTO", doctor.getFirstName());
        assertEquals("NewDoctorDTO", doctor.getLastName());
        assertEquals("surgeonNewDto", doctor.getSpecialization());
        assertEquals("Fac", doctor.getFacilities().get(0).getName());
        assertEquals("Warsaw", doctor.getFacilities().get(0).getCity());

        assertNotEquals("doc@email.com", doctor.getEmail());
        assertNotEquals("Doc", doctor.getFirstName());
        assertNotEquals("Doctor", doctor.getLastName());
        assertNotEquals("surgeon", doctor.getSpecialization());
    }
}
