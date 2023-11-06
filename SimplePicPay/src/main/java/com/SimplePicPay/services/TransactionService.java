package com.SimplePicPay.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SimplePicPay.domain.transaction.Transaction;
import com.SimplePicPay.domain.user.User;
import com.SimplePicPay.dtos.TransactionDTO;
import com.SimplePicPay.repositories.TransactionRepository;

@Service
public class TransactionService {
  @Autowired
  private UserService userService;

  @Autowired
  private TransactionRepository repository;

  @Autowired
  private AuthorizationService authorization;

  @Autowired
  private NotificationService notification;

  public Transaction createTransaction(TransactionDTO transaction) throws Exception {
    User sender = this.userService.findUserById(transaction.senderId());
    User reciver = this.userService.findUserById(transaction.reciverId());

    userService.validateTransaction(sender, transaction.value());

    boolean isAuthorized = this.authorization.authorizeTransaction(sender, transaction.value());
    if (!isAuthorized) {
      throw new Exception("Transação não autorizada!");
    }

    Transaction newtransaction = new Transaction();
    newtransaction.setAmount(newtransaction.value());
    newtransaction.setSender(sender);
    newtransaction.setReciver(reciver);
    newtransaction.setTimestamp(LocalDateTime.now());

    sender.setBalance(sender.getBalance().subtract(transaction.value()));
    reciver.setBalance(reciver.getBalance().add(transaction.value()));

    repository.save(newtransaction);
    userService.saveUser(sender);
    userService.saveUser(reciver);

    this.notification.sendNotification(sender, "Transação realizada com sucesso!");
    this.notification.sendNotification(reciver, "Transação recebida com sucesso!");

    return newtransaction;
  }

}
