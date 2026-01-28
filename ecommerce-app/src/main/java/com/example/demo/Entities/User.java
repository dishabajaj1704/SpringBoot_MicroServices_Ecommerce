package com.example.demo.Entities;


import com.example.demo.Enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
//Mandatory no args constructor for JPA. It is also added by Lombok @Data but adding explicitly for clarity
@NoArgsConstructor
//@AllArgsConstructor
@Entity(name = "users")
//@Entity
public class User {

    @Id
    //It will generate unique values for id automatically
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    //Here role variable will intialized as null because of @AllArgsConstructor. So we are removing @AllArgsConstructor and adding default value here
    //How @AllArgsConstructor causes role to be null?
    //Because Lombok's @AllArgsConstructor (or any explicit all-args constructor) assigns the incoming parameter values after field initializers run, a null passed for role will overwrite the default. When Jackson deserializes using that constructor it will pass null for missing fields, so the field initializer doesn't help.
    private UserRole role=UserRole.CUSTOMER;
    //JPA relies on a no-argument constructor to create entity instances via reflection.
    //Default no args constructor is need to create instances of entity class during retrieval of data from database
    //Already added by Lombok @Data
    //    public  User() {
//    }

    //CascadeType.ALL It means all JPA operations on the parent are applied to the child:
    //orphanRemoval = true means if the parent entity is removed or the reference to the child entity is removed, the child entity will also be deleted from the database.
    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private  LocalDateTime updatedAt;
}
