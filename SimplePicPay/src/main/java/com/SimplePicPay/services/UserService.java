package com.SimplePicPay.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SimplePicPay.domain.user.User;
import com.SimplePicPay.domain.user.UserType;
import com.SimplePicPay.dtos.UserDTO;
import com.SimplePicPay.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception{
        if(sender.getUserType() == UserType.MARCHANT){
            throw new Exception("Usiario do tipo logista não está autorizado a fazer transação!");
        }
        if(sender.getBalance().compareTo(amount) < 0){
            throw new Exception("Saldo insuficiente!");
        }
    }

    public User findUserById(Long id) throws Exception{
        return this.repository.findUserById(id).orElseThrow(() -> new Exception());
    }

    public User createUser(UserDTO data){
        User newUser = new User(data);
        this.saveUser(newUser);
        return newUser;
    }

    public List<User> getAllUsers(){
        return this.repository.findAll();
    }

    public void saveUser(User user){
        this.repository.save(user);
    }
}
