package com.damageddream.medicalclinic.repository;

import com.damageddream.medicalclinic.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

}
