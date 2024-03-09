package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dao.PatientDAO;
import com.damageddream.medicalclinic.dto.PatientGetDTO;
import com.damageddream.medicalclinic.dto.mapper.Mapper;
import com.damageddream.medicalclinic.dto.mapper.PatientMapper;
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
    private final PatientMapper patientMapper;

    @Override
    public PatientGetDTO save(PatientCreateUpdateDTO patient) {
        var existingPatient = patientDAO.findByEmail(patient.getEmail());
        if (existingPatient.isPresent()) {
            throw new EmailAlreadyExistsException("Patient with that email already exists");
        }
        Patient newPatient = patientMapper.patentCreateUpdateDTOToPatient(patient);
        patientDAO.save(newPatient);
        return patientMapper.patientToPatientGetDTO(newPatient);
    }

    @Override
    public PatientGetDTO findByEmail(String email) {
        Patient patient = patientDAO.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        return patientMapper.patientToPatientGetDTO(patient);
    }

    @Override
    public List<PatientGetDTO> findAll() {
        List<Patient> patients = patientDAO.findAll();
        return patients.stream()
                .map(patientMapper::patientToPatientGetDTO)
                .toList();
    }

    @Override
    public PatientGetDTO update(String email, PatientCreateUpdateDTO patient) {
        var toEdit = patientDAO.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        dataValidator.validateEditPatientData(email, patient, toEdit);
        toEdit.update(patient);
        return patientMapper.patientToPatientGetDTO(toEdit);
    }


    @Override
    public PatientGetDTO delete(String email) {
        var existingPatient = patientDAO.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        patientDAO.delete(existingPatient);
        return patientMapper.patientToPatientGetDTO(existingPatient);
    }

    @Override
    public PatientGetDTO editPassword(ChangePasswordCommand password, String email) {
        var toEdit = patientDAO.findByEmail(email).orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        toEdit.setPassword(password.getPassword());
        return patientMapper.patientToPatientGetDTO(toEdit);
    }

}
