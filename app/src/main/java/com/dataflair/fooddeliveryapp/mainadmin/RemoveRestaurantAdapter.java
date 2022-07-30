package com.dataflair.fooddeliveryapp.mainadmin;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflair.fooddeliveryapp.Model.RestaurantModel;

import com.dataflair.fooddeliveryapp.R;


import java.util.ArrayList;


public class RemoveRestaurantAdapter extends RecyclerView.Adapter<RemoveRestaurantAdapter.MyViewHolder> {

    ArrayList<RestaurantModel> restaurantModelList;
    ArrayList<Long> countFoodItems;
    DeleteRestaurantMainAdmin.OnConfirmDelete onConfirmDeleteListener;



    RemoveRestaurantAdapter(ArrayList<RestaurantModel> restaurantModelList, ArrayList<Long> countFoodItems, DeleteRestaurantMainAdmin.OnConfirmDelete onConfirmDeleteListener){
        this.restaurantModelList = restaurantModelList;
        this.countFoodItems = countFoodItems;
        this.onConfirmDeleteListener = onConfirmDeleteListener;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.delete_restaurant_item_main_admin,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RestaurantModel model = restaurantModelList.get(position);

        holder.restaurantNameTV.setText(model.getRestaurantName());
        holder.restaurantOwnerNameTV.setText(model.getRestaurantOwnerName());
        holder.restaurantMobileNumberTV.setText(model.getRestaurantMobileNumber());

        holder.restaurantFoodItemCountTV.setText(countFoodItems.get(position)+"");

        holder.deleteButtonIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), " Delete Restaurant button",Toast.LENGTH_SHORT).show();
                onConfirmDeleteListener.onConfirm(model.getRestaurantMobileNumber(), holder.getAdapterPosition());

            }
        });



    }

    @Override
    public int getItemCount() {
        return restaurantModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView restaurantNameTV,restaurantOwnerNameTV,restaurantMobileNumberTV,restaurantFoodItemCountTV;
        ImageView deleteButtonIV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            restaurantNameTV = itemView.findViewById(R.id.restaurant_name_delete_restaurant_main_admin);
            restaurantOwnerNameTV = itemView.findViewById(R.id.restaurant_owner_name_delete_restaurant_main_admin);
            restaurantMobileNumberTV = itemView.findViewById(R.id.restaurant_mobile_number_delete_main_admin);
            restaurantFoodItemCountTV = itemView.findViewById(R.id.restaurant_food_item_count_delete_main_admin);

            deleteButtonIV = itemView.findViewById(R.id.delete_button_delete_main_admin);
        }

    }
}
