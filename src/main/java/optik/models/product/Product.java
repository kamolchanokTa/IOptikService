package optik.models.product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import optik.models.orderdetail.OrderDetail;
import optik.models.stock.Stock;

@Entity
@Table(name = "product")
public class Product {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	// The product amount
	@NotNull
	private String name;
	
	@NotNull
	private String productType;
	
	@NotNull
	private double price;
	
	@NotNull
	private String description;
	
	@Lob
	private byte[] image;

	public Product(String name, String productType, double price, byte[] image, String description) {
		this.name = name;
		this.productType = productType;
		this.price = price;
		this.image = image;
		this.description= description;
	}
	
	public Product() {
		
	}

	// Getter and setter methods

	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id= id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	public String getProductType() {
		return this.productType;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public Double getPrice() {
		return this.price;
	}
	
	public void setImage(byte[] image) {
		this.image = image;
	}
	
	public byte[] getImage() {
		return this.image;
	}

	@OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<OrderDetail> orderdetails = new HashSet<>();
    
    public void addOrderDetail(OrderDetail orderdetail) {
		this.orderdetails.add(orderdetail);
		orderdetail.setProduct(this);
	}

	private String getOrderDetails() {
		ArrayList<OrderDetail> orderdetaillist = new ArrayList<OrderDetail>(this.orderdetails);
		return orderdetaillist.size() + "";
	}

	public void removeOrderDetail(OrderDetail orderdetail) {
		orderdetails.remove(orderdetail);
		orderdetail.setProduct(null);
	}
	
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Stock> stocks = new HashSet<>();

	public void addStock(Stock stock) {
		this.stocks.add(stock);
		stock.setProduct(this);
	}

	public void removeStock(Stock stock) {
		stocks.remove(stock);
		stock.setProduct(null);
	}
	
	public String getDescription() {
		return this.description;
	}

}
