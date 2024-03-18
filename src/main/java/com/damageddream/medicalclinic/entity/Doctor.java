package com.damageddream.medicalclinic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
    public class Doctor {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(nullable = false)
        private Long id;
        @Column(unique = true)
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private String specialization;
        @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinTable(
                name = "doctor_facility",
                joinColumns = @JoinColumn(name = "doctor_id"),
                inverseJoinColumns = @JoinColumn(name = "facility_id")
        )
        private List<Facility> facilities;
    }
