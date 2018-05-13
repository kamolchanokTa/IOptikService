package optik.models.stock;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import optik.models.product.Product;


/**
 * A DAO for the entity stock is simply created by extending the CrudRepository
 * interface provided by spring. The following methods are some of the ones
 * available from such interface: save, delete, deleteAll, findOne and findAll.
 * The magic is that such methods must not be implemented, and moreover it is
 * possible create new query methods working only by defining their signature!
 * 
 * @author Kamolchanok Tangsri
 */
@Transactional
public interface StockDao extends CrudRepository<Stock, UUID> {

  /**
   * Return the products having the productType or null if productType is found.
   * 
   * @param productType .
   */
  public Set<Stock> findByProduct(Product product);
  

} 