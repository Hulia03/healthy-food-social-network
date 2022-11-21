package com.telerikacademy.healthy.food.social.network.controllers.mvc;

import com.telerikacademy.healthy.food.social.network.models.Nationality;
import com.telerikacademy.healthy.food.social.network.models.dtos.nationalities.NationalityDto;
import com.telerikacademy.healthy.food.social.network.services.contracts.NationalitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.ADMIN_URL;
import static com.telerikacademy.healthy.food.social.network.utils.GlobalConstants.NATIONALITY_URL;

@Controller
@RequestMapping(ADMIN_URL + NATIONALITY_URL)
public class NationalitiesMvcController {
    private final NationalitiesService nationalitiesService;

    @Autowired
    public NationalitiesMvcController(NationalitiesService nationalitiesService) {
        this.nationalitiesService = nationalitiesService;
    }

    @GetMapping
    public String showNationalities(final Model model) {
        model.addAttribute("nationalities", nationalitiesService.getAll());
        return "admin/nationalities/nationalities";
    }

    @GetMapping("/new")
    public String showNewNationalityForm(final Model model) {
        model.addAttribute("nationality", new NationalityDto());
        return "admin/nationalities/create-nationality";
    }

    @PostMapping("/new")
    public String createNationality(@Valid @ModelAttribute("nationality") NationalityDto nationality,
                                    BindingResult errors) {
        if (errors.hasErrors()) {
            return "admin/nationalities/create-nationality";
        }

        Nationality nationalityNew = new Nationality();
        nationalityNew.setNationality(nationality.getNationality());
        nationalitiesService.createNationality(nationalityNew);
        return "redirect:/admin/nationalities";
    }

    @GetMapping("/{id}/update")
    public String showUpdateNationalityForm(@PathVariable int id, final Model model) {
        Nationality nationality = nationalitiesService.getNationalityById(id);
        NationalityDto dto = new NationalityDto();
        dto.setNationality(nationality.getNationality());
        model.addAttribute("nationality", dto);
        return "admin/nationalities/update-nationality";
    }

    @PostMapping("/{id}/update")
    public String updateNationality(@PathVariable int id,
                                    @Valid @ModelAttribute("nationality") NationalityDto nationality,
                                    BindingResult errors) {
        if (errors.hasErrors()) {
            return "admin/nationalities/update-nationality";
        }

        Nationality oldNationality = nationalitiesService.getNationalityById(id);
        oldNationality.setNationality(nationality.getNationality());
        nationalitiesService.updateNationality(oldNationality);
        return "redirect:/admin/nationalities";
    }

    @GetMapping("/{id}/delete")
    public String showDeleteNationalityForm(@PathVariable int id, final Model model) {
        Nationality nationality = nationalitiesService.getNationalityById(id);
        model.addAttribute("nationality", nationality);
        return "admin/nationalities/delete-nationality";
    }

    @PostMapping("/{id}/delete")
    public String deleteNationality(@PathVariable int id) {
        nationalitiesService.deleteNationality(id);
        return "redirect:/admin/nationalities";
    }

}
