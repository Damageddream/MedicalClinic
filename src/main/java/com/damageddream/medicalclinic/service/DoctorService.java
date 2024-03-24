package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.DoctorDTO;
import com.damageddream.medicalclinic.dto.FacilityDTO;
import com.damageddream.medicalclinic.dto.GetIdCommand;
import com.damageddream.medicalclinic.dto.NewDoctorDTO;

import java.util.List;

public interface DoctorService {
    DoctorDTO findById(Long id);

    List<FacilityDTO> findFacilitiesByDoctor(Long id);

    DoctorDTO save(NewDoctorDTO newDoctorDTO);

    DoctorDTO addFacilityToDoctor(Long doctorId, GetIdCommand entityId);

    DoctorDTO deleteDoctor(Long id);


}
