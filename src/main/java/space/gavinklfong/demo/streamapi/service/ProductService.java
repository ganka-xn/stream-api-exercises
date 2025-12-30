package space.gavinklfong.demo.streamapi.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.gavinklfong.demo.streamapi.models.Product;
import space.gavinklfong.demo.streamapi.repos.ProductRepo;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepo productRepo;
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    /**
     * Упражнение 1: Получить список товаров категории "Books" с ценой > 100
     */
    public List<Product> getProductsByCategoryWithPriceLimit(String category, Double priceLimit) {
        return productRepo.findAll().stream()
                .filter(product -> category.equalsIgnoreCase(product.getCategory()))
                .filter(product -> product.getPrice() > priceLimit)
                .toList();
    }

    /**
     * Упражнение 2: Получить список товаров по категории
     */
    public List<Product> getProductsByCategory(String category) {
        return productRepo.findAll().stream()
                .filter(product -> category.equalsIgnoreCase(product.getCategory()))
                .toList();
    }

    /**
     * Obtain a list of product with category and then apply 10% discount
     */
    public List<Product> getProductsByCategoryWithDiscount(String category, Double discount) {
        return productRepo.findAll().stream()
                .filter(product -> category.equalsIgnoreCase(product.getCategory()))
                .map(product -> new Product(
                        product.getId(),
                        product.getName(),
                        product.getCategory(),
                        product.getPrice() * (1 - discount)))
                .toList();
    }

    /**
     * Получить самый дешевый товар категории "Books"
     */
    public Optional<Product> getCheapestProductByCategory(String category) {
        return productRepo.findAll().stream()
                .filter(product -> category.equalsIgnoreCase(product.getCategory()))
                .min(Comparator.comparingDouble(Product::getPrice));
    }

    /**
     * Get the most expensive product by category
     */
    public Optional<Product> getMostExpensiveProductByCategory(String category) {
        return productRepo.findAll().stream()
                .filter(product -> category.equalsIgnoreCase(product.getCategory()))
                .max(Comparator.comparingDouble(Product::getPrice));
    }

    /**
     * Get map of the most expensive product by category
     */
    public Map<String, Optional<Product>> getMostExpensiveProductsByCategories() {
        return productRepo.findAll().stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.maxBy(Comparator.comparing(Product::getPrice))
                ));
    }

    /**
     * Получить 3 последних добавленных товара
     */
    public List<Product> getRecentProducts(int size) {
        return productRepo.findAll().stream()
                .sorted(Comparator.comparingLong(Product::getId).reversed())
//                .sorted((p1, p2) -> p2.getId().compareTo(p1.getId()))
                .limit(size)
                .toList();
    }

//    Если метод findAll() возвращает много данных, можно оптимизировать:
//    public List<Product> getRecentProducts(int size) {
//        return productRepo.findAll(Sort.by(Sort.Direction.DESC, "id"))
//                .stream()
//                .limit(size)
//                .toList();
//    }
//    Это позволит выполнить сортировку на уровне базы данных,
//    что будет эффективнее при большом количестве записей (если используется Spring Data JPA).

    /**
     * Получить общую стоимость товаров категории "Books" на складе
     */
    public Double getSumByCategory(String category) {
        return productRepo.findAll().stream()
                .filter(product -> category.equalsIgnoreCase(product.getCategory()))
                .mapToDouble(Product::getPrice)
                .sum();
    }

    /**
     * Сгруппировать товары по категориям
     */
    public Map<String, List<Product>> getGroupByCategory() {
        return productRepo.findAll().stream()
                .collect(Collectors.groupingBy(Product::getCategory));
    }

    /**
     * Получить статистику по ценам товаров в категории
     * <p>
     * Obtain a collection of statistic figures (i.e. sum, average, max, min, count) for all products of category
     */
    public Map<String, Double> getPriceStatsInCategory(String category) {
        DoubleSummaryStatistics stat = productRepo.findAll().stream()
                .filter(product -> category.equalsIgnoreCase(product.getCategory()))
                .mapToDouble(Product::getPrice)
                .summaryStatistics();
        return Map.of(
                "sum", stat.getSum(),
                "count", (double) stat.getCount(),
                "min", stat.getMin(),
                "max", stat.getMax(),
                "average", stat.getAverage()
        );
//        List<Product> products = productRepo.findAll().stream()
//                .filter(product -> category.equalsIgnoreCase(product.getCategory()))
//                .toList();
//
//        Map<String, Double> stat = new HashMap<>();
//        stat.put("sum", products.stream().mapToDouble(Product::getPrice).sum());
//        stat.put("average", products.stream().mapToDouble(Product::getPrice).average().orElse(0));
//        stat.put("max", products.stream().mapToDouble(Product::getPrice).max().orElse(0));
//        stat.put("min", products.stream().mapToDouble(Product::getPrice).min().orElse(0));
//        stat.put("count", (double) products.size());
//
//        return stat;
    }

    /**
     * Поиск товаров по названию (частичное совпадение)
     */
    public List<Product> searchProductsByName(String name) {
        return productRepo.findAll().stream()
                .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    /**
     * Получить товары в ценовом диапазоне
     */
    public List<Product> getProductsInPriceRange(double minPrice, double maxPrice) {
        return productRepo.findAll().stream()
                .filter(product -> product.getPrice() <= maxPrice && product.getPrice() >= minPrice)
                .toList();
    }

    /**
     * Получить список всех категорий
     */
    public List<String> getProductsCategories() {
        return productRepo.findAll().stream()
                .map(Product::getCategory)
                .distinct().toList();
    }

    /**
     * Получить количество товаров в каждой категории
     */
    public Map<String, Long> countProductsByCategory() {
        return productRepo.findAll().stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));
    }

}
