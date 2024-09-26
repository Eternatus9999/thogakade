package controller.item;

import javafx.collections.ObservableList;
import dto.Item;
import dto.OrderDetail;

import java.util.List;

public interface ItemService {
    boolean addItem(Item item);
    boolean deleteItem(String id);
    ObservableList<Item> getAll();
    boolean updateItem(Item item);
    Item searchItem(String description);

    ObservableList<String> getItemCodes();

    boolean updateStock(List<OrderDetail> orderDetails);
}
