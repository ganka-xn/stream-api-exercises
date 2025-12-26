package space.gavinklfong.demo.streamapi.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.gavinklfong.demo.streamapi.models.Product;
import space.gavinklfong.demo.streamapi.repos.ProductRepo;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
     * Упражнение 2: Получить список товаров категории "Baby"
     */
    public List<Product> getProductsByCategory(String category) {
        return productRepo.findAll().stream()
                .filter(product -> category.equalsIgnoreCase(product.getCategory()))
                .toList();
    }

    /**
     * Упражнение 3: Получить список товаров категории "Toys" со скидкой 10%
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
     * Obtain a data map with list of product name by category
     */

    /**
     * Получить статистику по ценам в категории
     * Obtain a collection of statistic figures (i.e. sum, average, max, min, count) for all products of category
     */

    /**
     * Поиск товаров по названию (частичное совпадение)
     */

    /**
     * Получить товары в ценовом диапазоне
     */

    /**
     * Получить количество товаров в каждой категории
     */
}
