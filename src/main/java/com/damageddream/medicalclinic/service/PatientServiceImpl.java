package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.PatientGetDTO;
import com.damageddream.medicalclinic.dto.mapper.PatientMapper;
import com.damageddream.medicalclinic.entity.ChangePasswordCommand;
import com.damageddream.medicalclinic.entity.Patient;
import com.damageddream.medicalclinic.dto.PatientCreateUpdateDTO;
import com.damageddream.medicalclinic.exception.EmailAlreadyExistsException;
import com.damageddream.medicalclinic.exception.PatientNotFoundException;
import com.damageddream.medicalclinic.repository.PatientRepository;
import com.damageddream.medicalclinic.validation.DataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    //clean nazwy
    private final DataValidator dataValidator;
    private final PatientMapper patientMapper;
    private final PatientRepository patientRepository;

    @Override
    public PatientGetDTO save(PatientCreateUpdateDTO patient) {
        var existingPatient = patientRepository.findByEmail(patient.getEmail());
        if (existingPatient.isPresent()) {
            throw new EmailAlreadyExistsException("Patient with that email already exists");
        }
        Patient newPatient = patientMapper.patentCreateUpdateDTOToPatient(patient);
        patientRepository.save(newPatient);
        return patientMapper.patientToPatientGetDTO(newPatient);
    }

    @Override
    public PatientGetDTO findByEmail(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        return patientMapper.patientToPatientGetDTO(patient);
    }

    @Override
    public List<PatientGetDTO> findAll() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(patientMapper::patientToPatientGetDTO)
                .toList();
    }

    @Override
    public PatientGetDTO update(String email, PatientCreateUpdateDTO patient) {
        var toEdit = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        dataValidator.validateEditPatientData(email, patient, toEdit);
        toEdit.update(patient);
        return patientMapper.patientToPatientGetDTO(toEdit);
    }


    @Override
    public PatientGetDTO delete(String email) {
        var existingPatient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        patientRepository.delete(existingPatient);
        return patientMapper.patientToPatientGetDTO(existingPatient);
    }

    @Override
    public PatientGetDTO editPassword(ChangePasswordCommand password, String email) {
        var toEdit = patientRepository.findByEmail(email).orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        toEdit.setPassword(password.getPassword());
        return patientMapper.patientToPatientGetDTO(toEdit);
    }

}
