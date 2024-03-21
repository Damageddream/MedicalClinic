package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.*;
import com.damageddream.medicalclinic.entity.Appointment;

import java.util.List;

public interface DoctorService {
    DoctorDTO findById(Long id);
    List<FacilityDTO> findFacilitiesByDoctor(Long id);
    DoctorDTO save(NewDoctorDTO newDoctorDTO);
    DoctorDTO addFacilityToDoctor(Long doctorId, GetIdCommand entityId);

    DoctorDTO addAppointment(Long doctorId, Appointment appointment);


}
