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

    @Autowired
    private ProductRepository productRepository; // 核心：注入数据库工具

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

        // 1. 获取抓取的数据（Spider 内部已经执行了保存逻辑）
        rakutenSpider.fetchTrendingData(genre);

        // 2. 直接从数据库读取所有已存入的数据传给前端
        // 注意：变量名必须是 "products"
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("lang", lang);

        return "analysis";
    }
}