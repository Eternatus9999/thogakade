package service.custom.impl;

import db.DBConnection;
import entity.CustomerEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import dto.Customer;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.CustomerDao;
import service.custom.CustomerService;
import util.DaoType;

import java.sql.*;

public class CustomerServiceImpl implements CustomerService {
        @Override
        public boolean addCustomer(Customer customer) {
            CustomerEntity entity = new ModelMapper().map(customer,CustomerEntity.class);

            CustomerDao customerdao = DaoFactory.getInstance().getDaoType(DaoType.CUSTOMER);

            customerdao.save(entity);

            return true;
        }

        @Override
        public boolean deleteCustomer(String id) {
            String SQL = "DELETE FROM customer WHERE CustID='" + id + "'";
            try {
                Connection connection = DBConnection.getInstance().getConnection();
                return connection.createStatement().executeUpdate(SQL) > 0;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public ObservableList<Customer> getAll() {
            ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
            try {
                String SQL = "SELECT * FROM customer";
                Connection connection = DBConnection.getInstance().getConnection();
                System.out.println(connection);
                PreparedStatement psTm = connection.prepareStatement(SQL);
                ResultSet resultSet = psTm.executeQuery();
                while (resultSet.next()) {
                    //System.out.println(resultSet.getString("CustTitle") + resultSet.getString("CustName"));
                    Customer customer = new Customer(
                            resultSet.getString("CustID"),
                            resultSet.getString("CustTitle"),
                            resultSet.getString("CustName"),
                            resultSet.getString("CustAddress"),
                            resultSet.getDate("DOB").toLocalDate(),
                            resultSet.getDouble("salary"),
                            resultSet.getString("City"),
                            resultSet.getString("Province"),
                            resultSet.getString("postalCode")
                    );
                    customerObservableList.add(customer);
                    //System.out.println(customer);
                }
                return customerObservableList;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean updateCustomer(Customer customer) {
            String SQL = "UPDATE customer SET CustTitle=?, CustName=?, DOB=?, salary=?, CustAddress=?, City=?, Province=?, postalCode=? WHERE CustID=?";
            try {
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement psTm = connection.prepareStatement(SQL);
                psTm.setObject(1, customer.getTitle());
                psTm.setObject(2, customer.getName());
                psTm.setDate(3, Date.valueOf(customer.getDob()));
                psTm.setDouble(4, customer.getSalary());
                psTm.setObject(5, customer.getAddress());
                psTm.setObject(6, customer.getCity());
                psTm.setObject(7, customer.getProvince());
                psTm.setObject(8, customer.getPostalCode());
                psTm.setObject(9, customer.getId());
                return psTm.executeUpdate() > 0;
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Error : " + e.getMessage()).show();
            }
            return false;
        }

        @Override
        public Customer searchCustomer(String idOrName) {
            String SQL = "SELECT * FROM customer WHERE CustID=? OR CustName=?";
            try {
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement psTm = connection.prepareStatement(SQL);
                psTm.setString(1, idOrName);
                psTm.setString(2, idOrName);
                ResultSet resultSet = psTm.executeQuery();

                if (resultSet.next()) {
                    return new Customer(
                            resultSet.getString("CustID"),
                            resultSet.getString("CustTitle"),
                            resultSet.getString("CustName"),
                            resultSet.getString("CustAddress"),
                            resultSet.getDate("DOB").toLocalDate(),
                            resultSet.getDouble("salary"),
                            resultSet.getString("City"),
                            resultSet.getString("Province"),
                            resultSet.getString("postalCode")
                    );
                } else {
                    new Alert(Alert.AlertType.WARNING, "Customer not found!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Error : " + e.getMessage()).show();
            }
            return null;
        }
        @Override
        public ObservableList<String> getCustomerIds(){
            ObservableList<String> customerIds = FXCollections.observableArrayList();
            ObservableList<Customer> customerObserverList = getAll();
            customerObserverList.forEach(customer -> {
                customerIds.add(customer.getId());
            });
            return customerIds;
        }
    }

