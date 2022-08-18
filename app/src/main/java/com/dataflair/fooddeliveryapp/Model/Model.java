package com.dataflair.fooddeliveryapp.Model;

public class Model {

    String displayName, cityName, profilepic, pinCode, phoneNumber, address,userId,email;
    String hotelLocation, imageUrl, itemName, itemPrice,foodItemID,foodItemOrderId;
    private String restaurantName;
    private String restaurantOwnerName;
    private String restaurantAddress;
    private String restaurantMobileNumber;
    private String restaurantEmail;
    private String pinCodeRestaurant;

    public Model() {
    }

    public Model(String name, String cityName, String profilepic, String pinCode, String phoneNumber, String address, String userId, String hotelLocation, String imageUrl, String itemName, String itemPrice) {
        this.displayName = name;
        this.cityName = cityName;
        this.profilepic = profilepic;
        this.pinCode = pinCode;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.userId = userId;
        this.hotelLocation = hotelLocation;
        this.imageUrl = imageUrl;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public String getFoodItemOrderId() {
        return foodItemOrderId;
    }

    public void setFoodItemOrderId(String foodItemOrderId) {
        this.foodItemOrderId = foodItemOrderId;
    }

    public String getFoodItemId() {
        return foodItemID;
    }

    public void setFoodItemId(String foodItemId) {
        this.foodItemID = foodItemID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String name) {
        this.displayName = name;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHotelLocation() {
        return hotelLocation;
    }

    public void setHotelLocation(String hotelLocation) {
        this.hotelLocation = hotelLocation;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getPinCodeRestaurant() {
        return pinCodeRestaurant;
    }

    public void setPinCodeRestaurant(String pinCodeRestaurant) {
        this.pinCodeRestaurant = pinCodeRestaurant;
    }
}