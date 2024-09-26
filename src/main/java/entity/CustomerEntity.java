package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class CustomerEntity{

    @Id
    private String id;
    private String  title;
    private String  name;
    private String address;
    private LocalDate dob;
    private Double salary;
    private String city;
    private String province;
    private String postalCode;
}
