package com.example.cafekiosk.unit;

import com.example.cafekiosk.unit.beverage.Americano;
import com.example.cafekiosk.unit.beverage.Latte;
import com.example.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

public class CafeKioskRunner {

    public static void main(String[] args) {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());
        System.out.println("Add Americano");

        cafeKiosk.add(new Latte());
        System.out.println("Add Latte");

        int totalPrice = cafeKiosk.calculateTotalPrice();
        System.out.println("Total Price: " + totalPrice);

        Order order = cafeKiosk.createOrder(LocalDateTime.now());
    }

}
