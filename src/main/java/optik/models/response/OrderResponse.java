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

		
		public User user;
		
		public List<OrderDetail> orderdetails;
		
		public String orderDate;
		
		public double totalPrice;
		
		public OrderResponse(UUID id, double totalprice, User user, List<OrderDetail> orderdetails, double totalPrice, String orderDate) {
			this.id= id;
			this.user = user;
			this.orderdetails = orderdetails;
			this.totalPrice = totalPrice;
			this.orderDate = orderDate;
		}

}
