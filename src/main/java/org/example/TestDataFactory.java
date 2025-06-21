package org.example;

import java.util.Arrays;
import java.util.List;

public class TestDataFactory {

    // Константы для данных курьера
    public static final String STANDARD_USERNAME = "standard_username";
    public static final String STANDARD_PASS = "standard_password";
    public static final String STANDARD_DELIVERY_PERSON_NAME = "courier_test";

    // Константы для данных заказа
    public static final String CUSTOMER_NAME = "Анна Петрова";
    public static final String CUSTOMER_SURNAME = "Иванова";
    public static final String DELIVERY_ADDRESS = "проспект Мира, 15";
    public static final String NEAREST_STATION = "3";
    public static final String CONTACT_NUMBER = "+79111234567";
    public static final String ORDER_NOTES = "Тестовый заказ";

    private static CreatingCourier standardDeliveryPerson;
    private static LoginCourier standardAuthCredentials;

    public static LoginCourier getStandardAuthCredentials() {
        if (standardAuthCredentials == null) {
            standardAuthCredentials = new LoginCourier (STANDARD_USERNAME, STANDARD_PASS);
        }
        return standardAuthCredentials;
    }

    public static CreatingCourier getStandardDeliveryPerson() {
        if (standardDeliveryPerson == null) {
            standardDeliveryPerson = new CreatingCourier(
                    STANDARD_USERNAME,
                    STANDARD_PASS,
                    STANDARD_DELIVERY_PERSON_NAME
            );
        }
        return standardDeliveryPerson;
    }

    public static CreatingOrder createNewOrder(List<String> colors) {
        CreatingOrder order = new CreatingOrder();
        order.setFirstName(CUSTOMER_NAME);
        order.setLastName(CUSTOMER_SURNAME);
        order.setAddress(DELIVERY_ADDRESS);
        order.setMetroStation(NEAREST_STATION);
        order.setPhone(CONTACT_NUMBER);
        order.setRentTime("5"); // дней аренды
        order.setDeliveryDate("2025-07-01");
        order.setComment(ORDER_NOTES);
        order.setColor(colors);
        return order;
    }


    //Создаем новый заказ с цветом по умолчанию

    public static CreatingOrder createNewOrder() {
        return createNewOrder(Arrays.asList("SILVER"));
    }


     //Создаем новый заказ без указания цвета
    public static CreatingOrder createColorlessOrder() {
        return createNewOrder(null);
    }
}
