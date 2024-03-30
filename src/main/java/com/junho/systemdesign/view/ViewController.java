package com.junho.systemdesign.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index() {
        return "redirect:/shortenUrl";
    }

    @GetMapping("/shortenUrl")
    public String shortenUrl() {
        return "shortenUrl";
    }

}
