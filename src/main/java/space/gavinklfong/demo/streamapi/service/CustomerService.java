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

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {
    private final CustomerRepo customerRepo;
    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    /**
     * Получить клиентов определенного tier
     */
    public List<Customer> getCustomersByTier(Integer tier) {
        return customerRepo.findAll().stream()
                .filter(c -> tier.equals(c.getTier()))
                .collect(Collectors.toList());
    }

    /**
     * Получить клиентов по имени (поиск)
     */
    public List<Customer> getCustomerByName(String name) {
        return customerRepo.findAll().stream()
                .filter(c -> c.getName().toLowerCase().contains(name))
                .collect(Collectors.toList());
    }

    /**
     * Получить клиентов с заказами
     */
    public List<Customer> getCustomersWithOrders() {
        return customerRepo.findAll().stream()
                .filter(c -> c.getOrders() != null && !c.getOrders().isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Получить клиентов без заказов
     */
    public List<Customer> getCustomersWithoutOrders() {
        return customerRepo.findAll().stream()
                .filter(c -> c.getOrders() == null || c.getOrders().isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Получить клиента с наибольшим количеством заказов
     */
    public Optional<Customer> getCustomerWithMostOrders() {
        return customerRepo.findAll().stream()
                .filter(c -> c.getOrders() != null)
                .max(Comparator.comparingInt(c -> c.getOrders().size()));
    }

    /**
     * Получить клиентов, которые заказывали продукты определенной категории
     */
    public List<Customer> getCustomersWhoOrderedCategory(String category) {
        return customerRepo.findAll().stream()
                .filter(c -> c.getOrders() != null)
                .filter(c -> c.getOrders().stream()
                        .anyMatch(o -> o.getProducts().stream()
                                .anyMatch(p -> category.equals(p.getCategory()))))
                .collect(Collectors.toList());
    }

    /**
     * Получить общую сумму покупок для каждого клиента
     */
    public Map<Customer, Double> getCustomerTotalSpent() {
        return customerRepo.findAll().stream()
                .filter(c -> c.getOrders() != null && !c.getOrders().isEmpty())
                .collect(Collectors.toMap(
                        Function.identity(),
                        customer -> customer.getOrders().stream()
                                .flatMap(order -> order.getProducts().stream())
                                .mapToDouble(Product::getPrice)
                                .sum())
                );
    }

    /**
     * Получить клиента с наибольшей общей суммой покупок
     */
    public Optional<Customer> getTopSpendingCustomer() {
        return customerRepo.findAll().stream()
                .filter(c -> c.getOrders() != null && !c.getOrders().isEmpty())
                .max(Comparator.comparingDouble(
                        c -> c.getOrders().stream()
                                .flatMap(order -> order.getProducts().stream())
                                .mapToDouble(Product::getPrice)
                                .sum())
                );
    }

    /**
     * Получить статистику по клиентам
     */
    public Map<String, Object> getCustomerStatistics() {
        List<Customer> customers = customerRepo.findAll();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCustomers", customers.size());
        stats.put("customersWithOrders", getCustomersWithOrders().size());
        stats.put("customersWithoutOrders", getCustomersWithoutOrders().size());

        // Распределение по tier
        Map<Integer, Long> tierDistribution = customers.stream()
                .collect(Collectors.groupingBy(Customer::getTier, Collectors.counting()));
        stats.put("tierDistribution", tierDistribution);

        return stats;
    }

    /**
     * Получить клиентов, которые делали заказы в определенный период
     */
    public List<Customer> getCustomersWithOrdersBetween(LocalDate startDate, LocalDate endDate) {
        return customerRepo.findAll().stream()
                .filter(customer -> customer.getOrders() != null && !customer.getOrders().isEmpty())
                .filter(customer -> customer.getOrders().stream()
                        .anyMatch(
                                order -> !order.getOrderDate().isAfter(endDate) &&
                                        !order.getOrderDate().isBefore(startDate)
                        ))
                .collect(Collectors.toList());
    }

    /**
     * Получить последних активных клиентов
     */
    public List<Customer> getRecentlyActiveCustomers(int limit) {
        return customerRepo.findAll().stream()
                .filter(customer -> customer.getOrders() != null && !customer.getOrders().isEmpty())
                .sorted((c1, c2) -> {
                    LocalDate lastOrder1 = c1.getOrders().stream()
                            .map(Order::getOrderDate)
                            .max(LocalDate::compareTo)
                            .orElse(LocalDate.MIN);
                    LocalDate lastOrder2 = c2.getOrders().stream()
                            .map(Order::getOrderDate)
                            .max(LocalDate::compareTo)
                            .orElse(LocalDate.MIN);
                    return lastOrder2.compareTo(lastOrder1);
                })
                .limit(limit)
                .collect(Collectors.toList());
    }
}
