package com.dataflair.fooddeliveryapp.Fragments;

import com.dataflair.fooddeliveryapp.Adapters.MyOrdersAdapter;

public interface OnMarkDeliveredListener {
    void markAsDelivered(String foodItemOrderID);
}
