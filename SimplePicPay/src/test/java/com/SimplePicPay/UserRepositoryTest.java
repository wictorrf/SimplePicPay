package com.SimplePicPay;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import com.SimplePicPay.domain.user.User;
import com.SimplePicPay.domain.user.UserType;
import com.SimplePicPay.dtos.UserDTO;
import com.SimplePicPay.repositories.UserRepository;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Should return user successfully from db")
    void findUserByDocumentSuccess() {
        String document = "79089976544";
        UserDTO data = new UserDTO("Wictor", "Rafael", document, new BigDecimal(10), "wictor@email.com", "wictor12345",
                UserType.COMMON);
        this.createUser(data);
        Optional<User> foundedUser = this.userRepository.findUserByDocument(document);
        assertThat(foundedUser.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get user from db when user not exists")
    void findUserByDocumentCase2() {
        String document = "79089976544";
        Optional<User> foundedUser = this.userRepository.findUserByDocument(document);
        assertThat(foundedUser.isEmpty()).isTrue();
    }

    private User createUser(UserDTO data) {
        User newUser = new User(data);
        this.entityManager.persist(newUser);
        return newUser;
    }
}
