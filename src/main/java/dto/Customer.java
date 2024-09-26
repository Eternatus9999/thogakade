package dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor

public class Customer {
    private String id;
    private String  title;
    private String  name;
    private String address;
    private LocalDate dob;
    private Double salary;
    private String city;
    private String province;
    private String postalCode;


    public Customer(String id, String title, String name, String address, LocalDate dob, Double salary, String city, String province, String postalCode) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.address = address;
        this.dob = dob;
        this.salary = salary;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
    }
}
