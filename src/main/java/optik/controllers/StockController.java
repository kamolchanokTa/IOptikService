package optik.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import optik.models.product.Product;
import optik.models.product.ProductDao;
import optik.models.product.ProductRequest;
import optik.models.response.Response;
import optik.models.stock.Stock;
import optik.models.stock.StockDao;
import optik.models.stock.StockRequest;
import optik.models.user.User;
import optik.models.user.UserDao;

@Controller
public class StockController extends BaseController{
	
	@RequestMapping(value = "/stock/create", method = RequestMethod.POST)
	  @ResponseBody
	  public ResponseEntity<?>  create(@RequestBody StockRequest stockRequest, @RequestHeader(value="appId") String appid) {
		  if(isValidAppid(appid)) {
			  try {
				  Date convertedDate = null;
				  SimpleDateFormat sdf = new SimpleDateFormat(getAppProperties().getDateformat());
				  convertedDate = sdf.parse(stockRequest.updateStockDate);
				  Stock stock = new Stock(stockRequest.quantity,convertedDate);
				  UUID productid = UUID.fromString(stockRequest.productId);
				  Product product = productDao.findById(productid).get();
				  stock.setProduct(product);
				  stockDao.save(stock);
			  }
			  catch (Exception ex) {
				  return new ResponseEntity<>( new Response(getAppProperties().getStatus().getFail(), "Error creating the stock: " + ex.toString(), null)
						  , HttpStatus.BAD_REQUEST);
			  }
			  return new ResponseEntity<>( new Response(getAppProperties().getStatus().getSuccess(), "Stock successfully created!", null), HttpStatus.OK) ;
		  }
		  else {
			  return new ResponseEntity<>( new Response(getAppProperties().getStatus().getUnautherized(), "Access By unauthorized app", null), HttpStatus.FORBIDDEN);
		  }	
	    
	  }
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private StockDao stockDao;
}
