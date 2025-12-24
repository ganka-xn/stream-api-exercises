package space.gavinklfong.demo.streamapi.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "product_orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "order_date")
	private LocalDate orderDate;

	@Column(name = "delivery_date")
	private LocalDate deliveryDate;

	private String status;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@ManyToMany
	@JoinTable(
			name = "order_product_relationship",
			joinColumns = { @JoinColumn(name = "order_id") },
			inverseJoinColumns = { @JoinColumn(name = "product_id") }
	)
	private Set<Product> products = new HashSet<>();

	// Конструкторы
	public Order() {
	}

	public Order(Long id, LocalDate orderDate, LocalDate deliveryDate, String status, Customer customer) {
		this.id = id;
		this.orderDate = orderDate;
		this.deliveryDate = deliveryDate;
		this.status = status;
		this.customer = customer;
	}

	// Геттеры
	public Long getId() {
		return id;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}

	public String getStatus() {
		return status;
	}

	public Customer getCustomer() {
		return customer;
	}

	public Set<Product> getProducts() {
		return products;
	}

	// Сеттеры
	public void setId(Long id) {
		this.id = id;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	// equals и hashCode (исключая customer и products, чтобы избежать циклических ссылок)
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Order order = (Order) o;
		return Objects.equals(id, order.id) &&
				Objects.equals(orderDate, order.orderDate) &&
				Objects.equals(deliveryDate, order.deliveryDate) &&
				Objects.equals(status, order.status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, orderDate, deliveryDate, status);
	}

	// toString (исключая customer и products, чтобы избежать циклических ссылок)
	@Override
	public String toString() {
		return "Order{" +
				"id=" + id +
				", orderDate=" + orderDate +
				", deliveryDate=" + deliveryDate +
				", status='" + status + '\'' +
				", customerId=" + (customer != null ? customer.getId() : null) +
				", productsCount=" + (products != null ? products.size() : 0) +
				'}';
	}

	// Вспомогательные методы для работы с продуктами
	public void addProduct(Product product) {
		if (this.products == null) {
			this.products = new HashSet<>();
		}
		this.products.add(product);
		if (product.getOrders() != null) {
			product.getOrders().add(this);
		}
	}

	public void removeProduct(Product product) {
		if (this.products != null) {
			this.products.remove(product);
		}
		if (product.getOrders() != null) {
			product.getOrders().remove(this);
		}
	}
}