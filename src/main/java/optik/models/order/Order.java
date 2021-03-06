package optik.models.order;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import optik.models.orderdetail.OrderDetail;
import optik.models.user.User;

/**
 * An entity Order composed by three fields (id, email, name). The Entity
 * annotation indicates that this class is a JPA entity. The Table annotation
 * specifies the name for the table in the db.
 *
 * @author Kamolchanok Tangsri
 */
@Entity
@Table(name = "cart")

public class Order {

	// An autogenerated id (unique for each user in the db)
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	

	// The user's Date of birth
	@Column(name = "orderdate", columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderdate;

	

	@NotNull
	private double totalprice;

	public Order() {

	}

	public Order(String email, String firstName, String lastName, Date orderdate, String address, String city,
			String country, String zipcode, double totalprice, String deliverymethod) {
		this.orderdate = orderdate;
		this.totalprice = totalprice;
		this.deliverymethod = deliverymethod;
	}
	
	private String deliverymethod;

	// Getter and setter methods
	
	public String getDeliveryMethod() {
		return  this.deliverymethod;
	}
	

	public UUID getId() {
		return id;
	}

	public void setId(UUID value) {
		this.id = value;
	}

	public Date getOrderDate() {
		return this.orderdate;
	}
	
	public double getTotalPrice() {
		return this.totalprice;
	}

	public void setOrder(User user) {
		// TODO Auto-generated method stub

	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Order))
			return false;
		return id != null && id.equals(((Order) o).id);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderDetail> orderdetails = new HashSet<>();

	public void addOrderDetail(OrderDetail orderdetail) {
		this.orderdetails.add(orderdetail);
		orderdetail.setOrder(this);
	}

	public void removeOrder(OrderDetail orderdetail) {
		orderdetails.remove(orderdetail);
		orderdetail.setOrder(null);
	}

}
