package model.dao.interfaces;

import model.dao.SaleDetailsDTO;
import model.entities.SaleDetail;

import java.util.List;

/**
 *
 * @author Mi PC
 */
public interface IDAOSalesDetails extends IDAO<SaleDetail> {

    List<SaleDetailsDTO> getAllData() throws Exception;

}
