package optik.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import optik.models.order.Order;
import optik.models.order.OrderDao;
import optik.models.order.OrderRequest;
import optik.models.orderdetail.OrderDetail;
import optik.models.orderdetail.OrderDetailDao;
import optik.models.orderdetail.OrderDetailRequest;
import optik.models.product.Product;
import optik.models.product.ProductDao;
import optik.models.response.OrderResponse;
import optik.models.response.Response;
import optik.models.user.UpdateAddressRequest;
import optik.models.user.User;
import optik.models.user.UserDao;

@Controller
public class OrderController extends BaseController {

	OrderController() {
		super();
	}

	@RequestMapping(value = "/order/create", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody OrderRequest orderRequest,
			@RequestHeader(value = "appId") String appid) {
		if (isValidAppid(appid)) {
			Order order = null;
			SimpleDateFormat sdf = new SimpleDateFormat(getAppProperties().getDateformat());

			Date convertedDate = null;
			try {
				if (orderRequest.orderdate != null) {
					convertedDate = sdf.parse(orderRequest.orderdate);
				}
				order = new Order(orderRequest.email, orderRequest.firstname, orderRequest.lastname, convertedDate,
						orderRequest.address, orderRequest.city, orderRequest.country, orderRequest.zipcode,
						orderRequest.totalprice, orderRequest.deliverymethod);
				if (!orderRequest.userid.isEmpty()) {
					UUID uuid = UUID.fromString(orderRequest.userid);
					User user = userDao.findById(uuid).get();
					order.setUser(user);
					orderDao.save(order);
					user.addOrder(order);
				}
				double totalprice = 0;
				if (orderRequest.orderdetails.size() > 0) {
					for (OrderDetailRequest orderdetailrequest : orderRequest.orderdetails) {
						OrderDetail orderDetail = null;
						orderDetail = new OrderDetail(orderdetailrequest.amount);
						UUID productid = UUID.fromString(orderdetailrequest.productId);
						Product product = productDao.findById(productid).get();
						orderDetail.setProduct(product);
						orderDetail.setOrder(order);
						orderDetailDao.save(orderDetail);
						totalprice += (orderdetailrequest.amount * product.getPrice());
					}
				}
			} catch (Exception ex) {
				return new ResponseEntity<>(new Response(getAppProperties().getStatus().getFail(),
						"Error creating the user: " + ex.toString()), HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(
					new Response(getAppProperties().getStatus().getSuccess(), "User successfully created!"),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					new Response(getAppProperties().getStatus().getUnautherized(), "Access By unauthorized app"),
					HttpStatus.FORBIDDEN);
		}

	}

	@RequestMapping(value = "/orders/get/user", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(String userid, @RequestHeader(value = "appId") String appid) {
		String productName = "";
		if (isValidAppid(appid)) {
			try {
				UUID uuid = UUID.fromString(userid);
				User user = userDao.findById(uuid).get();
				Iterator<Order> orderlist = orderDao.findByUser(user).iterator();

				List<Order> orders = StreamSupport
						.stream(Spliterators.spliteratorUnknownSize(orderlist, Spliterator.ORDERED), false)
						.collect(Collectors.<Order>toList());
				ArrayList<OrderResponse> orderResponse= new ArrayList<OrderResponse>();
				for (Order order : orders) {
					
					Iterator<OrderDetail> orderdetailist = orderDetailDao.findByOrder(order).iterator();
					List<OrderDetail> orderdetails= StreamSupport
							.stream(Spliterators.spliteratorUnknownSize(orderdetailist, Spliterator.ORDERED), false)
							.collect(Collectors.<OrderDetail>toList());
					orderResponse.add(new OrderResponse(order.getId(), order.getEmail(),order.getFirstName(), order.getLastName(), order.getOrderDate()
							,order.getAddress(), order.getCity(), order.getCountry(), order.getZipcode(), order.getTotalPrice(), order.getUser(), orderdetails));

				}

				return new ResponseEntity<>(orderResponse,HttpStatus.OK);
			} catch (Exception ex) {
				return new ResponseEntity<>(new Response(getAppProperties().getStatus().getFail(),
						"Error get the order by userid: " + ex.toString()), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>(
					new Response(getAppProperties().getStatus().getUnautherized(), "Access By unauthorized app"),
					HttpStatus.FORBIDDEN);
		}

	}
	
	@RequestMapping(value = "/order/get", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getOrder(String id, @RequestHeader(value = "appId") String appid) {
		String productName = "";
		if (isValidAppid(appid)) {
			try {
				UUID uuid = UUID.fromString(id);
				User user = userDao.findById(uuid).get();
				Order order = orderDao.findById(uuid).get();
				Iterator<OrderDetail> orderdetailist = orderDetailDao.findByOrder(order).iterator();
				List<OrderDetail> orderdetails= StreamSupport
						.stream(Spliterators.spliteratorUnknownSize(orderdetailist, Spliterator.ORDERED), false)
						.collect(Collectors.<OrderDetail>toList());
				for(OrderDetail orderdetail: orderdetails) {
					Product product = orderdetail.getProduct();
					productName = product.getName();
					System.out.println(productName);
				}

				return new ResponseEntity<>(order,HttpStatus.OK);
			} catch (Exception ex) {
				return new ResponseEntity<>(new Response(getAppProperties().getStatus().getFail(),
						"Error get the order by userid: " + ex.toString()), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>(
					new Response(getAppProperties().getStatus().getUnautherized(), "Access By unauthorized app"),
					HttpStatus.FORBIDDEN);
		}

	}

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private OrderDetailDao orderDetailDao;

	@Autowired
	private ProductDao productDao;
}
