package com.damageddream.medicalclinic.util;

import com.damageddream.medicalclinic.dto.*;
import com.damageddream.medicalclinic.entity.Appointment;
import com.damageddream.medicalclinic.entity.Doctor;
import com.damageddream.medicalclinic.entity.Facility;
import com.damageddream.medicalclinic.entity.Patient;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestDataFactory {

    public static PatientDTO createPatientDTO(String email, String firstName) {
        return PatientDTO.builder()
                .email(email)
                .firstName(firstName)
                .lastName("GrabDTO")
                .phoneNumber("222222")
                .birthday(LocalDate.of(1902, 02, 02))
                .build();
    }

    public static Patient createPatient(String email, String firstName) {
        return Patient
                .builder()
                .id(1L)
                .firstName(firstName)
                .lastName("Grab")
                .email(email)
                .password("password")
                .idCardNo("123456")
                .phoneNumber("678910123")
                .birthday(LocalDate.of(1900, 01, 01))
                .build();
    }

    public static NewPatientDTO createNewPatientDTO(String email, String firstName, String idCardNo) {
        return NewPatientDTO.builder()
                .password("passNewDto")
                .idCardNo(idCardNo)
                .email(email)
                .firstName(firstName)
                .lastName("GrabNewDto")
                .phoneNumber("333333333")
                .birthday(LocalDate.of(1903, 03, 03))
                .build();
    }

    public static Doctor createDoctor(String email, String firstName) {
        return Doctor.builder()
                .id(1L)
                .email(email)
                .password("password")
                .firstName(firstName)
                .lastName("Doctor")
                .specialization("surgeon")
                .facilities(new ArrayList<>())
                .build();
    }
    public static void addFacilitiesToDoctor(Doctor doctor, List<Facility> facilities) {
        doctor.getFacilities().addAll(facilities);
    }

    public static DoctorDTO createDoctorDTO(String email, String firstName) {
        return DoctorDTO.builder()
                .email(email)
                .firstName(firstName)
                .lastName("doctorDTO")
                .specialization("surgeonDTO")
                .build();
    }

    public static NewDoctorDTO createNewDoctorDTO(String email, String firstName) {
        return NewDoctorDTO.builder()
                .email(email)
                .password("passNewDto")
                .firstName(firstName)
                .lastName("NewDoctorDTO")
                .specialization("surgeonNewDto")
                .facilities(new ArrayList<>())
                .build();
    }
    public static void addFacilitiesToNewDoctorDto(NewDoctorDTO doctor, List<Facility> facilities) {
        doctor.getFacilities().addAll(facilities);
    }

    public static Facility createFacility(String name, String city) {
        return Facility.builder()
                .id(1L)
                .name(name)
                .city(city)
                .zipCode("212")
                .street("Hospital avenue")
                .buildingNo("3")
                .doctors(new ArrayList<>())
                .build();
    }
    public static void addDoctorsToFacility(Facility facility, List<Doctor> doctors) {
        facility.getDoctors().addAll(doctors);
    }

    public static FacilityDTO createFacilityDTO(String name, String city) {
        return FacilityDTO.builder()
                .name(name)
                .city(city)
                .zipCode("212Dto")
                .street("Dto avenue ")
                .buildingNo("5")
                .build();
    }

    public static NewFacilityDTO createNewFacilityDTO(String name, String city) {
        return NewFacilityDTO.builder()
                .name(name)
                .city(city)
                .zipCode("212")
                .street("Hospital avenue")
                .buildingNo("3")
                .doctors(new ArrayList<>())
                .build();
    }

    public static void addDoctorsToNewFacilityDTO(NewFacilityDTO facility, List<Doctor> doctors) {
        facility.getDoctors().addAll(doctors);
    }

    public static Appointment createAppointment(LocalDateTime start, LocalDateTime end){
        return Appointment.builder()
                .id(1L)
                .appointmentEnd(end)
                .appointmentStart(start)
                .patient(TestDataFactory
                        .createPatient("mar@mail.com", "Marcin"))
                .doctor(TestDataFactory
                        .createDoctor("doc@mail.com", "Doc"))
                .build();
    }

    public static AppointmentDTO createAppointmentDTO(LocalDateTime start, LocalDateTime end){
        return AppointmentDTO.builder()
                .id(1L)
                .appointmentEnd(end)
                .appointmentStart(start)
                .patient(TestDataFactory
                        .createPatientDTO("mar@mail.com", "Marcin"))
                .doctor(TestDataFactory
                        .createDoctorDTO("doc@mail.com", "Doc"))
                .build();
    }

}
