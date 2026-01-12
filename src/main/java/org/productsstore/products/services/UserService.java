package org.productsstore.products.services;

import org.productsstore.products.models.User;
import org.productsstore.products.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public User getUserDetail(Long id) {
        Optional<User> userOptional = userRepo.findById(id);
        if(userOptional.isEmpty()) {
            System.out.println("NO USER FOUND");
            return null;
        }

        System.out.println(userOptional.get().getEmail());
        return userOptional.get();
    }


}
