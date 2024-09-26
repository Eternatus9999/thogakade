package controller.item;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import dto.Item;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewItemController implements Initializable {

    @FXML
    private TableColumn<?, ?> colDesc;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colPackSize;

    @FXML
    private TableColumn<?, ?> colQtyHand;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private TableView<Item> tblViewItem;

    @FXML
    private JFXTextField txtDesc;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtPackSize;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXTextField txtUnitPrice;

    ItemService service = ItemController.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

        tblViewItem.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            if(newValue!=null) {
                setTextToValues(newValue);
            }
        }));
        loadTable();
    }
    private void setTextToValues(Item newValue) {
        txtId.setText(newValue.getItemCode());
        txtDesc.setText(newValue.getDescription());
        txtPackSize.setText(newValue.getPackSize());
        txtUnitPrice.setText(String.valueOf(newValue.getUnitPrice()));
        txtQty.setText(String.valueOf(newValue.getQtyOnHand()));
    }

    private void loadTable() {
        ObservableList<Item> all =service.getAll();
        tblViewItem.setItems(all);
    }

    public void btnOnClickAdd(ActionEvent event) {
        Item item = new Item(
                txtId.getText(),
                txtDesc.getText(),
                txtPackSize.getText(),
                Double.parseDouble(txtUnitPrice.getText()),
                Integer.parseInt(txtQty.getText())
        );
        if(service.addItem(item)){
            new Alert(Alert.AlertType.INFORMATION,"Item Added...").show();
        }else {
            new Alert(Alert.AlertType.ERROR,"Item Not Added :(").show();
        }
    }

    public void btnOnClickUpdate(ActionEvent event) {
        Item item = new Item(
                txtId.getText(),
                txtDesc.getText(),
                txtPackSize.getText(),
                Double.parseDouble(txtUnitPrice.getText()),
                Integer.parseInt(txtQty.getText())
        );

        if(service.updateItem(item)) {
            new Alert(Alert.AlertType.INFORMATION, "Item Updated...!").show();
            loadTable();
        } else {
            new Alert(Alert.AlertType.ERROR, "Item Not Updated :(").show();
        }
    }

    public void btnOnClickDelete(ActionEvent event) {
        if (service.deleteItem(txtId.getText())){
            new Alert(Alert.AlertType.INFORMATION,"Item Deleted...!").show();
            loadTable();
        }else{
            new Alert(Alert.AlertType.ERROR).show();
        }
    }

    public void btnOnClickSearch(ActionEvent event) {
        String idOrName = txtId.getText().isEmpty() ? txtDesc.getText() : txtId.getText();

        Item item = service.searchItem(idOrName);

        if (item != null) {
            setTextToValues(item);
        } else {
            new Alert(Alert.AlertType.WARNING, "Item Not Found Buddy :(").show();
        }
    }
}
