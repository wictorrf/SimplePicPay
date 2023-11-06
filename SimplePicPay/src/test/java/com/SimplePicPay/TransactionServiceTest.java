package com.SimplePicPay;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import com.SimplePicPay.domain.user.User;
import com.SimplePicPay.dtos.TransactionDTO;
import com.SimplePicPay.repositories.TransactionRepository;
import com.SimplePicPay.services.AuthorizationService;
import com.SimplePicPay.services.NotificationService;
import com.SimplePicPay.services.TransactionService;
import com.SimplePicPay.services.UserService;

public class TransactionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository repository;

    @Mock
    private AuthorizationService authorization;

    @Mock
    private NotificationService notification;

    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should create transaction successfully when everything is ok")
    void createTransactionCase1() throws Exception {
        User sender = new User(1L, "Joao", "Pedro", "99900088877", "joao@email.com", "joao12345", new BigDecimal(1000),
                com.SimplePicPay.domain.user.UserType.COMMON);
        User reciver = new User(2L, "Paulo", "Marques", "99933388877", "paulo@email.com", "paulo12345",
                new BigDecimal(1000), com.SimplePicPay.domain.user.UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(reciver);
        when(authorization.authorizeTransaction(any(), any())).thenReturn(true);

        TransactionDTO request = new TransactionDTO(new BigDecimal(10), 1l, 2L);
        transactionService.createTransaction(request);

        verify(repository, times(1)).save(any());

        sender.setBalance(new BigDecimal(990));
        verify(userService, times(1)).saveUser(sender);

        reciver.setBalance(new BigDecimal(1010));
        verify(userService, times(1)).saveUser(reciver);
    }

    @Test
    @DisplayName("should throw exception when transaction is not allowed")
    void createTransactionCase2() throws Exception {
        User sender = new User(1L, "Joao", "Pedro", "99900088877", "joao@email.com", "joao12345", new BigDecimal(1000),
                com.SimplePicPay.domain.user.UserType.COMMON);
        User reciver = new User(2L, "Paulo", "Marques", "99933388877", "paulo@email.com", "paulo12345",
                new BigDecimal(1000), com.SimplePicPay.domain.user.UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(reciver);
        when(authorization.authorizeTransaction(any(), any())).thenReturn(false);
        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            TransactionDTO request = new TransactionDTO(new BigDecimal(10), 1l, 2L);
            transactionService.createTransaction(request);
        });

        Assertions.assertEquals("Transação não autorizada!", thrown.getMessage());
    }
}
