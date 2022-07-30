package com.dataflair.fooddeliveryapp;

import com.dataflair.fooddeliveryapp.Model.Model;
import com.dataflair.fooddeliveryapp.Model.RestaurantModel;
import com.dataflair.fooddeliveryapp.mainadmin.restaurant_database.ModelRestaurant;

public interface OnConfirmOrderListener {
     void onConfirmOrder(Model model);

     void onConfirmRestaurantDetails(ModelRestaurant modelRestaurant);
}
