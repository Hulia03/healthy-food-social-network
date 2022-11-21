package com.telerikacademy.healthy.food.social.network.controllers.rest;

import com.telerikacademy.healthy.food.social.network.models.Nationality;
import com.telerikacademy.healthy.food.social.network.services.contracts.NationalitiesService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.*;

@RestController
@RequestMapping(API_URL + ADMIN_URL + NATIONALITY_URL)
public class NationalitiesRestController {
    private final NationalitiesService nationalitiesService;

    @Autowired
    public NationalitiesRestController(NationalitiesService nationalitiesService) {
        this.nationalitiesService = nationalitiesService;
    }

    @GetMapping
    @ApiOperation(value = "List of all nationalities", response = Collection.class)
    public Collection<Nationality> getNationalities() {
        return nationalitiesService.getAll();
    }

    @PostMapping
    @ApiOperation(value = "Create nationality", response = Nationality.class)
    public Nationality createNationality(@RequestBody @Valid Nationality nationality) {
        return nationalitiesService.createNationality(nationality);
    }

    @PutMapping
    @ApiOperation(value = "Update nationality", response = Nationality.class)
    public Nationality updateNationality(@RequestBody @Valid Nationality nationality) {
        return nationalitiesService.updateNationality(nationality);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete nationality", notes = "Return list of all nationalities", response = Collection.class)
    public Collection<Nationality> deleteNationality(@PathVariable int id) {
        nationalitiesService.deleteNationality(id);
        return nationalitiesService.getAll();
    }
}
