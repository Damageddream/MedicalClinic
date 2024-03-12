package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.NewPatientDTO;
import com.damageddream.medicalclinic.dto.PatientDTO;
import com.damageddream.medicalclinic.dto.mapper.PatientMapper;
import com.damageddream.medicalclinic.dto.ChangePasswordCommand;
import com.damageddream.medicalclinic.entity.Patient;
import com.damageddream.medicalclinic.exception.EmailAlreadyExistsException;
import com.damageddream.medicalclinic.exception.PatientNotFoundException;
import com.damageddream.medicalclinic.repository.PatientRepository;
import com.damageddream.medicalclinic.validation.DataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final DataValidator dataValidator;
    private final PatientMapper patientMapper;
    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public PatientDTO save(NewPatientDTO patient) {
        var existingPatient = patientRepository.findByEmail(patient.getEmail());
        if (existingPatient.isPresent()) {
            throw new EmailAlreadyExistsException("Patient with that email already exists");
        }
        Patient newPatient = patientMapper.fromPatientDTO(patient);
        patientRepository.save(newPatient);
        return patientMapper.fromPatient(newPatient);
    }

    @Override
    public PatientDTO findByEmail(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        return patientMapper.fromPatient(patient);
    }

    @Override
    public List<PatientDTO> findAll() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(patientMapper::fromPatient)
                .toList();
    }

    @Override
    @Transactional
    public PatientDTO update(String email, NewPatientDTO patient) {
        var toEdit = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        dataValidator.validateEditPatientData(email, patient, toEdit);
        patientMapper.updatePatientFromDTO(patient, toEdit);
        patientRepository.save(toEdit);
        return patientMapper.fromPatient(toEdit);
    }


    @Override
    @Transactional
    public PatientDTO delete(String email) {
        var existingPatient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        patientRepository.delete(existingPatient);
        return patientMapper.fromPatient(existingPatient);
    }

    @Override
    @Transactional
    public PatientDTO editPassword(ChangePasswordCommand password, String email) {
        var toEdit = patientRepository.findByEmail(email).orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        toEdit.setPassword(password.getPassword());
        patientRepository.save(toEdit);
        return patientMapper.fromPatient(toEdit);
    }

}
