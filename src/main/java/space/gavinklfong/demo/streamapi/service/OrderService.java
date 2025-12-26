package space.gavinklfong.demo.streamapi.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.gavinklfong.demo.streamapi.models.Order;
import space.gavinklfong.demo.streamapi.repos.OrderRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepo orderRepo;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    /**
     * Упражнение 4: Получить список заказов с продуктами категории "Baby"
     */

    /**
     * Упражнение 5: Получить список заказов с продуктами категории "Toys"
     */

    /**
     * Получить заказы за определенную дату
     */

    /**
     * Get the 3 most recent placed order
     */

    /**
     * Получить заказы за период
     */

    /**
     * Получить заказы по статусу
     */

    /**
     * Получить заказы конкретного клиента
     */

    /**
     * Produce a data map with order records grouped by customer
     */

    /**
     * Получить суммарную стоимость каждого заказа
     * Produce a data map with order record and product total sum
     */

    /**
     * Получить заказы с минимальной/максимальной стоимостью
     */

    /**
     * Получить среднюю стоимость заказа
     */

    /**
     * Получить продукты, которые заказывались чаще всего
     */

    /**
     * Получить даты с наибольшим количеством заказов
     */

    /**
     * Calculate total lump sum of all orders placed in Feb 2021
     */

    /**
     * Obtain a data map with order id and order’s product count
     */



}
