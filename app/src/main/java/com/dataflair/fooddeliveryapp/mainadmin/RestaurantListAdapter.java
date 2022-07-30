package com.dataflair.fooddeliveryapp.mainadmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflair.fooddeliveryapp.Model.Model;
import com.dataflair.fooddeliveryapp.Model.RestaurantModel;
import com.dataflair.fooddeliveryapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantListHolder> {

    private HashMap<RestaurantModel, ArrayList<Model>> mixArrayList;
    private ArrayList<Model> list;
   public RestaurantListAdapter(HashMap<RestaurantModel, ArrayList<Model>> mixArrayList)
   {
       this.mixArrayList = mixArrayList;
   }




    @NonNull
    @Override
    public RestaurantListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurnat_list_main_admin,parent,false);
        return new RestaurantListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantListHolder holder, int position) {
       Set<RestaurantModel> temp = mixArrayList.keySet();
       RestaurantModel[] modelArray = temp.toArray(new RestaurantModel[0]);

       RestaurantModel restaurantModel = modelArray[position];

       boolean isExpandable = restaurantModel.isExpandable();
       holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        if (isExpandable){
            holder.arrowImageView.setImageResource(R.drawable.arrow_up);
        }else{
            holder.arrowImageView.setImageResource(R.drawable.arrow_down);
        }

       holder.restaurantName.setText(restaurantModel.getRestaurantName());
       holder.restaurantAddress.setText(restaurantModel.getRestaurantAddress());
       holder.restaurantMobileNumber.setText(restaurantModel.getRestaurantMobileNumber());

       list = mixArrayList.get(restaurantModel);

       NestedAdapter nestedAdapter = new NestedAdapter(mixArrayList.get(restaurantModel));
        holder.expandableRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.expandableRecyclerView.setAdapter(nestedAdapter);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantModel.setExpandable(!restaurantModel.isExpandable());
                Toast.makeText(holder.itemView.getContext(), " linear layout pressed",Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });




    }


    @Override
    public int getItemCount() {
        return mixArrayList.size();
    }

    class RestaurantListHolder extends RecyclerView.ViewHolder{

       TextView restaurantName,restaurantAddress,restaurantMobileNumber;
       RelativeLayout expandableLayout;
       ImageView arrowImageView;
       RecyclerView expandableRecyclerView;
       LinearLayout linearLayout;

        public RestaurantListHolder(@NonNull View itemView) {
            super(itemView);

            restaurantName = itemView.findViewById(R.id.restaurant_name_main_admin);
            restaurantAddress = itemView.findViewById(R.id.restaurant_address_main_admin);
            restaurantMobileNumber = itemView.findViewById(R.id.restaurant_mobile_number_main_admin);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            arrowImageView = itemView.findViewById(R.id.arrow_imageview);
            expandableRecyclerView = itemView.findViewById(R.id.child_rv);
            linearLayout = itemView.findViewById(R.id.linear_layout);
        }

    }
}
