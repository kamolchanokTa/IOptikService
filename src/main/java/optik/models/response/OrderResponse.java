package optik.models.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import optik.models.order.Order;
import optik.models.orderdetail.OrderDetail;
import optik.models.user.User;

public class OrderResponse {
	
		public UUID id;

		// The customer's email
		public String email;

		// The customer's first name
		
		public String firstname;

		// The customer's last name
		
		public String lastname;
		
		public Date orderdate;

		// The customer's Address

		public String address;

		// The customer's city

		public String city;

		// The customer's country

		public String country;

		// The customer's zipcode

		public String zipcode;

		
		public double totalprice;
		
		public User user;
		
		public List<OrderDetail> orderdetails;
		
		public OrderResponse(UUID id, String email, String firstName, String lastName, Date orderdate, String address, String city,
				String country, String zipcode, double totalprice, User user, List<OrderDetail> orderdetails) {
			this.id= id;
			this.orderdate = orderdate;
			this.email = email;
			this.firstname = firstName;
			this.lastname = lastName;
			this.address = address;
			this.city = city;
			this.country = country;
			this.zipcode = zipcode;
			this.totalprice = totalprice;
			this.user = user;
			this.orderdetails = orderdetails;
		}

}
