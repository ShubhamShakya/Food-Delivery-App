package com.dataflair.fooddeliveryapp;

import com.dataflair.fooddeliveryapp.Model.FoodItem;

public interface OnEditDeleteFoodItemListener {

    void onConfirmDelete(int position, FoodItem foodItem);

    void onEditFoodItem(int foodItemPosition);
}
