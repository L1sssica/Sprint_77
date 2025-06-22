package org.example;

import java.util.List;

public class CreatingOrder {
    // Поля класса для создания заказа
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String[] color;
    private String comment;

    // конструктор класса
    public CreatingOrder(String firstName, String lastName, String address, String metroStation,
                       String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address= address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }
    public CreatingOrder() {
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setMetroStation(String metroStation) {
        this.metroStation = metroStation;
    }

    public String getMetroStation() {
        return metroStation;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setRentTime(String rentTime) {
        this.rentTime = Integer.parseInt(rentTime);
    }

    public int getRentTime() {
        return rentTime;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setColor(List<String> colors) {
        this.color = colors != null ? colors.toArray(new String[0]) : null;
    }

    public String[] getColor() {
        return color;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}
