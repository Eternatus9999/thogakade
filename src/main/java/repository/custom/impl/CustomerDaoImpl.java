package repository.custom.impl;

import db.DBConnection;
import entity.CustomerEntity;
import javafx.collections.ObservableList;
import dto.Customer;
import javafx.scene.control.Alert;
import org.hibernate.Session;
import repository.custom.CustomerDao;
import util.HibernateUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerDaoImpl implements CustomerDao {

    @Override
    public boolean save(CustomerEntity customer) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(customer);
        session.getTransaction().commit();
        session.close();

//        try {
//            String SQL = "INSERT INTO customer VALUES(?,?,?,?,?,?,?,?,?)";
//            Connection connection = DBConnection.getInstance().getConnection();
//            PreparedStatement psTm = connection.prepareStatement(SQL);
//            psTm.setObject(1, customer.getId());
//            psTm.setObject(2, customer.getTitle());
//            psTm.setObject(3, customer.getName());
//            psTm.setDate(4, Date.valueOf(customer.getDob()));
//            psTm.setDouble(5, customer.getSalary());
//            psTm.setObject(6, customer.getAddress());
//            psTm.setObject(7, customer.getCity());
//            psTm.setObject(8, customer.getProvince());
//            psTm.setObject(9, customer.getPostalCode());
//            return psTm.executeUpdate() > 0;
//        } catch (SQLException e) {
//            new Alert(Alert.AlertType.ERROR,"Error : "+e.getMessage()).show();
//        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public ObservableList<CustomerEntity> getAll() {
        return null;
    }

    @Override
    public boolean update(CustomerEntity customer) {
        return false;
    }

    @Override
    public CustomerEntity search(String name) {
        return null;
    }
}
