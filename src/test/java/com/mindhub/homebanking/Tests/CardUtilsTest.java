//package com.mindhub.homebanking.Tests;
//
//import com.mindhub.homebanking.Utils.CardUtils;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//
//@SpringBootTest
//public class CardUtilsTest {
//    @Test
//    public void cardNumberIsCreated(){
//        String cardNumber = CardUtils.getCards();
//        assertThat(cardNumber,is(not(emptyOrNullString())));
//    }
//
//    @Test
//    public void cardCVV(){
//        int cardCVV = CardUtils.aleatoryNumberCvv();
//        assertThat(cardCVV,is(greaterThanOrEqualTo(100)));
//        assertThat(cardCVV,is(lessThanOrEqualTo(999)));
//    }
//
//}
