package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dao.PatientDAO;
import com.damageddream.medicalclinic.dto.PatientGetDTO;
import com.damageddream.medicalclinic.dto.mapper.Mapper;
import com.damageddream.medicalclinic.entity.ChangePasswordCommand;
import com.damageddream.medicalclinic.entity.Patient;
import com.damageddream.medicalclinic.dto.PatientCreateUpdateDTO;
import com.damageddream.medicalclinic.exception.EmailAlreadyExistsException;
import com.damageddream.medicalclinic.exception.PatientNotFoundException;
import com.damageddream.medicalclinic.validation.DataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientDAO patientDAO;
    private final DataValidator dataValidator;
    private final Mapper mapper;

    @Override
    public PatientGetDTO save(PatientCreateUpdateDTO patient) {
        var existingPatient = patientDAO.findByEmail(patient.getEmail());
        if (existingPatient.isPresent()) {
            throw new EmailAlreadyExistsException("Patient with that email already exists");
        }
        Patient newPatient = mapper.fromDTOcreate(patient);
        patientDAO.save(newPatient);
        return mapper.toDTOget(newPatient);
    }

    @Override
    public PatientGetDTO findByEmail(String email) {
        Patient patient = patientDAO.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        return mapper.toDTOget(patient);
    }

    @Override
    public List<PatientGetDTO> findAll() {

        List<Patient> patients = patientDAO.findAll();
        return patients.stream()
                .map(mapper::toDTOget)
                .toList();
    }

    @Override
    public PatientGetDTO update(String email, PatientCreateUpdateDTO patient) {
        var toEdit = patientDAO.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        dataValidator.validateEditPatientData(email, patient, toEdit);
        toEdit.update(patient);
        return mapper.toDTOget(toEdit);
    }


    @Override
    public PatientGetDTO delete(String email) {
        var existingPatient = patientDAO.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        patientDAO.delete(existingPatient);
        return mapper.toDTOget(existingPatient);
    }

    @Override
    public PatientGetDTO editPassword(ChangePasswordCommand password, String email) {
        var toEdit = patientDAO.findByEmail(email).orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        toEdit.setPassword(password.getPassword());
        return mapper.toDTOget(toEdit);
    }

}
