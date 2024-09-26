package controller.item;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import dto.Item;
import dto.OrderDetail;
import util.CrudUtil;

import java.sql.*;
import java.util.List;

public class ItemController implements ItemService{

    private static ItemController instance;
    private ItemController(){}
    public static ItemController getInstance(){
        return instance==null?instance=new ItemController():instance;
    }
    @Override
    public boolean addItem(Item item) {
        String SQL = "INSERT INTO item VALUES(?,?,?,?,?)";
        try {
            return CrudUtil.execute(SQL,item.getItemCode(),item.getDescription(),item.getPackSize(),item.getUnitPrice(),item.getQtyOnHand());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Error : "+e.getMessage()).show();
        }
        return false;
    }

    @Override
    public boolean deleteItem(String id) {
        String SQL = "DELETE FROM item WHERE ItemCode='" + id + "'";
        try {
            return CrudUtil.execute(SQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<Item> getAll() {
        ObservableList<Item> itemObservableList = FXCollections.observableArrayList();
        String SQL = "SELECT * FROM item";
        try {
            ResultSet resultSet = CrudUtil.execute(SQL);
            while (resultSet.next()) {
                Item item = new Item(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4),
                        resultSet.getInt(5)
                );
                itemObservableList.add(item);
            }
            return itemObservableList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateItem(Item item) {
        String SQL = "UPDATE item SET Description=?, PackSize=?, UnitPrice=?, QtyOnHand=? WHERE ItemCode=?";
        try {
            return CrudUtil.execute(SQL,item.getDescription(),item.getPackSize(),item.getUnitPrice(),item.getQtyOnHand(),item.getItemCode());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error : " + e.getMessage()).show();
        }
        return false;
    }

    @Override
    public Item searchItem(String idOrName) {
        String SQL = "SELECT * FROM item WHERE ItemCode=? OR Description=?";
        try {
            ResultSet resultSet = CrudUtil.execute(SQL,idOrName,idOrName);
            while(resultSet.next()) {
                return new Item(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4),
                        resultSet.getInt(5)
                );
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error : " + e.getMessage()).show();
        }
        return null;
    }
    @Override
    public ObservableList<String> getItemCodes(){
        ObservableList<String> itemCodes = FXCollections.observableArrayList();
        ObservableList<Item> all = getAll();
        all.forEach(item -> {
            itemCodes.add(item.getItemCode());
        });
        return itemCodes;
    }
    @Override
    public boolean updateStock(List<OrderDetail> orderDetails){
        for (OrderDetail orderDetail:orderDetails){
            boolean updateStock = updateStock(orderDetail);
            if(!updateStock){
                return false;
            }
        }
        return true;
    }
    public boolean updateStock(OrderDetail orderDetails){
        String SQL = "UPDATE Item SET QtyOnHand=QtyOnHand-? WHERE ItemCode=?";
        try {
            return CrudUtil.execute(SQL,orderDetails.getQty(),orderDetails.getItemCode());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}