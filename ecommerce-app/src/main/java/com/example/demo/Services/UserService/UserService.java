package com.example.demo.Services.UserService;


import com.example.demo.DTOS.AddressDTO;
import com.example.demo.DTOS.UserRequest;
import com.example.demo.DTOS.UserResponse;
import com.example.demo.Entities.Address;
import com.example.demo.Entities.User;
import com.example.demo.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    //private List<User> userList=new ArrayList<>();

    public List<UserResponse> getAllUsers() {
         List<User> userList=userRepository.findAll();
         return  userRepository.findAll().stream()
                 .map(this::mapUserToUserResponse)
                 .toList();
    }

    public Optional<UserResponse> getUserById(Long id) {

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

        return userRepository.findById(id)
                .map(this::mapUserToUserResponse);
    }
    public void createUser( UserRequest userRequest) {
        //user.setId(userList.isEmpty()?1L:userList.get(userList.size()-1).getId()+1);
        User user = new User();
        updateUserFromRequest(user,userRequest);
       userRepository.save(user);
    }

    //boolean - either true or false
    //Boolean - can be null too
    //Optional<Boolean> - can be true,false or null
    //Difference between Optional<Boolean> and Boolean is that Optional<Boolean> explicitly indicates the possibility of absence of a value, while Boolean can be null but doesn't convey that meaning as clearly.
    //Difference between OrElse and OrElseGet -> OrElse always evaluates the argument, even if it's not needed.
    //OrElseGet only evaluates the argument if the Optional is empty.

    public boolean updateUser(Long id,UserRequest updatedUserRequest) {
//             return userList.stream()
//              .filter(u->u.getId().equals(id))
//              .findFirst()
//              .map(existingUser -> {
//                    existingUser.setFirstName(updatedUser.getFirstName());
//                    existingUser.setLastName(updatedUser.getLastName());
//                  return true;
//              }).orElse(false);

//        return  userRepository.findById(id)
//                .map(existingUser -> {
//                    existingUser.setFirstName(updatedUser.getFirstName());
//                    existingUser.setLastName(updatedUser.getLastName());
//                    userRepository.save(existingUser);
//                    return true;
//                }).orElse(false);

        return  userRepository.findById(id)
                .map(existingUser -> {
                    updateUserFromRequest(existingUser,updatedUserRequest);
                    userRepository.save(existingUser);
                    return true;
                }).orElse(false);
    }

    private UserResponse mapUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setRole(user.getRole().name());
        // Map Address to AddressDTO
        if (user.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipCode(user.getAddress().getZipCode());
            userResponse.setAddress(addressDTO);
        }
        return userResponse;
    }

    private void updateUserFromRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());

        // Set Address from AddressDTO
        if (userRequest.getAddress() != null) {
            AddressDTO addressDTO = userRequest.getAddress();
            Address address = new Address();
            address.setStreet(addressDTO.getStreet());
            address.setCity(addressDTO.getCity());
            address.setState(addressDTO.getState());
            address.setCountry(addressDTO.getCountry());
            address.setZipCode(addressDTO.getZipCode());
            user.setAddress(address);
        }
    }
}
