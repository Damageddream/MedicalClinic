package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.GetIdCommand;
import com.damageddream.medicalclinic.dto.NewFacilityDTO;
import com.damageddream.medicalclinic.dto.mapper.DoctorMapper;
import com.damageddream.medicalclinic.dto.mapper.FacilityMapper;
import com.damageddream.medicalclinic.entity.Doctor;
import com.damageddream.medicalclinic.entity.Facility;
import com.damageddream.medicalclinic.exception.DoctorNotFoundException;
import com.damageddream.medicalclinic.exception.FacilityAlreadyExistsException;
import com.damageddream.medicalclinic.exception.FacilityNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FacilityServiceTest {
    private FacilityMapper facilityMapper;
    private DoctorMapper doctorMapper;
    private FacilityRepository facilityRepository;
    private DoctorRepository doctorRepository;

    private FacilityService facilityService;

    @BeforeEach
    void setup() {
        this.facilityMapper = Mappers.getMapper(FacilityMapper.class);
        this.doctorMapper = Mappers.getMapper(DoctorMapper.class);
        this.facilityRepository = Mockito.mock(FacilityRepository.class);
        this.doctorRepository = Mockito.mock(DoctorRepository.class);

        this.facilityService = new FacilityServiceImpl(facilityRepository, doctorRepository,
                facilityMapper, doctorMapper);
    }

    @Test
    void findFacilityById_facilityExists_facilityDTOReturned() {
        //given
        Facility facility = TestDataFactory.createFacility("Fac", "Warsaw");

        when(facilityRepository.findById(1L)).thenReturn(Optional.of(facility));

        //when
        var result = facilityService.findById(1L);

        //then
        assertNotNull(result);
        assertEquals("Fac", result.getName());
        assertEquals("Warsaw", result.getCity());
        assertEquals("212", result.getZipCode());
        assertEquals("Hospital avenue", result.getStreet());
        assertEquals("3", result.getBuildingNo());

    }

    @Test
    void findFacilityById_facilityNotExists_facilityNotFoundExceptionThrown() {
        //given
        when(facilityRepository.findById(1L)).thenReturn(Optional.empty());

        //then //when
        FacilityNotFoundException ex = assertThrows(FacilityNotFoundException.class,
                () -> facilityService.findById(1L));
        assertEquals("Facility not found", ex.getMessage());
    }

    @Test
    void saveFacility_facilityNotInDb_facilityDTOReturned() {
        //given
        NewFacilityDTO newFacilityDTO = TestDataFactory.createNewFacilityDTO("Fac", "Warsaw");

        when(facilityRepository.findByName("Fac")).thenReturn(Optional.empty());

        //when
        var result = facilityService.save(newFacilityDTO);

        //then
        assertNotNull(result);
        assertEquals("Fac", result.getName());
        assertEquals("Warsaw", result.getCity());
        assertEquals("212", result.getZipCode());
        assertEquals("Hospital avenue", result.getStreet());
        assertEquals("3", result.getBuildingNo());
    }

    @Test
    void saveFacility_facilityInDb_FacilityAlreadyExistsExceptionThrown() {
        //given
        Facility facility = TestDataFactory.createFacility("Fac", "Warsaw");
        NewFacilityDTO newFacilityDTO = TestDataFactory.createNewFacilityDTO("Fac", "Warsaw");

        when(facilityRepository.findByName("Fac")).thenReturn(Optional.of(facility));

        //when then
        FacilityAlreadyExistsException ex = assertThrows(FacilityAlreadyExistsException.class,
                () -> facilityService.save(newFacilityDTO));

        assertEquals("Facility with that name already exists", ex.getMessage());
    }

    @Test
    void findDoctorsByFacility_facilityExists_returnListOfDoctorDTO() {
        //given
        Facility facility = TestDataFactory.createFacility("Fac", "Warsaw");

        Doctor doctor1 = TestDataFactory.createDoctor("doc@email.com", "DocOne");
        Doctor doctor2 = TestDataFactory.createDoctor("doc2@email.com", "DocTwo");

        List<Doctor> doctors = List.of(doctor1, doctor2);

        TestDataFactory.addDoctorsToFacility(facility, doctors);

        when(facilityRepository.findById(1L)).thenReturn(Optional.of(facility));

        //when
        var result = facilityService.findDoctorsByFacility(1L);

        //then
        assertNotNull(result);
        assertEquals("doc@email.com", result.get(0).getEmail());
        assertEquals("DocOne", result.get(0).getFirstName());
        assertEquals("doc2@email.com", result.get(1).getEmail());
        assertEquals("DocTwo", result.get(1).getFirstName());
    }

    @Test
    void findDoctorsByFacility_facilityNotExists_facilityNotFoundExceptionThrown() {
        //given
        when(facilityRepository.findById(1L)).thenReturn(Optional.empty());

        //then //when
        FacilityNotFoundException ex = assertThrows(FacilityNotFoundException.class,
                () -> facilityService.findDoctorsByFacility(1L));
        assertEquals("Facility not found", ex.getMessage());
    }

    @Test
    void addDoctorToFacility_facilityExists_facilityDTOReturned() {
        //given
        Facility facility = TestDataFactory.createFacility("Fac", "Warsaw");
        Doctor doctor = TestDataFactory.createDoctor("doc@email.com", "DocOne");
        GetIdCommand getIdCommand = new GetIdCommand(1L);

        when(facilityRepository.findById(2L)).thenReturn(Optional.of(facility));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        //when
        var result = facilityService.addDoctorToFacility(2L, getIdCommand);

        //then
        assertNotNull(result);
        assertEquals("Fac", result.getName());
        assertEquals("Warsaw", result.getCity());
        assertEquals("212", result.getZipCode());
        assertEquals("Hospital avenue", result.getStreet());
        assertEquals("3", result.getBuildingNo());
    }

    @Test
    void addDoctorToFacility_facilityNotExists_facilityNotFoundExceptionThrown() {
        //given
        GetIdCommand getIdCommand = new GetIdCommand(1L);
        when(facilityRepository.findById(2L)).thenReturn(Optional.empty());

        //then //when
        FacilityNotFoundException ex = assertThrows(FacilityNotFoundException.class,
                () -> facilityService.addDoctorToFacility(2L, getIdCommand));
        assertEquals("Facility not found", ex.getMessage());
    }

    @Test
    void addDoctorToFacility_doctorNotExists_doctorNotFoundExceptionThrown() {
        //given
        GetIdCommand getIdCommand = new GetIdCommand(1L);
        Facility facility = TestDataFactory.createFacility("Fac", "Warsaw");

        when(facilityRepository.findById(2L)).thenReturn(Optional.of(facility));
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        //then //when
        DoctorNotFoundException ex = assertThrows(DoctorNotFoundException.class,
                () -> facilityService.addDoctorToFacility(2L, getIdCommand));
        assertEquals("Doctor not found", ex.getMessage());
    }

    @Test
    void deleteFacility_facilityExists_returnFacilityDTO() {
        //given
        Facility facility = TestDataFactory.createFacility("Fac", "Warsaw");
        when(facilityRepository.findById(any())).thenReturn(Optional.of(facility));

        //when
        var result = facilityService.deleteFacility(1L);

        //then
        assertNotNull(result);
        assertEquals("Fac", result.getName());
        assertEquals("Warsaw", result.getCity());
        assertEquals("212", result.getZipCode());
        assertEquals("Hospital avenue", result.getStreet());
        assertEquals("3", result.getBuildingNo());
    }

    @Test
    void deleteFacility_facilityNotExists_facilityNotFoundExceptionThrown() {
        //given
        when(facilityRepository.findById(any())).thenReturn(Optional.empty());

        //then //when
        FacilityNotFoundException ex = assertThrows(FacilityNotFoundException.class,
                () -> facilityService.deleteFacility(1L));
        assertEquals("Facility not found", ex.getMessage());
    }

    @Test
    void updateFacility_facilityExists_returnFacilityDTO() {
        //given
        Facility facility = TestDataFactory.createFacility("Fac", "Warsaw");
        NewFacilityDTO newFacilityDTO = TestDataFactory.createNewFacilityDTO("NewFac", "Cracow");

        when(facilityRepository.findById(any())).thenReturn(Optional.of(facility));

        //when
        var result = facilityService.update(1L, newFacilityDTO);

        //then
        assertNotNull(result);
        assertEquals("NewFac", result.getName());
        assertEquals("Cracow", result.getCity());
        assertEquals("212", result.getZipCode());
        assertEquals("Hospital avenue", result.getStreet());
        assertEquals("3", result.getBuildingNo());
    }

    @Test
    void updateFacility_facilityNotExists_facilityNotFoundExceptionThrown() {
        //given
        NewFacilityDTO newFacilityDTO = TestDataFactory.createNewFacilityDTO("NewFac", "Cracow");
        when(facilityRepository.findById(any())).thenReturn(Optional.empty());

        //then //when
        FacilityNotFoundException ex = assertThrows(FacilityNotFoundException.class,
                () -> facilityService.update(1L, newFacilityDTO));
        assertEquals("Facility not found", ex.getMessage());
    }
}
