package com.damageddream.medicalclinic.repository;

import com.damageddream.medicalclinic.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
}
