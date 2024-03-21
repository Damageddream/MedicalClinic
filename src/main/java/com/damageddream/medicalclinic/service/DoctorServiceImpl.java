package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.*;
import com.damageddream.medicalclinic.dto.mapper.AppointmentMapper;
import com.damageddream.medicalclinic.dto.mapper.DoctorMapper;
import com.damageddream.medicalclinic.dto.mapper.FacilityMapper;
import com.damageddream.medicalclinic.entity.Appointment;
import com.damageddream.medicalclinic.entity.Doctor;
import com.damageddream.medicalclinic.entity.Facility;
import com.damageddream.medicalclinic.exception.*;
import com.damageddream.medicalclinic.repository.AppointmentRepository;
import com.damageddream.medicalclinic.repository.DoctorRepository;
import com.damageddream.medicalclinic.repository.FacilityRepository;
import com.damageddream.medicalclinic.validation.DataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final FacilityRepository facilityRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorMapper doctorMapper;
    private final FacilityMapper facilityMapper;
    private final DataValidator dataValidator;
    private final AppointmentMapper appointmentMapper;


    @Override
    public DoctorDTO findById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));
        return doctorMapper.toDTO(doctor);
    }
    @Override
    public List<FacilityDTO> findFacilitiesByDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));
        return doctor.getFacilities().stream().map(facilityMapper::toDTO).toList();
    }
    @Override
    @Transactional
    public DoctorDTO save(NewDoctorDTO newDoctorDTO) {
        var existingDoctor = doctorRepository.findByEmail(newDoctorDTO.getEmail());
        if (existingDoctor.isPresent()) {
            throw new DoctorAlreadyExistsException("Doctor with that email already exists");
        }
        Doctor doctor = doctorMapper.fromDTO(newDoctorDTO);
        doctorRepository.save(doctor);
        return doctorMapper.toDTO(doctor);
    }
    @Override
    @Transactional
    public DoctorDTO addFacilityToDoctor(Long doctorId, GetIdCommand entityId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));

        Facility facility = facilityRepository.findById(entityId.getEntityId())
                .orElseThrow(() -> new FacilityNotFoundException("Facility not found"));

        if(doctor.getFacilities().contains(facility)){
            throw new FacilityAlreadyExistsException("Facility already is this doctor employer");
        }

        doctor.getFacilities().add(facility);
        doctorRepository.save(doctor);
        return doctorMapper.toDTO(doctor);
    }

    @Override
    @Transactional
    public AppointmentDTO addAppointment(Long doctorId, Appointment appointment) {
        dataValidator.validateDateTime(appointment.getAppointmentStart(), appointment.getAppointmentEnd());
        List<Appointment> conflictingAppointments = appointmentRepository
                .findConflictingAppointments(doctorId, appointment.getAppointmentStart()
                        , appointment.getAppointmentEnd());
        if(!conflictingAppointments.isEmpty()) {
            throw new InvalidDateTimeException("There is already appointment at this time");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));

        appointment.setDoctor(doctor);
        appointmentRepository.save(appointment);

        return appointmentMapper.toDTO(appointment);
    }

    @Override
    public List<AppointmentDTO> getFreeAppointmentsByDoctor(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findFreeAppointmentsByDoctor(doctorId);
        if(appointments.isEmpty()){
            throw new AppointmentNotFoundException("There are no free appointments for this doctor");
        }
        return appointments.stream().map(appointmentMapper::toDTO).toList();
    }
}