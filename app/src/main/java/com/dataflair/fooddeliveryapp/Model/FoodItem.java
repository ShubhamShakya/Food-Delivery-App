package com.dataflair.fooddeliveryapp.Model;

public class FoodItem {
    private String itemName;
    private String itemPrice;
    private String foodItemImgUrl;
    private String foodItemId;

    public String getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(String foodItemId) {
        this.foodItemId = foodItemId;
    }

    public FoodItem(String foodItemImgUrl, String itemName, String itemPrice, String foodItemId) {
        this.foodItemImgUrl = foodItemImgUrl;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.foodItemId = foodItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getFoodItemImgUrl() {
        return foodItemImgUrl;
    }

    public void setFoodItemImgUrl(String foodItemImgUrl) {
        this.foodItemImgUrl = foodItemImgUrl;
    }
}
