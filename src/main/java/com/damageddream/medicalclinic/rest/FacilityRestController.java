package com.damageddream.medicalclinic.rest;

import com.damageddream.medicalclinic.entity.Facility;
import com.damageddream.medicalclinic.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/facilities")
public class FacilityRestController {

    private final FacilityService facilityService;

    @GetMapping("/{id}")
    public Facility getFacility(@PathVariable Long id){return facilityService.findById(id);}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Facility addFacility(@RequestBody Facility facility) {
        return facilityService.save(facility);
    }

}
