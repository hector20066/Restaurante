package factory;

import model.dao.impl.*;
import model.enums.InstanceTypeDB;
import model.enums.InstanceTypeFile;

/**
 *
 * @author hectoracevedo
 */
public class FactoryMethod {

    public static <T> T getInstanceDB (InstanceTypeDB type) {
        switch (type) {
            case InstanceTypeDB.DAODISHDB -> {
                return (T) DAODishDBImpl.getInstance();
            }
            case InstanceTypeDB.DAODRINKDB -> {
                return (T) DAODrinkDBImpl.getInstance();
            }
            case InstanceTypeDB.CUSTOMERDB -> {
                return (T) IDAOCustomersImpl.getInstance();
            }
            case InstanceTypeDB.SALEDB -> {
                return (T) IDAOSalesImpl.getInstance();
            }
            case InstanceTypeDB.QUALIFICATIONDB -> {
                return (T) IDAOQualificationImpl.getInstance();
            }
            default -> {
                return null;
            }
        }
    }

    public static <T> T getInstanceFile (InstanceTypeFile type) {
        switch (type) {
            case InstanceTypeFile.DAODISHFILE -> {
                return (T) DAODishFileImpl.getInstance();
            }
            case InstanceTypeFile.DAODRINKFILE -> {
                return (T) DAODrinkFileImpl.getInstance();
            }
            default -> {
                return null;
            }
        }
    }

}
