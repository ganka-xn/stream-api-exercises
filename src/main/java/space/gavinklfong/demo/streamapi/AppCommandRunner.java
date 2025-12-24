package space.gavinklfong.demo.streamapi;

//import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import space.gavinklfong.demo.streamapi.repos.CustomerRepo;
import space.gavinklfong.demo.streamapi.repos.OrderRepo;
import space.gavinklfong.demo.streamapi.repos.ProductRepo;

//@Slf4j
@Component
public class AppCommandRunner implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(AppCommandRunner.class);

	@Autowired
	private CustomerRepo customerRepos;
	
	@Autowired
	private OrderRepo orderRepos;
	
	@Autowired
	private ProductRepo productRepos;

	@Transactional
	@Override
	public void run(String... args) throws Exception {
		log.info("Customers:");
		customerRepos.findAll()
				.forEach(c -> log.info(c.toString()));

		log.info("Orders:");
		orderRepos.findAll()
				.forEach(o -> log.info(o.toString()));

		log.info("Products:");
		productRepos.findAll()
				.forEach(p -> log.info(p.toString()));
	}
}
