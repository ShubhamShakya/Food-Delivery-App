package com.dataflair.fooddeliveryapp.mainadmin.restaurant_database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
interface RestaurantDAO {
    @Insert(onConflict = REPLACE)
    void addRestaurant(ModelRestaurant modelRestaurant);

    @Delete
    void deleteRestaurant(ModelRestaurant modelRestaurant);

    @Query("Select * from `Restaurant Information`")
    public List<ModelRestaurant> getAllRestaurantInformation();

    @Query("Select * from `Restaurant Information` where restaurantMobileNumber = :restaurantMobileNumber")
    public ModelRestaurant getRestaurantInformation(String restaurantMobileNumber);

    @Query("Delete from `Restaurant Information` where restaurantMobileNumber = :mobileNumber")
    void deleteRestaurant(String mobileNumber);



}
