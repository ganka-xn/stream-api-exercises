package space.gavinklfong.demo.streamapi.models;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String category;
	private Double price;

	@ManyToMany(mappedBy = "products")
	private Set<Order> orders = new HashSet<>();

	public Product() {
	}

	public Product(Long id, String name, String category, Double price) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.price = price;
	}

	// Builder-паттерн вручную
	public static ProductBuilder builder() {
		return new ProductBuilder();
	}

	public static class ProductBuilder {
		private Long id;
		private String name;
		private String category;
		private Double price;

		private ProductBuilder() {}

		public ProductBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public ProductBuilder name(String name) {
			this.name = name;
			return this;
		}

		public ProductBuilder category(String category) {
			this.category = category;
			return this;
		}

		public ProductBuilder price(Double price) {
			this.price = price;
			return this;
		}

		public Product build() {
			Product product = new Product();
			product.setId(id);
			product.setName(name);
			product.setCategory(category);
			product.setPrice(price);
			return product;
		}
	}

	// Метод with для создания копии с измененным полем
	public Product withPrice(Double price) {
		Product newProduct = new Product();
		newProduct.setId(this.id);
		newProduct.setName(this.name);
		newProduct.setCategory(this.category);
		newProduct.setPrice(price);
		newProduct.setOrders(this.orders);
		return newProduct;
	}

	// метод для поддержки двунаправленной связи
	public void addOrder(Order order) {
		if (this.orders == null) {
			this.orders = new HashSet<>();
		}
		this.orders.add(order);
		if (order.getProducts() != null) {
			order.getProducts().add(this);
		}
	}

	public void removeOrder(Order order) {
		if (this.orders != null) {
			this.orders.remove(order);
		}
		if (order.getProducts() != null) {
			order.getProducts().remove(this);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	// equals и hashCode (исключая orders, чтобы избежать циклических ссылок)
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Product product = (Product) o;
		return Objects.equals(id, product.id) &&
				Objects.equals(name, product.name) &&
				Objects.equals(category, product.category) &&
				Objects.equals(price, product.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, category, price);
	}

	// toString (исключая orders, чтобы избежать циклических ссылок)
	@Override
	public String toString() {
		return "Product{" +
				"id=" + id +
				", name='" + name + '\'' +
				", category='" + category + '\'' +
				", price=" + price +
				'}';
	}
}
