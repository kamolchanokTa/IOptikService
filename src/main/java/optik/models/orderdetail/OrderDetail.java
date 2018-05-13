package optik.models.orderdetail;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import optik.models.order.Order;
import optik.models.product.Product;

/**
 * An entity OrderDetail. The Entity
 * annotation indicates that this class is a JPA entity. The Table annotation
 * specifies the name for the table in the db.
 *
 * @author Kamolchanok Tangsri
 */
@Entity
@Table(name = "orderdetail")

public class OrderDetail {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	// The product amount
	@NotNull
	private int amount;

	public OrderDetail(int amount) {
		this.amount = amount;
	}
	
	public OrderDetail() {
	}

	// Getter and setter methods

	public UUID getId() {
		return id;
	}

	public void setId(UUID value) {
		this.id = value;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof OrderDetail))
			return false;
		return id != null && id.equals(((OrderDetail) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	public Order getOrder() {
		return this.order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	private Product product;

	public void setProduct(Product product) {
		this.product = product;
		
	}
	
	public Product getProduct() {
		return this.product;
	}
	
	public int getAmount() {
		return this.amount;
	}
}
