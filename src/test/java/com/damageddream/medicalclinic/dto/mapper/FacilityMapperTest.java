package com.damageddream.medicalclinic.dto.mapper;

import com.damageddream.medicalclinic.dto.NewFacilityDTO;
import com.damageddream.medicalclinic.entity.Doctor;
import com.damageddream.medicalclinic.entity.Facility;
import com.damageddream.medicalclinic.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FacilityMapperTest {

    private FacilityMapper facilityMapper;

    @BeforeEach
    void setup() {
        this.facilityMapper = Mappers.getMapper(FacilityMapper.class);
    }

    @Test
    void fromFacility_validFacility_FacilityDTORetruned() {
        //given
        Facility facility = TestDataFactory.createFacility("Fac", "Warsaw");

        //when
        var result = facilityMapper.toDTO(facility);

        //then
        assertNotNull(result);
        assertEquals("Fac", result.getName());
        assertEquals("Warsaw", result.getCity());
        assertEquals("212", result.getZipCode());
        assertEquals("Hospital avenue", result.getStreet());
        assertEquals("3", result.getBuildingNo());
    }

    @Test
    void fromFacilityDTO_validFacilityDTO_facilityReturned() {
        //given
        NewFacilityDTO newFacilityDTO = TestDataFactory.createNewFacilityDTO("NewFac", "Warsaw");
        Doctor doctor1 = TestDataFactory.createDoctor("doc1@mail.com", "DocOne");
        Doctor doctor2 = TestDataFactory.createDoctor("doc2@mail.com", "DocTwo");
        TestDataFactory.addDoctorsToNewFacilityDTO(newFacilityDTO, List.of(doctor1, doctor2));

        //when
        var result = facilityMapper.fromDTO(newFacilityDTO);

        //then
        assertNotNull(result);
        assertEquals("NewFac", result.getName());
        assertEquals("Warsaw", result.getCity());
        assertEquals("212", result.getZipCode());
        assertEquals("3", result.getBuildingNo());
        assertEquals("Hospital avenue", result.getStreet());
        assertEquals("doc1@mail.com", result.getDoctors().get(0).getEmail());
        assertEquals("DocOne", result.getDoctors().get(0).getFirstName());
        assertEquals("doc2@mail.com", result.getDoctors().get(1).getEmail());
        assertEquals("DocTwo", result.getDoctors().get(1).getFirstName());
    }

    @Test
    void updateFacilityFromFacilityDTO_validFacilityDTO_facilityUpdated() {
        //given
        NewFacilityDTO newFacilityDTO = TestDataFactory.createNewFacilityDTO("NewFac", "Cracow");
        Facility facility = TestDataFactory.createFacility("Fac", "Warsaw");
        Doctor doctor = TestDataFactory.createDoctor("doc1@mail.com", "DocOne");
        facility.getDoctors().add(doctor);

        //when
        facilityMapper.updateFacilityFromDTO(newFacilityDTO, facility);

        //then
        assertNotNull(facility);
        assertEquals("NewFac", facility.getName());
        assertEquals("Cracow", facility.getCity());
        assertEquals("212", facility.getZipCode());
        assertEquals("3", facility.getBuildingNo());
        assertEquals("Hospital avenue", facility.getStreet());
        assertEquals("DocOne", facility.getDoctors().get(0).getFirstName());
        assertEquals("doc1@mail.com", facility.getDoctors().get(0).getEmail());

        assertNotEquals("Fac", facility.getName());
        assertNotEquals("Warsaw", facility.getCity());
    }
}
