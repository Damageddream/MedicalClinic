package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.GetIdCommand;
import com.damageddream.medicalclinic.dto.NewDoctorDTO;
import com.damageddream.medicalclinic.dto.mapper.DoctorMapper;
import com.damageddream.medicalclinic.dto.mapper.FacilityMapper;
import com.damageddream.medicalclinic.entity.Doctor;
import com.damageddream.medicalclinic.entity.Facility;
import com.damageddream.medicalclinic.exception.DoctorAlreadyExistsException;
import com.damageddream.medicalclinic.exception.DoctorNotFoundException;
import com.damageddream.medicalclinic.repository.DoctorRepository;
import com.damageddream.medicalclinic.repository.FacilityRepository;
import com.damageddream.medicalclinic.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class DoctorServiceTest {
    private FacilityMapper facilityMapper;
    private DoctorMapper doctorMapper;
    private FacilityRepository facilityRepository;
    private DoctorRepository doctorRepository;

    private DoctorService doctorService;

    @BeforeEach
    void setup() {
        this.facilityMapper = Mappers.getMapper(FacilityMapper.class);
        this.doctorMapper = Mappers.getMapper(DoctorMapper.class);
        this.facilityRepository = Mockito.mock(FacilityRepository.class);
        this.doctorRepository = Mockito.mock(DoctorRepository.class);

        this.doctorService = new DoctorServiceImpl(doctorRepository, facilityRepository, doctorMapper, facilityMapper);
    }

    @Test
    void findDoctorById_doctorExists_doctorDTOReturned() {
        //given
        Doctor doctor = TestDataFactory.createDoctor("doc@email.com", "DocOne");

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        //when
        var result = doctorService.findById(1L);

        //then
        assertNotNull(result);
        assertEquals("DocOne", result.getFirstName());
        assertEquals("doc@email.com", result.getEmail());
        assertEquals("Doctor", result.getLastName());
        assertEquals("surgeon", result.getSpecialization());
    }

    @Test
    void findDoctorById_doctorNotExists_doctorNotExistsExceptionThrown() {
        //given
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        //then //when
        DoctorNotFoundException ex = assertThrows(DoctorNotFoundException.class,
                ()->doctorService.findById(1L));
        assertEquals("Doctor not found", ex.getMessage());
    }

    @Test
    void saveDoctor_doctorNotInDb_doctorDTOReturned() {
        //given
        NewDoctorDTO newDoctorDTO = TestDataFactory.createNewDoctorDTO("doc@email.com", "DocOne");

        when(doctorRepository.findByEmail("doc@email.com")).thenReturn(Optional.empty());

        //when
        var result = doctorService.save(newDoctorDTO);

        //then
        assertNotNull(result);
        assertEquals("doc@email.com", result.getEmail());
        assertEquals("DocOne", result.getFirstName());
        assertEquals("NewDoctorDTO", result.getLastName());
        assertEquals("surgeonNewDto", result.getSpecialization());
    }

    @Test
    void saveDoctor_doctorInDb_DoctorAlreadyExistsExceptionThrown() {
        //given
        Doctor doctor = TestDataFactory.createDoctor("doc@email.com", "DocOne");
        NewDoctorDTO newDoctorDTO = TestDataFactory.createNewDoctorDTO("doc@email.com", "DocOne");

        when(doctorRepository.findByEmail("doc@email.com")).thenReturn(Optional.of(doctor));

        //when //then
        DoctorAlreadyExistsException ex = assertThrows(DoctorAlreadyExistsException.class,
                ()-> doctorService.save(newDoctorDTO));

        assertEquals("Doctor with that email already exists", ex.getMessage());
    }

    @Test
    void findFacilitiesByDoctor_doctorExists_returnListOfFacilitesDTO() {
        //given
        Doctor doctor = TestDataFactory.createDoctor("doc@email.com", "DocOne");

        Facility facility1 = TestDataFactory.createFacility("Fac", "Warsaw");
        Facility facility2 = TestDataFactory.createFacility("Fac2", "Warsaw2");

        List<Facility> facilities = List.of(facility1, facility2);

        TestDataFactory.addFacilitiesToDoctor(doctor, facilities);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        //when
        var result = doctorService.findFacilitiesByDoctor(1L);

        //then
        assertNotNull(result);
        assertEquals("Fac", result.get(0).getName());
        assertEquals("Warsaw", result.get(0).getCity());
        assertEquals("Fac2", result.get(1).getName());
        assertEquals("Warsaw2", result.get(1).getCity());

    }

    @Test
    void addFacilityToDoctor_doctorExists_doctorDTOReturned() {
        //given
        Facility facility = TestDataFactory.createFacility("Fac", "Warsaw");
        Doctor doctor = TestDataFactory.createDoctor("doc@email.com", "DocOne");
        GetIdCommand getIdCommand = new GetIdCommand(1L);

        when(facilityRepository.findById(1L)).thenReturn(Optional.of(facility));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));

        //when
        var result = doctorService.addFacilityToDoctor(2L, getIdCommand);

        //then
        assertNotNull(result);
        assertEquals("DocOne", result.getFirstName());
        assertEquals("doc@email.com", result.getEmail());
        assertEquals("Doctor", result.getLastName());
        assertEquals("surgeon", result.getSpecialization());
    }

}
