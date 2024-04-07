package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJBDC;
import model.dao.impl.DepartmentDaoJBDC;


public class DaoFactory {

    public static SellerDao createSellerDao() {
        return new SellerDaoJBDC(DB.getConnection());
    }
    
    public static DepartmentDao createDepartmentDao(){
        return  new DepartmentDaoJBDC(DB.getConnection());
    }
}
