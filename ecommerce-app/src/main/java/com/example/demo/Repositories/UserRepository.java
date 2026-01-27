package com.example.demo.Repositories;

import com.example.demo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//JpaRepository provides JPA related methods such as saving, deleting, and finding entities
//CrudRepository provides CRUD functions
//Here User is the entity type and Long is the type of the entity's primary identifier
@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
}
