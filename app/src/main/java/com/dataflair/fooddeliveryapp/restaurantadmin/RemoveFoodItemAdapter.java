package com.dataflair.fooddeliveryapp.restaurantadmin;

import static com.dataflair.fooddeliveryapp.restaurantadmin.RestaurantAdminDashboard.foodItemList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflair.fooddeliveryapp.Model.FoodItem;
import com.dataflair.fooddeliveryapp.Model.Model;
import com.dataflair.fooddeliveryapp.OnEditDeleteFoodItemListener;
import com.dataflair.fooddeliveryapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RemoveFoodItemAdapter extends FirebaseRecyclerAdapter<Model,RemoveFoodItemAdapter.MyViewHolder> {


    private OnEditDeleteFoodItemListener listener;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RemoveFoodItemAdapter(@NonNull FirebaseRecyclerOptions<Model> options, OnEditDeleteFoodItemListener listener) {
        super(options);
        this.listener = listener;
        foodItemList = new ArrayList<>();
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Model model) {

        Picasso.get().load(model.getImageUrl()).into(holder.foodImage);
        holder.foodItemPrice.setText(model.getItemPrice());
        holder.foodItemName.setText(model.getItemName());
        FoodItem foodItem = new FoodItem(model.getImageUrl(),model.getItemName(), model.getItemPrice(), model.getFoodItemId());
        foodItemList.add(foodItem);
        holder.deleteFoodItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onConfirmDelete(holder.getAdapterPosition(),foodItem);
            }
        });

        holder.editFoodItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestaurantAdminDashboard.isUpdateFoodItem = true;
                listener.onEditFoodItem(holder.getAdapterPosition());
            }
        });


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.delete_food_item_restaurant_admin,parent,false);
        return new MyViewHolder(v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView foodImage;
        TextView foodItemName,foodItemPrice;
        ImageView deleteFoodItemButton,editFoodItemButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImage = itemView.findViewById(R.id.foodItemImageViewRemove);
            foodItemName = itemView.findViewById(R.id.foodNameTVDeleteFoodItem);
            foodItemPrice = itemView.findViewById(R.id.foodPriceTVDeleteFoodItem);
            deleteFoodItemButton = itemView.findViewById(R.id.deleteFoodItem);
            editFoodItemButton = itemView.findViewById(R.id.editFoodItem);
        }
    }
}
