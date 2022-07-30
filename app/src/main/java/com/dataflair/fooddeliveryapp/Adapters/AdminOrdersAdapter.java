package com.dataflair.fooddeliveryapp.Adapters;

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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class AdminOrdersAdapter extends FirebaseRecyclerAdapter<Model, AdminOrdersAdapter.Viewholder> {


    public AdminOrdersAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {

        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull AdminOrdersAdapter.Viewholder holder, int position, @NonNull Model model) {

        //Getting the data from firebase using Model class and setting to TextView
        holder.foodTitle.setText(model.getItemName());
        holder.foodPrice.setText(model.getItemPrice());
        holder.hostelLocation.setText(model.getHotelLocation());
        Picasso.get().load(model.getImageUrl()).into(holder.imageView);

        holder.userName.setText(model.getDisplayName());
        holder.userPhoneNumber.setText(model.getPhoneNumber());
        holder.userCity.setText(model.getCityName());
        holder.userAddress.setText(model.getAddress());
        holder.userPinCode.setText(model.getPinCode());

        String userId = model.getUserId();


        //Implementing OnClickListener to delete data from the fireabase
        holder.completeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Getting the current user id
                String userId = GoogleSignIn.getLastSignedInAccount(view.getContext()).getId();
                //Setting the path of the database
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("totalOrders");
                //Getting the parent node of the values
                reference.orderByChild("name").equalTo(model.getDisplayName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String key = ds.getKey();

                            //Deleting the values from the firebase
                            reference.child(key).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("myOrders").child(userId).child(key).removeValue();

                            //Showing the toast to the user
                            Toast.makeText(holder.foodPrice.getContext(), "Order summited Successfully", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });


            }
        });


    }


    @NonNull
    @Override
    public AdminOrdersAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //the data objects are inflated into the xml file single_data_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_orders_file, parent, false);
        return new AdminOrdersAdapter.Viewholder(view);

    }

    //we need view holder to hold each objet form recyclerview and to show it in recyclerview
    class Viewholder extends RecyclerView.ViewHolder {


        TextView foodTitle, foodPrice, hostelLocation, userName, userPhoneNumber, userCity, userAddress, userPinCode;
        Button completeOrderButton;
        ImageView imageView;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            //Assigning the address of the Android Material to show the data and Perform actions
            foodTitle = (TextView) itemView.findViewById(R.id.FoodName);
            foodPrice = (TextView) itemView.findViewById(R.id.FoodPrice);
            hostelLocation = (TextView) itemView.findViewById(R.id.HotelLocation);

            imageView = (ImageView) itemView.findViewById(R.id.FoodImage);
            completeOrderButton = (Button) itemView.findViewById(R.id.CompleteOrderBtn);

            userName = (TextView) itemView.findViewById(R.id.UserName);
            userPhoneNumber = (TextView) itemView.findViewById(R.id.UserPhoneNumber);
            userCity = (TextView) itemView.findViewById(R.id.UserCity);
            userAddress = (TextView) itemView.findViewById(R.id.UserAddress);
            userPinCode = (TextView) itemView.findViewById(R.id.UserPinCode);


        }
    }


}


