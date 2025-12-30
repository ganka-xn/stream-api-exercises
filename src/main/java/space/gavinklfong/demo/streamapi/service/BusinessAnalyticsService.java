package space.gavinklfong.demo.streamapi.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.gavinklfong.demo.streamapi.models.Customer;
import space.gavinklfong.demo.streamapi.models.Order;
import space.gavinklfong.demo.streamapi.models.Product;
import space.gavinklfong.demo.streamapi.repos.CustomerRepo;
import space.gavinklfong.demo.streamapi.repos.OrderRepo;
import space.gavinklfong.demo.streamapi.repos.ProductRepo;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BusinessAnalyticsService {
    private final CustomerRepo customerRepo;
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;

    private static final Logger log = LoggerFactory.getLogger(BusinessAnalyticsService.class);

    /**
     * Упражнение 1: Получить список заказов с продуктами категории "Books"
     */
    public List<Order> getOrdersWithProductCategory(String category) {
        return orderRepo.findAll().stream()
                .filter(order -> order.getProducts().stream()
                        .anyMatch(product -> category.equalsIgnoreCase(product.getCategory())))
                .toList();
    }

    /**
     * Упражнение 2: Получить список продуктов, купленных клиентами tier=2
     */
    public List<Product> getProductsByCustomerTier(Integer tier) {
        return customerRepo.findAll().stream()
                .filter(customer -> tier.equals(customer.getTier()))
                .flatMap((customer -> customer.getOrders().stream()))
                .flatMap(order -> order.getProducts().stream())
                .distinct()
                .toList();
    }

    /**
     * Упражнение 3: Получить список недавних заказов (последние n дней)
     */
    public List<Order> getRecentOrders(int days) {
        LocalDate daysAgo = LocalDate.now().minusDays(days);

        return orderRepo.findAll().stream()
                .filter(order ->
                        order.getOrderDate() != null &&
                                order.getOrderDate().isAfter(daysAgo))
                .toList();
    }

    /**
     * Упражнение 4: Получить список продуктов, заказанных в определенную дату
     */
    public Set<Product> getProductsByDate(LocalDate date) {
        return orderRepo.findAll().stream()
                .filter(order -> date.isEqual(order.getOrderDate()))
                .flatMap(order -> order.getProducts().stream())
                .distinct()
                .collect(Collectors.toSet());
    }

    /**
     * Упражнение 5: Получить самую дорогую покупку
     */
    public Optional<Order> getMostExpensiveOrder() {
        return orderRepo.findAll().stream()
                .max(Comparator.comparingDouble(order ->
                                order.getProducts().stream()
                                        .mapToDouble(Product::getPrice)
                                        .sum()
                        )
                );
    }

    /**
     * Получить выручку по дням
     */
    public Map<LocalDate, Double> getRevenue() {
        return orderRepo.findAll().stream()
                .collect(Collectors.groupingBy(
                        Order::getOrderDate,
                        Collectors.summingDouble(order -> order.getProducts().stream()
                                .mapToDouble(Product::getPrice)
                                .sum())
                ));
    }

    /**
     * Получить топ-5 самых популярных категорий
     */
    public Map<String, Long> getMostPopularCategories(int limit) {
        return orderRepo.findAll().stream()
                .flatMap(order -> order.getProducts().stream())
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    /**
     * Получить клиентов, которые купили все продукты категории
     */
    public List<Customer> getCustomersWhoBoughtAnyProductInCategory(String category) {
        List<Product> categoryProducts = productRepo.findAll().stream()
                .filter(product -> category.equalsIgnoreCase(product.getCategory()))
                .toList();

        return customerRepo.findAll().stream()
                .filter(customer -> customer.getOrders() != null)
                .filter(customer -> {
                    Set<Product> boughtProducts = customer.getOrders().stream()
                            .flatMap(order -> order.getProducts().stream())
                            .filter(product -> category.equalsIgnoreCase(product.getCategory()))
                            .collect(Collectors.toSet());
                    return boughtProducts.containsAll(categoryProducts);
                })
                .toList();
    }

    /**
     * Получить отчет по продажам
     */
    public Map<String, Object> getSalesReport() {
        Map<String, Object> report = new HashMap<>();

        // Общая статистика
        report.put("totalOrders", orderRepo.count());
        report.put("totalCustomers", customerRepo.count());
        report.put("totalProducts", productRepo.count());

        // Выручка
        double totalRevenue = orderRepo.findAll().stream()
                .flatMap(order -> order.getProducts().stream())
                .mapToDouble(Product::getPrice)
                .sum();
        report.put("totalRevenue", totalRevenue);

        // Средний чек
        double averageOrderValue = orderRepo.findAll().stream()
                .mapToDouble(order -> order.getProducts().stream()
                        .mapToDouble(Product::getPrice)
                        .sum())
                .average()
                .orElse(0.0);
        report.put("averageOrderValue", averageOrderValue);

        // Статистика по статусам заказов
        Map<String, Long> ordersByStatus = orderRepo.findAll().stream()
                .collect(Collectors.groupingBy(
                        Order::getStatus,
                        Collectors.counting()
                ));
        report.put("ordersByStatus", ordersByStatus);

        // Топ категорий
        report.put("mostPopularCategories", getMostPopularCategories(5));

        return report;
    }


}
