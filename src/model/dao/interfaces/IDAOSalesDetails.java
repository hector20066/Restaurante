package model.dao.interfaces;

import java.util.List;
import model.dao.SaleDetailsDTO;
import model.entities.Sale;
import model.entities.SaleDetail;

/**
 *
 * @author Mi PC
 */
public interface IDAOSalesDetails extends IDAO<SaleDetail> {
    
    List<SaleDetailsDTO> getAllData() throws Exception;
    
}
