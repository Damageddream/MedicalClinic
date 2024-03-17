package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.GetFacilityIdCommand;
import com.damageddream.medicalclinic.entity.Doctor;
import com.damageddream.medicalclinic.entity.Facility;
import com.damageddream.medicalclinic.exception.EmailAlreadyExistsException;
import com.damageddream.medicalclinic.exception.PatientNotFoundException;
import com.damageddream.medicalclinic.repository.DoctorRepository;
import com.damageddream.medicalclinic.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final FacilityRepository facilityRepository;

    public Doctor findById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Doctor not found"));
    }

    @Transactional
    public Doctor save(Doctor doctor) {
//        var existingDoctor = doctorRepository.findById(doctor.getId());
//        if (existingDoctor.isPresent()) {
//            throw new EmailAlreadyExistsException("Doctor with that id exists");
//        }
        return doctorRepository.save(doctor);
    }

    @Transactional
    public Doctor addFacilityToDoctor(Long doctorId, GetFacilityIdCommand facilityId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new PatientNotFoundException("not found doctor"));

        Facility facility = facilityRepository.findById(facilityId.getFacilityId())
                .orElseThrow(() -> new PatientNotFoundException("Not found facility"));

        doctor.getFacilities().add(facility);
        return doctorRepository.save(doctor);
    }
}
