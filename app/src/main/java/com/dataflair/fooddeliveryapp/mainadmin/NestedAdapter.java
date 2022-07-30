package com.dataflair.fooddeliveryapp.mainadmin;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflair.fooddeliveryapp.Model.Model;
import com.dataflair.fooddeliveryapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

import de.hdodenhof.circleimageview.CircleImageView;

public class NestedAdapter extends RecyclerView.Adapter<NestedAdapter.NestedViewHolder> {

    private ArrayList<Model> values;

    public NestedAdapter(ArrayList<Model> values) {
        this.values = values;
    }

    @NonNull
    @Override
    public NestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nested_item_food_item,parent,false);
        return new NestedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedViewHolder holder, int position) {

        Model model = values.get(position);

        holder.foodName.setText(model.getItemName());
        holder.foodPrice.setText(model.getItemPrice());
        Picasso.get().load(model.getImageUrl()).into(holder.foodItemImage);

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), " delte buttone pressed",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    class NestedViewHolder extends RecyclerView.ViewHolder{

        CircleImageView foodItemImage;
        TextView foodName,foodPrice;
        ImageView imageViewDelete;

        public NestedViewHolder(@NonNull View itemView) {
            super(itemView);

            foodItemImage = itemView.findViewById(R.id.image_view_food_item_nested_list_main_admin);
            foodName = itemView.findViewById(R.id.food_item_name_main_admin);
            foodPrice = itemView.findViewById(R.id.food_item_price_main_admin);
            imageViewDelete = itemView.findViewById(R.id.delete_food_item_main_delete);
        }
    }
}
