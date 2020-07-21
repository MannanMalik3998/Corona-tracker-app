package io.javabrains.CoronaVirusAppSpringBoot.controllers;

import io.javabrains.CoronaVirusAppSpringBoot.models.locationStatistics;
import io.javabrains.CoronaVirusAppSpringBoot.services.CoronaVirusDataFetch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired //Service needs to be autowired
    CoronaVirusDataFetch coronaVirusService;

    @GetMapping("/") // Maps the following function to home page
    public String home(Model model){

        List<locationStatistics> allStats = coronaVirusService.getAllStatistics();
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getTotalCases()).sum();
        //Models are used to pass information to be displayed on web page
        model.addAttribute("locationStatistics", allStats);
        //Sum of daily global cases
        model.addAttribute("totalReportedCases", totalReportedCases);

        return "home";
    }
}
