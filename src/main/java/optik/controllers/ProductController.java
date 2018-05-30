package optik.controllers;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
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

import optik.models.orderdetail.OrderDetail;
import optik.models.orderdetail.OrderDetailDao;
import optik.models.product.Product;
import optik.models.product.ProductDao;
import optik.models.product.ProductRequest;
import optik.models.response.ProductResponse;
import optik.models.response.Response;
import optik.models.stock.Stock;
import optik.models.stock.StockDao;
import optik.models.user.User;
import optik.models.user.UserDao;

@Controller
public class ProductController extends BaseController {

	ProductController() {
		super();
	}

	@RequestMapping(value = "/product/create", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody ProductRequest productRequest,
			@RequestHeader(value = "appId") String appid) {
		if (isValidAppid(appid)) {
			UUID uuid = UUID.fromString(productRequest.userid);
			User user = userDao.findById(uuid).get();
			if(user.getUserType() == '0') {
				try {
					
					byte[] imageBytes = Base64.getDecoder().decode(productRequest.image);
					Product product = new Product(productRequest.name, productRequest.productType, productRequest.price,
							imageBytes, productRequest.description);
					productDao.save(product);
					Date convertedDate = new Date();
					Stock stock = new Stock(productRequest.amount,convertedDate);
					stock.setProduct(product);
					stockDao.save(stock);
				} catch (Exception ex) {
					return new ResponseEntity<>(new Response(getAppProperties().getStatus().getFail(),
							"Error creating the product: " + ex.toString(), null), HttpStatus.BAD_REQUEST);
				}
				return new ResponseEntity<>(
						new Response(getAppProperties().getStatus().getSuccess(), "Product successfully created!", null),
						HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>(
						new Response(getAppProperties().getStatus().getUnautherized(), "Access By unauthorized user", null),
						HttpStatus.FORBIDDEN);
			}
		} else {
			return new ResponseEntity<>(
					new Response(getAppProperties().getStatus().getUnautherized(), "Access By unauthorized app", null),
					HttpStatus.FORBIDDEN);
		}

	}

	@RequestMapping("/product/delete")
	@ResponseBody
	public ResponseEntity<?> delete(String id, @RequestHeader(value = "appId") String appid) {
		if (isValidAppid(appid)) {
			try {
				UUID productid = UUID.fromString(id);
				Product product = productDao.findById(productid).get();
				productDao.delete(product);
				return new ResponseEntity<>(
						new Response(getAppProperties().getStatus().getSuccess(), "Product successfully deleted!", product),
						HttpStatus.OK);
			} catch (Exception ex) {
				return new ResponseEntity<>(new Response(getAppProperties().getStatus().getFail(),
						"Error deleting the product: " + ex.toString(), null), HttpStatus.FORBIDDEN);
			}
			
		} else {
			return new ResponseEntity<>(
					new Response(getAppProperties().getStatus().getUnautherized(), "Access By unauthorized app", null),
					HttpStatus.FORBIDDEN);
		}
	}

	@RequestMapping("/products")
	@ResponseBody
	public ResponseEntity<?> getProducts(@RequestHeader(value = "appId") String appid) {
		if (isValidAppid(appid)) {
			Iterator<Product> productlist;
			ArrayList<ProductResponse> productResponses = new ArrayList<ProductResponse>();
			try {
				productlist = productDao.findAll().iterator();
				List<Product> products = StreamSupport
						.stream(Spliterators.spliteratorUnknownSize(productlist, Spliterator.ORDERED), false)
						.collect(Collectors.<Product>toList());

				for (Product product : products) {
					int soldQuantity = getQuantityOfSold(product);
					int availableInStock = getQuantityOfStock(product);
					int availableQuantity = availableInStock - soldQuantity;

					productResponses.add(new ProductResponse(product.getId(), product.getName(),
							product.getProductType(), product.getPrice(), product.getDescription(), availableQuantity, product.getImage()));
				}
			} catch (Exception ex) {
				return new ResponseEntity<>(new Response(getAppProperties().getStatus().getFail(),
						"Error retrieve the products: " + ex.toString(), null), HttpStatus.FORBIDDEN);
			}
			return new ResponseEntity<>(new Response(getAppProperties().getStatus().getSuccess(), "Success retrieve products", productResponses), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					new Response(getAppProperties().getStatus().getUnautherized(), "Access By unauthorized app", null),
					HttpStatus.FORBIDDEN);
		}
	}

	@RequestMapping("/product")
	@ResponseBody
	public ResponseEntity<?> getProduct(String id, @RequestHeader(value = "appId") String appid) {
		if (isValidAppid(appid)) {
			Product product;
			ProductResponse productResponse;
			try {
				UUID productid = UUID.fromString(id);
				product = productDao.findById(productid).get();
				int soldQuantity = getQuantityOfSold(product);
				int availableInStock = getQuantityOfStock(product);
				int availableQuantity = availableInStock - soldQuantity;

				productResponse = new ProductResponse(product.getId(), product.getName(), product.getProductType(),
						product.getPrice(), product.getDescription(), availableQuantity, product.getImage());
			} catch (Exception ex) {
				return new ResponseEntity<>(new Response(getAppProperties().getStatus().getFail(),
						"Error retrieve the product: " + ex.toString(), null), HttpStatus.FORBIDDEN);
			}
			return new ResponseEntity<>(productResponse, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					new Response(getAppProperties().getStatus().getUnautherized(), "Access By unauthorized app", null),
					HttpStatus.FORBIDDEN);
		}
	}

	private int getQuantityOfStock(Product product) {
		Iterator<Stock> stocks = stockDao.findByProduct(product).iterator();
		int quantity = 0;
		for (Iterator<Stock> it = stocks; it.hasNext();) {
			Stock stock = it.next();
			quantity += stock.getQuantity();
		}
		return quantity;
	}

	private int getQuantityOfSold(Product product) {
		Iterator<OrderDetail> orderDetails = orderDetailDao.findByProduct(product).iterator();
		int quantity = 0;
		for (Iterator<OrderDetail> it = orderDetails; it.hasNext();) {
			OrderDetail orderDetail = it.next();
			quantity += orderDetail.getAmount();
		}
		return quantity;
	}

	@Autowired
	private ProductDao productDao;

	@Autowired
	private OrderDetailDao orderDetailDao;

	@Autowired
	private StockDao stockDao;
	
	@Autowired
	private UserDao userDao;
}
