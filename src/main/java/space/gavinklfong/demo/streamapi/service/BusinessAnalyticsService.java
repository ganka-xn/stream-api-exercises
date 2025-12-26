package space.gavinklfong.demo.streamapi.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.gavinklfong.demo.streamapi.repos.CustomerRepo;
import space.gavinklfong.demo.streamapi.repos.OrderRepo;
import space.gavinklfong.demo.streamapi.repos.ProductRepo;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * Упражнение 2: Получить список продуктов, купленных клиентами tier=2
     */

    /**
     * Упражнение 3: Получить список недавних заказов (последние 3 дня)
     */

    /**
     * Упражнение 4: Получить список продуктов, заказанных в определенную дату
     */

    /**
     * Упражнение 5: Получить самую дорогую покупку
     */

    /**
     * Получить выручку по дням
     */

    /**
     * Получить топ-5 самых популярных категорий
     */

    /**
     * Получить клиентов, которые купили все продукты категории
     */

    /**
     * Получить отчет по продажам
     */
    public Map<String, Object> getSalesReport() {
        Map<String, Object> report = new HashMap<>();

        return report;
    }


}
