package optik.models.response;

import java.util.Base64;
import java.util.UUID;

public class ProductResponse {
	public UUID id;
	
	public String name;
	
	public String image;
	
	public String productType;
	
	public double price;
	
	public String description;
	
	public int availableproduct;
	
	public ProductResponse(UUID id, String name, String productType, double price, String description, int availableproduct, byte[] image) {
		this.id = id;
		this.name = name;
		this.productType=productType;
		this.price = price;
		this.description = description;
		this.availableproduct = availableproduct;
		this.image = Base64.getEncoder().encodeToString(image);;
	}

}
