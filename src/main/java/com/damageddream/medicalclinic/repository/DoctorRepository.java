package com.damageddream.medicalclinic.repository;

import com.damageddream.medicalclinic.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
