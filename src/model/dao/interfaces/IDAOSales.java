/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package model.dao.interfaces;

import java.util.List;
import model.dao.SaleDetailsDTO;
import model.entities.Sale;

/**
 *
 * @author Mi PC
 */
public interface IDAOSales extends DAOWithAutoIncrement<Sale>{
    
    List<SaleDetailsDTO> getAllData() throws Exception;
    
}
