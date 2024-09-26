package controller.order;

import service.ServiceFactory;
import service.custom.CustomerService;
import controller.item.ItemController;
import db.DBConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import dto.*;
import util.ServiceType;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class PlaceOrderController implements Initializable {

    public Label lblTotal;
    public TextField txtOrderId;
    @FXML
    private ComboBox<String> cmbCustIomerId;

    @FXML
    private ComboBox<String> cmbItemCode;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblTime;

    @FXML
    private TableView<CartTM> tblOrders;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtItemDescription;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtStock;

    @FXML
    private TextField txtUnitPrice;

    CustomerService customerserivice = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);

    ObservableList<CartTM> cartTMS = FXCollections.observableArrayList();
    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        String itemCode = cmbItemCode.getValue();
        String description = txtItemDescription.getText();
        Integer qty = Integer.parseInt(txtQty.getText());
        Double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        Double total = unitPrice*qty;

        if (Integer.parseInt(txtStock.getText())<qty){
            new Alert(Alert.AlertType.WARNING,"Invalid QTY").show();
        }else{
            cartTMS.add(new CartTM(itemCode,description,qty,unitPrice,total));
            calcNetTotal();
        }
        tblOrders.setItems(cartTMS);
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
        String orderId = txtOrderId.getText();
        LocalDate orderDate = LocalDate.parse(lblDate.getText());
        String customerId = cmbCustIomerId.getValue();

        List<OrderDetail> orderDetails = new ArrayList<>();

        cartTMS.forEach(obj->{
            orderDetails.add(new OrderDetail(orderId, obj.getItemCode(), obj.getQty(), 0.0));
        });
        Order order = new Order(orderId,orderDate,customerId,orderDetails);

        System.out.println(order);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDateAndTime();
        loadCustomerIds();
        loadItemCodes();
        cmbCustIomerId.getSelectionModel().selectedItemProperty().addListener((observableValue, s, newVal) -> {
            if (newVal!=null){
                searchCustomer(newVal);
            }
        });
        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, s, newVal) -> {
            if (newVal!=null){
                searchItem(newVal);
            }
        });
    }

    private void loadDateAndTime(){
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String dateNow = f.format(date);

        lblDate.setText(dateNow);

//        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, event -> {
//            LocalTime now = LocalTime.now();
//            lblTime.setText(now.getHour() + " : " + now.getMinute() + " : " + now.getSecond());
//        }),
//                new KeyFrame(Duration.seconds(1))
//        );
//        timeline.setCycleCount(Animation.INDEFINITE);
//        timeline.play();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH : mm : ss");

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, event -> {
                    LocalTime now = LocalTime.now();
                    lblTime.setText(now.format(formatter));
                }),
                new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    private void loadCustomerIds(){
        cmbCustIomerId.setItems(customerserivice.getCustomerIds());
    }
    private void loadItemCodes(){
        cmbItemCode.setItems(ItemController.getInstance().getItemCodes());
    }
    private void searchItem(String newVal) {
        Item item = ItemController.getInstance().searchItem(newVal);
        txtItemDescription.setText(item.getDescription());
        txtStock.setText(item.getQtyOnHand().toString());
        txtUnitPrice.setText(item.getUnitPrice().toString());
    }

    private void searchCustomer(String customerID){
        Customer customer = customerserivice.searchCustomer(customerID);
        txtCustomerName.setText(customer.getName());
        txtAddress.setText(customer.getAddress());
        //System.out.println(customerID);
    }

    private void calcNetTotal(){
        Double total=0.0;

        for (CartTM cartTM: cartTMS) {
            total += cartTM.getTotal();
        }

        lblTotal.setText(total.toString()+"/=");

    }

    public void btnCommitOnAction(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
