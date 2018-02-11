package com.xsh.util.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "export")
public class ExportController {

    @RequestMapping("/index")
    public String export(Model model) {
        return "export/export";
    }
}
