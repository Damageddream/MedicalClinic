package com.damageddream.medicalclinic.dto.mapper;

import com.damageddream.medicalclinic.dto.AppointmentDTO;

import com.damageddream.medicalclinic.entity.Appointment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



//@Mapper(componentModel = "spring", uses = {DoctorMapper.class, PatientMapper.class})
public interface AppointmentMapper {
    //@Mapping(target = "patient", source = "appointment.patient", qualifiedByName = "toPatientDto")

//    @Mapping(target = "patient", source = "appointment.patient")
//    @Mapping(target = "doctor", source = "appointment.doctor")
//    AppointmentDTO toDTO(Appointment appointment);
//
//    @Mapping(target = "patient", source = "appointmentDTO.patient")
//    @Mapping(target = "doctor", source = "appointmentDTO.doctor")
//    Appointment fromDTO(AppointmentDTO appointmentDTO);

//    @Named("toPatientDto")
//    default PatientDTO toPatientDto(Patient patient) {
//        if(patient == null){
//            return null;
//        }
//        return PatientDTO.builder()
//                .email(patient.getEmail())
//                .firstName(patient.getFirstName())
//                .lastName(patient.getLastName())
//                .phoneNumber(patient.getPhoneNumber())
//                .birthday(patient.getBirthday())
//                .build();
//    }
}
