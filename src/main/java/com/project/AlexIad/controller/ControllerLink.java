package com.project.AlexIad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ControllerLink {

    @GetMapping("/list")
    public String shoList(){
//            (
//                    @RequestParam(name = "name", required = false, defaultValue = "user") String name, Model model) {
//        model.addAttribute("name", name);
        return "shopi";
    }

    @GetMapping("/listAl")
    public String alarmList(
            @RequestParam(name = "name", required = false, defaultValue = "user") String name, Model model) {
        model.addAttribute("name", name);
        return "alarms";
    }

    @GetMapping("/products")
    public String info(
            @RequestParam(name = "name", required = false, defaultValue = "user") String name, Model model) {
        model.addAttribute("name", name);
        return "products";
    }

}
