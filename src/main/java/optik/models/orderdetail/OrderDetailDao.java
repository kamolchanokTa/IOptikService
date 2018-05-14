package optik.models.orderdetail;

import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import optik.models.order.Order;
import optik.models.product.Product;



/**
 * A DAO for the entity OrderDetail is simply created by extending the CrudRepository
 * interface provided by spring. The following methods are some of the ones
 * available from such interface: save, delete, deleteAll, findOne and findAll.
 * The magic is that such methods must not be implemented, and moreover it is
 * possible create new query methods working only by defining their signature!
 * 
 * @author Kamolchanok Tangsri
 */
@Transactional
public interface OrderDetailDao extends CrudRepository<OrderDetail, UUID> {
	
  public Set<OrderDetail> findByOrder(Order order);
  
  public Set<OrderDetail> findByProduct(Product product);

} 
