package com.telerikacademy.healthy.food.social.network.controllers.mvc;

import com.telerikacademy.healthy.food.social.network.services.contracts.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final PostsService postsService;

    @Autowired
    public HomeController(PostsService postsService) {
        this.postsService = postsService;
    }

    @GetMapping({"/", "/home"})
    public String showHomePage(final Model model) {
        model.addAttribute("posts", postsService.getTop3Posts());
        return "index";
    }

    @GetMapping("/about")
    public String showAboutUsPage() {
        return "about-us";
    }
}

