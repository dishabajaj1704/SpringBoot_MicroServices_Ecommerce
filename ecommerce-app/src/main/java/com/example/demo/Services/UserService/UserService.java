package com.example.demo.Services.UserService;


import com.example.demo.Entities.User;
import com.example.demo.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    //private List<User> userList=new ArrayList<>();

    public List<User> getAllUsers() {
        return  userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {

        //Method 1: Without Stream API
//        for(User user:userList){
//            if(user.getId().equals(id)){
//               return user;
//            }
//        }
//        return null;

        //Method 2: With Stream API
//        return userList.stream()
//                .filter(user -> user.getId().equals(id))
//                .findFirst();

        return userRepository.findById(id);
    }

    public void createUser( User user) {
        //user.setId(userList.isEmpty()?1L:userList.get(userList.size()-1).getId()+1);
       userRepository.save(user);
    }
    //boolean - either true or false
    //Boolean - can be null too
    //Optional<Boolean> - can be true,false or null
    //Difference between Optional<Boolean> and Boolean is that Optional<Boolean> explicitly indicates the possibility of absence of a value, while Boolean can be null but doesn't convey that meaning as clearly.
    //Difference between OrElse and OrElseGet -> OrElse always evaluates the argument, even if it's not needed.
    //OrElseGet only evaluates the argument if the Optional is empty.

    public boolean updateUser(Long id,User updatedUser) {
//             return userList.stream()
//              .filter(u->u.getId().equals(id))
//              .findFirst()
//              .map(existingUser -> {
//                    existingUser.setFirstName(updatedUser.getFirstName());
//                    existingUser.setLastName(updatedUser.getLastName());
//                  return true;
//              }).orElse(false);

        return  userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(updatedUser.getFirstName());
                    existingUser.setLastName(updatedUser.getLastName());
                    userRepository.save(existingUser);
                    return true;
                }).orElse(false);
    }
}
