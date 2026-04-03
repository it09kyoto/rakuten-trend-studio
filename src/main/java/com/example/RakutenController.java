package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.*;

@Controller
public class RakutenController {

    @Autowired
    private RakutenSpider rakutenSpider;

    @GetMapping("/")
    public String index(@RequestParam(name = "lang", defaultValue = "cn") String lang, Model model) {
        model.addAttribute("lang", lang);
        return "index";
    }

    @GetMapping("/analysis")
    public String analysis(
            @RequestParam(name = "genre", defaultValue = "0") String genre,
            @RequestParam(name = "lang", defaultValue = "cn") String lang,
            Model model) {

        List<Map<String, String>> products = rakutenSpider.fetchTrendingData(genre);
        model.addAttribute("productList", products);
        model.addAttribute("lang", lang);
        return "analysis";
    }
}