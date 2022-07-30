package com.dataflair.fooddeliveryapp.mainadmin.restaurant_database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Restaurant Information")
public class ModelRestaurant {
    /*@PrimaryKey(autoGenerate = true )
    @ColumnInfo(name = "restaurantId")
    private int restaurantId;*/

    @ColumnInfo(name = "restaurantName")
    private String restaurantName;

    @ColumnInfo(name = "restaurantOwnerName")
    private String restaurantOwnerName;

    @ColumnInfo (name = "restaurantAddress")
    private String restaurantAddress;

    @ColumnInfo(name = "restaurantEmail")
    private String restaurantEmail;

    @PrimaryKey
    @NonNull
    @ColumnInfo (name = "restaurantMobileNumber")
    private String restaurantMobileNumber;

    @ColumnInfo(name = "restaurantPinCode")
    private String restaurantPinCode;

    @ColumnInfo(name = "restaurantPassword")
    private String restaurantPassword;

    public String getRestaurantPassword() {
        return restaurantPassword;
    }

    public void setRestaurantPassword(String restaurantPassword) {
        this.restaurantPassword = restaurantPassword;
    }


    public ModelRestaurant(String restaurantName, String restaurantOwnerName, String restaurantAddress,
                           String restaurantEmail, @NonNull String restaurantMobileNumber, String restaurantPinCode,String restaurantPassword){
        this.restaurantName = restaurantName;
        this.restaurantOwnerName = restaurantOwnerName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantEmail = restaurantEmail;
        this.restaurantMobileNumber = restaurantMobileNumber;
        this.restaurantPinCode = restaurantPinCode;
        this.restaurantPassword = restaurantPassword;
    }

    public ModelRestaurant(){

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

    public String getRestaurantEmail() {
        return restaurantEmail;
    }

    public void setRestaurantEmail(String restaurantEmail) {
        this.restaurantEmail = restaurantEmail;
    }

    public String getRestaurantMobileNumber() {
        return restaurantMobileNumber;
    }

    public void setRestaurantMobileNumber(String restaurantMobileNumber) {
        this.restaurantMobileNumber = restaurantMobileNumber;
    }

    public String getRestaurantPinCode() {
        return restaurantPinCode;
    }

    public void setRestaurantPinCode(String restaurantPinCode) {
        this.restaurantPinCode = restaurantPinCode;
    }
}
