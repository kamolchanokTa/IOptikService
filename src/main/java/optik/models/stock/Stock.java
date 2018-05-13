package optik.models.stock;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
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
import optik.models.user.User;

@Entity
@Table(name = "stock")
public class Stock {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;
	
	@NotNull
	private int quantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	public Stock(int quantity, Date updateStockDate) {
		this.quantity = quantity;
		this.updatestockdate = updateStockDate;
	}
	
	public Stock() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Stock))
			return false;
		return id != null && id.equals(((Stock) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	public UUID getId() {
		return id;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public int getQuantity() {
		return this.quantity;
	}

	@NotNull
	@Column(name = "updatestockdate", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatestockdate;

	public void setProduct(Product product) {
		this.product = product;
	}

}
