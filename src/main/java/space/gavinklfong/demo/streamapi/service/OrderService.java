package space.gavinklfong.demo.streamapi.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.gavinklfong.demo.streamapi.models.Customer;
import space.gavinklfong.demo.streamapi.models.Order;
import space.gavinklfong.demo.streamapi.models.Product;
import space.gavinklfong.demo.streamapi.repos.OrderRepo;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepo orderRepo;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    /**
     * Упражнение 4: Получить список заказов с продуктами по категории
     */
    public List<Order> getOrdersByCategory(String category) {
        return orderRepo.findAll().stream()
                .filter(order -> order.getProducts() != null && !order.getProducts().isEmpty())
                .filter(order -> order.getProducts().stream()
                        .anyMatch(product -> category.equalsIgnoreCase(product.getCategory())))
                .toList();
    }

    /**
     * Получить заказы за определенную дату
     */
    public List<Order> getOrdersByDate(LocalDate date) {
        return orderRepo.findAll().stream()
                .filter(order -> date.equals(order.getOrderDate()))
                .toList();
    }

    /**
     * Get a list of orders which were ordered on 15-Mar-2021,
     * log the order records to the console and
     * then return its product list
     */
    public List<Product> getProductsOrderedByDate(LocalDate date) {
        return orderRepo.findAll().stream()
                .filter(order -> date.isEqual(order.getOrderDate()))
                .peek(System.out::println)
                .flatMap(order -> order.getProducts().stream())
                .distinct()
                .toList();
    }

    /**
     * Get the most recent placed order with limit
     */
    public List<Order> getRecentOrders(int limit) {
        return orderRepo.findAll().stream()
//                .sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()))
                .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                .limit(limit)
                .toList();
    }

    /**
     * Получить заказы за период
     */
    public List<Order> getOrdersByPeriod(LocalDate start, LocalDate end) {
        return orderRepo.findAll().stream()
                .filter(order -> !order.getOrderDate().isAfter(end))
                .filter(order -> !order.getOrderDate().isBefore(start))
                .toList();
    }

    /**
     * Получить заказы по статусу
     */
    public List<Order> getOrdersByState(String status) {
        return orderRepo.findAll().stream()
                .filter(order -> status.equalsIgnoreCase(order.getStatus()))
                .toList();
    }

    /**
     * Получить заказы конкретного клиента
     */
    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepo.findAll().stream()
                .filter(order -> customerId.equals(order.getCustomer().getId()))
                .toList();
    }

    /**
     * Obtain a list of products ordered by customer of tier 2
     * between 01-Feb-2021 and 01-Apr-2021
     */
    public List<Product> getProductsByCustomerBetweenDates(Long customerId, LocalDate start, LocalDate end) {
        return orderRepo.findAll().stream()
                .filter(order -> customerId.equals(order.getCustomer().getId()))
                .filter(order -> !order.getOrderDate().isBefore(start))
                .filter(order -> !order.getOrderDate().isAfter(end))
                .flatMap(order -> order.getProducts().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Produce a data map with order records grouped by customer
     */
    public Map<Customer, List<Order>> getOrdersByCustomer() {
        return orderRepo.findAll().stream()
                .collect(Collectors.groupingBy(Order::getCustomer));
    }

    /**
     * Получить суммарную стоимость каждого заказа
     * <p>
     * Produce a data map with order record and product total sum
     */
    public Map<Order, Double> getOrdersWithSum() {
        return orderRepo.findAll().stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        order -> order.getProducts().stream()
                                .mapToDouble(Product::getPrice)
                                .sum()
                ));
    }

    /**
     * Получить заказы с максимальной стоимостью
     */
    public List<Order> getMostExpensiveOrders(int limit) {
        return orderRepo.findAll().stream()
                .sorted((o1, o2) -> {
                    double total1 = o1.getProducts().stream()
                            .mapToDouble(Product::getPrice)
                            .sum();
                    double total2 = o2.getProducts().stream()
                            .mapToDouble(Product::getPrice)
                            .sum();
                    return Double.compare(total2, total1);
                })
                .limit(limit)
                .toList();
    }

    /**
     * Получить заказы с минимальной стоимостью
     */
    public List<Order> getCheapestOrders(int limit) {
        return orderRepo.findAll().stream()
                .sorted((o1, o2) -> {
                    double total1 = o1.getProducts().stream()
                            .mapToDouble(Product::getPrice)
                            .sum();
                    double total2 = o2.getProducts().stream()
                            .mapToDouble(Product::getPrice)
                            .sum();

                    return Double.compare(total1, total2);
                })
                .limit(limit)
                .toList();
    }

    /**
     * Получить среднюю стоимость заказа
     */
    public Double getAverageOrderPrice() {
        return orderRepo.findAll().stream()
                .mapToDouble(order -> order.getProducts().stream()
                        .mapToDouble(Product::getPrice)
                        .sum())
                .average()
                .orElse(0);
    }

    /**
     * Получить среднюю стоимость заказа в определенную дату
     */
    public Double getAverageOrderPriceOnDate(LocalDate date) {
        return orderRepo.findAll().stream()
                .filter(order -> date.isEqual(order.getOrderDate()))
                .mapToDouble(order -> order.getProducts().stream()
                        .mapToDouble(Product::getPrice)
                        .sum())
                .average()
                .orElse(0);
    }

    /**
     * Получить продукты, которые заказывались чаще всего
     */
    public Map<Product, Long> getMostOrderedProducts(int limit) {
        return orderRepo.findAll().stream()
                .flatMap(order -> order.getProducts().stream())
                .collect(Collectors.groupingBy(
                        product -> product,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Получить даты с наибольшим количеством заказов
     */
    public Map<LocalDate, Long> getOrdersByDate() {
        return orderRepo.findAll().stream()
                .collect(Collectors.groupingBy(
                        Order::getOrderDate,
                        Collectors.counting()
                ));
    }

    /**
     * Calculate total lump sum of all orders placed in Feb 2021
     */
    public Double getSumByMonth(YearMonth yearMonth) {
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        return orderRepo.findAll().stream()
                .filter(order ->
                        !order.getOrderDate().isBefore(start) &&
                                !order.getOrderDate().isAfter(end))
                .flatMap(order -> order.getProducts().stream())
                .mapToDouble(Product::getPrice)
                .sum();
    }

    /**
     * Obtain a data map with order id and order’s product count
     */
    public Map<Long, Integer> getOrdersCount() {
        return orderRepo.findAll().stream()
                .collect(Collectors.toMap(
                        Order::getId,
                        order -> order.getProducts().size()
                ));
    }
}
