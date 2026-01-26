package com.example.demo.Controllers;

import com.example.demo.Entities.User;
import com.example.demo.Services.UserService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
@RequiredArgsConstructor  //generates a constructor with required arguments (final fields)
@RestController
@RequestMapping("/api/users")  //Define base URL for all endpoints in this controller
public class UserController {

//Method 1: Using @Autowired
//    @Autowired
//    UserService userService;
//Method 2: Using @RequiredArgsConstructor from Lombok
    private final UserService userService;
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        //We were getting status code as 200 even if there were no users present
        //To give your own status code you can use ResponseEntity
       // return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);

        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {

//        User user = userService.getUserById(id);
//        if(user==null) {
//            //Why build and not body(null)? -> to avoid sending a body with null value
//            //build() creates a response entity with no body
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//            return ResponseEntity.ok(user);

        /*Yes. userService.getUserById(id) returns an Optional<User>.
        map runs only when a User is present and transforms that User into a ResponseEntity<User>.
        The result is an Optional<ResponseEntity<User>>;
        orElseGet supplies the NOT_FOUND response when the optional is empty
         */
        return  userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {

        userService.createUser(user);
        return ResponseEntity.ok("User created successfully");

    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user) {

       boolean updated= userService.updateUser(id,user);
       if(updated){
              return ResponseEntity.ok("User updated successfully");
       }
        return ResponseEntity.notFound().build();

    }
}
