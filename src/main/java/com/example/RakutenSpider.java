package com.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.*;

@Service
public class RakutenSpider {

    // 注入数据库操作接口，用于执行保存逻辑
    @Autowired
    private ProductRepository productRepository;

    public List<Map<String, String>> fetchTrendingData(String genre) {
        List<Map<String, String>> products = new ArrayList<>();
        Document doc = null;

        try {
            // 1. 优先尝试读取根目录下的本地镜像文件
            File input = new File("rakuten.html");

            if (input.exists()) {
                System.out.println("✅ 读取本地镜像成功: " + input.getAbsolutePath());
                doc = Jsoup.parse(input, "UTF-8", "https://ranking.rakuten.co.jp/");
            } else {
                // 2. 备选方案：联网抓取
                System.out.println("🌐 本地文件缺失，尝试联网抓取...");
                String url = "https://ranking.rakuten.co.jp/realtime/" + (genre.equals("0") ? "" : genre + "/");
                doc = Jsoup.connect(url)
                        .timeout(10000)
                        .get();
            }

            if (doc != null) {
                // 使用 JSoup 选择器解析商品名称
                Elements items = doc.select(".rnkRanking_itemName");
                System.out.println("📊 成功解析商品条数: " + items.size());

                // 循环处理前 10 条数据并存入数据库
                for (int i = 0; i < Math.min(items.size(), 10); i++) {
                    String productName = items.get(i).text();

                    // --- 核心修改：封装实体类并保存到 SQLite ---
                    Product product = new Product();
                    product.setName(productName);
                    product.setPrice("取得中");
                    // 这里可以根据业务逻辑设置 AI 预测内容
                    product.setAiPrediction("AI 分析：该商品在类目内需求旺盛。建议优化物流体验以提升评分。");

                    // 调用 Repository 的 save 方法，JPA 会自动处理 SQL 插入
                    productRepository.save(product);
                    System.out.println("💾 已成功存入数据库: " + productName);

                    // --- 构造返回给前端展示的 List ---
                    Map<String, String> p = new HashMap<>();
                    p.put("rank", String.valueOf(i + 1));
                    p.put("name", productName);
                    p.put("score", "4.5");
                    p.put("reviews", "800");
                    p.put("painPoint", product.getAiPrediction());
                    products.add(p);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ 抓取或数据入库失败: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }
}