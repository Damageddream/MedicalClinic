package com.damageddream.medicalclinic.validation;

import com.damageddream.medicalclinic.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public class DataValidatorTest {
    private DataValidator dataValidator;
    private PatientRepository patientRepository;

    @BeforeEach
    void setup() {
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.dataValidator = new DataValidator(patientRepository);
    }
}
