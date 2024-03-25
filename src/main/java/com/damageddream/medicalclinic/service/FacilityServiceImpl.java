package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.DoctorDTO;
import com.damageddream.medicalclinic.dto.FacilityDTO;
import com.damageddream.medicalclinic.dto.GetIdCommand;
import com.damageddream.medicalclinic.dto.NewFacilityDTO;
import com.damageddream.medicalclinic.dto.mapper.DoctorMapper;
import com.damageddream.medicalclinic.dto.mapper.FacilityMapper;
import com.damageddream.medicalclinic.entity.Doctor;
import com.damageddream.medicalclinic.entity.Facility;
import com.damageddream.medicalclinic.exception.DoctorAlreadyExistsException;
import com.damageddream.medicalclinic.exception.DoctorNotFoundException;
import com.damageddream.medicalclinic.exception.FacilityAlreadyExistsException;
import com.damageddream.medicalclinic.exception.FacilityNotFoundException;
import com.damageddream.medicalclinic.repository.DoctorRepository;
import com.damageddream.medicalclinic.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {
    private final FacilityRepository facilityRepository;
    private final DoctorRepository doctorRepository;
    private final FacilityMapper facilityMapper;
    private final DoctorMapper doctorMapper;

    @Override
    public FacilityDTO findById(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new FacilityNotFoundException("Facility not found"));
        return facilityMapper.toDTO(facility);
    }

    @Override
    public List<DoctorDTO> findDoctorsByFacility(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new FacilityNotFoundException("Facility not found"));
        return facility.getDoctors().stream().map(doctorMapper::toDTO).toList();
    }

    @Override
    @Transactional
    public FacilityDTO save(NewFacilityDTO newFacilityDTO) {
        var existingFacility = facilityRepository.findByName(newFacilityDTO.getName());
        if (existingFacility.isPresent()) {
            throw new FacilityAlreadyExistsException("Facility with that name already exists");
        }
        Facility facility = facilityMapper.fromDTO(newFacilityDTO);
        facilityRepository.save(facility);
        return facilityMapper.toDTO(facility);
    }

    @Override
    @Transactional
    public FacilityDTO addDoctorToFacility(Long facilityId, GetIdCommand entityId) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new FacilityNotFoundException("Facility not found"));

        Doctor doctor = doctorRepository.findById(entityId.getEntityId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));

        if (facility.getDoctors().contains(doctor)) {
            throw new DoctorAlreadyExistsException("Doctor already is in this facility");
        }

        doctor.getFacilities().add(facility);
        doctorRepository.save(doctor);
        return facilityMapper.toDTO(facility);
    }

    @Override
    @Transactional
    public FacilityDTO update(Long id, NewFacilityDTO newFacility) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new FacilityNotFoundException("Facility not found"));
        facilityMapper.updateFacilityFromDTO(newFacility, facility);
        facilityRepository.save(facility);
        return facilityMapper.toDTO(facility);
    }

    @Override
    @Transactional
    public FacilityDTO deleteFacility(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new FacilityNotFoundException("Facility not found"));
        for (Doctor doctor : facility.getDoctors()) {
            doctor.getFacilities().remove(facility);
        }
        facility.getDoctors().clear();

        facilityRepository.delete(facility);
        return facilityMapper.toDTO(facility);
    }
}
