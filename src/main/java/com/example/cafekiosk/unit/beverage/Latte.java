package com.example.cafekiosk.unit.beverage;

public class Latte implements Beverage {
    @Override
    public String getName() {
        return "Latte";
    }

    @Override
    public int getPrice() {
        return 3000;
    }
}
