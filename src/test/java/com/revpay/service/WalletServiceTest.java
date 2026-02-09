package com.revpay.service;

import com.revpay.model.SecurityQuestion;
import com.revpay.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletServiceTest {

    private WalletService walletService;
    private AuthService authService;
    private int userId;

    @BeforeEach
    void setup() throws Exception {

        walletService = new WalletService();
        authService = new AuthService();

        User user = new User();
        user.setFullName("Wallet Test User");
        user.setEmail("wallet_" + System.currentTimeMillis() + "@mail.com");
        user.setPhone("9" + (int)(Math.random() * 1000000000));
        user.setUserType("PERSONAL");

        SecurityQuestion sq = new SecurityQuestion();
        sq.setQuestion("Fav color?");
        sq.setAnswerHash("blue");

        // ✅ Creates USER + WALLET (IMPORTANT)
        userId = authService.registerUser(
                user,
                "password123",
                "1234",
                sq,
                null   // personal user
        );
    }

    /* ================= TEST 1 ================= */
    @Test
    void testAddMoneySuccess() throws Exception {

        walletService.addMoney(userId, 500);

        double balance = walletService.getWallet(userId).getBalance();

        assertEquals(500, balance);
    }

    /* ================= TEST 2 ================= */
    @Test
    void testAddMoneyInvalidAmount() {

        Exception ex = assertThrows(
                Exception.class,
                () -> walletService.addMoney(userId, -100)
        );

        assertEquals("Amount must be greater than zero.", ex.getMessage());
    }

    /* ================= TEST 3 ================= */
    @Test
    void testGetWalletBalanceInitiallyZero() throws Exception {

        double balance = walletService.getWallet(userId).getBalance();

        assertEquals(0, balance);
    }

    /* ================= TEST 4 ================= */
    @Test
    void testWalletExistsAfterRegistration() throws Exception {

        assertNotNull(walletService.getWallet(userId));
    }
}
