package space.gavinklfong.demo.streamapi.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private Integer tier;

	@OneToMany(mappedBy = "customer")
	private Set<Order> orders = new HashSet<>();

	// Конструкторы
	public Customer() {}

	public Customer(Long id, String name, Integer tier) {
		this.id = id;
		this.name = name;
		this.tier = tier;
	}

	// Геттеры
	public Long getId() { return id; }
	public String getName() { return name; }
	public Integer getTier() { return tier; }
	public Set<Order> getOrders() { return orders; }

	// Сеттеры
	public void setId(Long id) { this.id = id; }
	public void setName(String name) { this.name = name; }
	public void setTier(Integer tier) { this.tier = tier; }
	public void setOrders(Set<Order> orders) { this.orders = orders; }

	// equals и hashCode
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Customer customer = (Customer) o;
		return Objects.equals(id, customer.id) &&
				Objects.equals(name, customer.name) &&
				Objects.equals(tier, customer.tier);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, tier);
	}

	@Override
	public String toString() {
		return "Customer{" +
				"id=" + id +
				", name='" + name + '\'' +
				", tier=" + tier +
				'}';
	}
}
