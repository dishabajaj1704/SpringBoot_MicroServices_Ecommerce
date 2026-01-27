package com.example.demo.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//Mandatory no args constructor for JPA. It is also added by Lombok @Data but adding explicitly for clarity
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
//@Entity
public class User {

    @Id
    //It will generate unique values for id automatically
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    //JPA relies on a no-argument constructor to create entity instances via reflection.
    //Default no args constructor is need to create instances of entity class during retrieval of data from database
    //Already added by Lombok @Data
    //    public  User() {
//    }



}
