package optik.models.order;

import java.util.ArrayList;
import optik.models.orderdetail.OrderDetailRequest;

public class OrderRequest {
	public String email;
	  
	// The user's first name
	
	public String firstname;
	
	// The user's last name
	
	public String lastname;
		  
	// The user's Date of birth
	public String orderdate;
  
	// The user's Address

	public String address;
	  
	//The user's city
	
	public String city;
	 
	//The user's country
	
	public String country;
	
	//The user's zipcode
	
	public String zipcode;
	
	public String userid;
	
	public double totalprice;
	
	public String deliverymethod;
	
	public ArrayList<OrderDetailRequest> orderdetails;
}
