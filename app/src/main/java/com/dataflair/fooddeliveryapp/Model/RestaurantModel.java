package com.dataflair.fooddeliveryapp.Model;

public class RestaurantModel {
    private String restaurantName;
    private String restaurantOwnerName;
    private String restaurantAddress;
    private String restaurantMobileNumber;
    private String restaurantEmail;
    private String restaurantPinCode;
    private boolean isExpandable = false;

    public boolean isExpandable() {
        return isExpandable;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantOwnerName() {
        return restaurantOwnerName;
    }

    public void setRestaurantOwnerName(String restaurantOwnerName) {
        this.restaurantOwnerName = restaurantOwnerName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public String getRestaurantMobileNumber() {
        return restaurantMobileNumber;
    }

    public void setRestaurantMobileNumber(String restaurantMobileNumber) {
        this.restaurantMobileNumber = restaurantMobileNumber;
    }

    public String getRestaurantEmail() {
        return restaurantEmail;
    }

    public void setRestaurantEmail(String restaurantEmail) {
        this.restaurantEmail = restaurantEmail;
    }

    public String getRestaurantPinCode() {
        return restaurantPinCode;
    }

    public void setRestaurantPinCode(String restaurantPinCode) {
        this.restaurantPinCode = restaurantPinCode;
    }
}
