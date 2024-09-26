package controller.customer;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import dto.Customer;
import service.ServiceFactory;
import service.custom.CustomerService;
import util.ServiceType;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class ViewFormController implements Initializable {


    @FXML
    private JFXComboBox<String> cmbTitle;

    @FXML
    private TableColumn<Customer, String> colAddress;

    @FXML
    private TableColumn<Customer, String> colCity;

    @FXML
    private TableColumn<Customer, Date> colDob;

    @FXML
    private TableColumn<Customer, String> colId;

    @FXML
    private TableColumn<Customer, String> colName;

    @FXML
    private TableColumn<Customer, String> colPostalCode;

    @FXML
    private TableColumn<Customer, String> colProvince;

    @FXML
    private TableColumn<Customer, Double> colsalary;

    @FXML
    private DatePicker datepPcker;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtCity;

    @FXML
    private JFXTextField txtId;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPostalCode;

    @FXML
    private JFXTextField txtProvince;

    @FXML
    private JFXTextField txtSalary;

    @FXML
    private TableView<Customer> tblCustomers;
    CustomerService service = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadTable();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colsalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));

        ObservableList<String> titles = FXCollections.observableArrayList();
        titles.add("Mr");
        titles.add("Miss");
        titles.add("Ms");
        cmbTitle.setItems(titles);

        tblCustomers.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            if(newValue!=null) {
                setTextToValues(newValue);
            }
        }));
        loadTable();
    }
    private void setTextToValues(Customer newValue) {
        txtId.setText(newValue.getId());
        cmbTitle.setValue(newValue.getTitle());
        txtName.setText(newValue.getName());
        datepPcker.setValue(newValue.getDob());
        txtAddress.setText(newValue.getAddress());
        txtSalary.setText(String.valueOf(newValue.getSalary()));
        txtCity.setText(newValue.getCity());
        txtProvince.setText(newValue.getProvince());
        txtPostalCode.setText(newValue.getPostalCode());
    }
    public void btmOnClickAdd(ActionEvent event) {
        Customer customer = new Customer(
                txtId.getText(),
                cmbTitle.getValue().toString(),
                txtName.getText(),
                txtAddress.getText(),
                datepPcker.getValue(),
                Double.parseDouble(txtSalary.getText()),
                txtCity.getText(), txtProvince.getText(),
                txtPostalCode.getText()
        );

        if(service.addCustomer(customer)){
            new Alert(Alert.AlertType.INFORMATION,"Customer Added...").show();
        }else {
            new Alert(Alert.AlertType.ERROR,"Customer Not Added :(").show();
        }
    }
    private void loadTable() {
        ObservableList<Customer> customerObservableList =service.getAll();
        tblCustomers.setItems(customerObservableList);
    }
    public void btmOnClickUpdate(ActionEvent event) {
        Customer customer = new Customer(
                txtId.getText(),
                cmbTitle.getValue().toString(),
                txtName.getText(),
                txtAddress.getText(),
                datepPcker.getValue(),
                Double.parseDouble(txtSalary.getText()),
                txtCity.getText(),
                txtProvince.getText(),
                txtPostalCode.getText()
        );

        if(service.updateCustomer(customer)) {
            new Alert(Alert.AlertType.INFORMATION, "Customer Updated...!").show();
            loadTable();
        } else {
            new Alert(Alert.AlertType.ERROR, "Customer Not Updated :(").show();
        }

    }

    public void btmOnClickDelete(ActionEvent event) {
        if (service.deleteCustomer(txtId.getText())){
            new Alert(Alert.AlertType.INFORMATION,"Customer Deleted...!").show();
        }else{
            new Alert(Alert.AlertType.ERROR).show();
        }
    }

    public void btmOnClickSearch(ActionEvent event) {
        String idOrName = txtId.getText().isEmpty() ? txtName.getText() : txtId.getText();

        Customer customer = service.searchCustomer(idOrName);

        if (customer != null) {
            setTextToValues(customer);
        } else {
            new Alert(Alert.AlertType.WARNING, "Customer Not Found :(").show();
        }
    }
}