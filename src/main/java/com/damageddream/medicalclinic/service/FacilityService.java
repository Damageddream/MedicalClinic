package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.entity.Facility;
import com.damageddream.medicalclinic.exception.PatientNotFoundException;
import com.damageddream.medicalclinic.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacilityService {
    private final FacilityRepository facilityRepository;

    public Facility findById(Long id) {
        return facilityRepository.findById(id).orElseThrow(
                ()->new PatientNotFoundException("Facility not found")
        );
    }

    public Facility save(Facility facility){
        return facilityRepository.save(facility);
    }
}
