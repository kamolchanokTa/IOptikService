package optik.models.product;

import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;


/**
 * A DAO for the entity User is simply created by extending the CrudRepository
 * interface provided by spring. The following methods are some of the ones
 * available from such interface: save, delete, deleteAll, findOne and findAll.
 * The magic is that such methods must not be implemented, and moreover it is
 * possible create new query methods working only by defining their signature!
 * 
 * @author Kamolchanok Tangsri
 */
@Transactional
public interface ProductDao extends CrudRepository<Product, UUID> {

  /**
   * Return the products having the productType or null if productType is found.
   * 
   * @param productType .
   */
  public Set<Product> findByProductType(String productType);
  

} 