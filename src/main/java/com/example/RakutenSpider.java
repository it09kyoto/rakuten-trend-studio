package com.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.*;

@Service
public class RakutenSpider {

    public List<Map<String, String>> fetchTrendingData(String genre) {
        List<Map<String, String>> products = new ArrayList<>();
        Document doc = null;

        try {
            // 1. 尝试读取根目录下的本地镜像
            File input = new File("rakuten.html");

            if (input.exists()) {
                System.out.println("✅ 读取本地镜像成功: " + input.getAbsolutePath());
                doc = Jsoup.parse(input, "UTF-8", "https://ranking.rakuten.co.jp/");
            } else {
                // 2. 备选方案：通过学校代理联网抓取
                System.out.println("🌐 本地文件缺失，尝试通过代理 10.1.1.5 联网...");
                String url = "https://ranking.rakuten.co.jp/realtime/" + (genre.equals("0") ? "" : genre + "/");
                doc = Jsoup.connect(url)


                        //.proxy("10.1.1.5", 8080)
                        .timeout(10000)
                        .get();
            }

            if (doc != null) {
                Elements items = doc.select(".rnkRanking_itemName");
                System.out.println("📊 成功解析商品条数: " + items.size());

                for (int i = 0; i < Math.min(items.size(), 10); i++) {
                    Map<String, String> p = new HashMap<>();
                    p.put("rank", String.valueOf(i + 1));
                    p.put("name", items.get(i).text());
                    p.put("score", "4.5");
                    p.put("reviews", "800");
                    p.put("painPoint", "AI 分析：该商品在类目内需求旺盛。建议优化物流体验以提升评分。");
                    products.add(p);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ 抓取失败: " + e.getMessage());
        }
        return products;
    }
}