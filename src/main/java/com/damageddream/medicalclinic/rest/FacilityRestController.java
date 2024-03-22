package com.damageddream.medicalclinic.rest;

import com.damageddream.medicalclinic.dto.DoctorDTO;
import com.damageddream.medicalclinic.dto.FacilityDTO;
import com.damageddream.medicalclinic.dto.GetIdCommand;
import com.damageddream.medicalclinic.dto.NewFacilityDTO;
import com.damageddream.medicalclinic.service.FacilityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/facilities")
public class FacilityRestController {

    private final FacilityServiceImpl facilityServiceImpl;

    @GetMapping("/{id}")
    public FacilityDTO getFacility(@PathVariable Long id) {
        return facilityServiceImpl.findById(id);
    }

    @GetMapping("/{id}/doctors")
    public List<DoctorDTO> getFacilityDoctors(@PathVariable Long id) {
        return facilityServiceImpl.findDoctorsByFacility(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FacilityDTO addFacility(@RequestBody NewFacilityDTO facility) {
        return facilityServiceImpl.save(facility);
    }

    @PutMapping("/{id}/doctors")
    public FacilityDTO addDoctor(@PathVariable Long id, @RequestBody GetIdCommand entityId) {
        return facilityServiceImpl.addDoctorToFacility(id, entityId);
    }

}
