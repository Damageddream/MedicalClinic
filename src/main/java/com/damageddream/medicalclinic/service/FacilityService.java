package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.DoctorDTO;
import com.damageddream.medicalclinic.dto.FacilityDTO;
import com.damageddream.medicalclinic.dto.GetIdCommand;
import com.damageddream.medicalclinic.dto.NewFacilityDTO;

import java.util.List;

public interface FacilityService {
    FacilityDTO findById(Long id);

    List<DoctorDTO> findDoctorsByFacility(Long id);

    FacilityDTO save(NewFacilityDTO newFacilityDTO);

    FacilityDTO addDoctorToFacility(Long facilityId, GetIdCommand entityId);
}
