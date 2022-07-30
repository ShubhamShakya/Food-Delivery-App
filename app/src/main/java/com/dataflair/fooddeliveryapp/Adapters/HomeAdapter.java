package com.dataflair.fooddeliveryapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflair.fooddeliveryapp.Model.Model;
import com.dataflair.fooddeliveryapp.OnConfirmOrderListener;
import com.dataflair.fooddeliveryapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.Viewholder> {

    private OnConfirmOrderListener onCancelOrderListener;
    private ArrayList<Model> modelArrayList;
    public HomeAdapter(ArrayList<Model> modelArrayList, OnConfirmOrderListener listener) {

        this.modelArrayList = modelArrayList;
        onCancelOrderListener = listener;

    }

   /* @Override
    protected void onBindViewHolder(@NonNull HomeAdapter.Viewholder holder, int position, @NonNull Model model) {

        //Getting the data from the database using the Model class and setting the data
        holder.foodTitle.setText(model.getItemName());
        holder.foodPrice.setText(model.getItemPrice());
        holder.hostelLocation.setText(model.getHotelLocation());
        Picasso.get().load(model.getImageUrl()).into(holder.imageView);

        String hotelLocation = model.getHotelLocation();
        String foodPrice = model.getItemPrice();
        String foodName = model.getItemName();
        String foodImage = model.getImageUrl();

        //Implementing the Onclick Listener to add the order and user details to database
        holder.orderNowBtn.setOnClickListener(view -> {

            final Model[] userDataModel = new Model[1];

            //Getting the user email from google sign in
           *//* //String userEmail = Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(view.getContext())).getEmail();

            //used as a key for database
            assert userEmail != null;
            String emailAsFirebaseKey = userEmail.substring(0,userEmail.lastIndexOf('@'));

            //Database Path to add the details
            FirebaseDatabase.getInstance().getReference().child("users").child(emailAsFirebaseKey)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                             userDataModel[0] = snapshot.getValue(Model.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });*//*


            onCancelOrderListener.onConfirmOrder(model);

           *//* //Generating the unique key
            String key = FirebaseDatabase.getInstance().getReference().child("myOrders").push().getKey().toString();

            //Getting the user email from google sign in
            String userEmail = Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(view.getContext())).getEmail();

            //used as a key for database
            assert userEmail != null;
            String emailAsFirebaseKey = userEmail.substring(0,userEmail.lastIndexOf('@'));



            //Database Path to add the details
            FirebaseDatabase.getInstance().getReference().child("users").child(emailAsFirebaseKey)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            Model model = snapshot.getValue(Model.class);

                            //Hash Map to store the values
                            HashMap<String,String> orderDetails = new HashMap();

                            //Adding the details to hashmap
                            orderDetails.put("name", model.getDisplayName());
                            orderDetails.put("phoneNumber", model.getPhoneNumber());
                            orderDetails.put("address", model.getAddress());
                            orderDetails.put("cityName", model.getCityName());
                            orderDetails.put("pinCode", model.getPinCode());

                            orderDetails.put("hotelLocation", hotelLocation);
                            orderDetails.put("itemPrice", foodPrice);
                            orderDetails.put("itemName", foodName);
                            orderDetails.put("imageUrl", foodImage);
                            //orderDetails.put("userId", userId);


                            //Adding the hash map to the database
                            FirebaseDatabase.getInstance().getReference().child("myOrders").child(emailAsFirebaseKey).child(key)
                                    .setValue(orderDetails)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                FirebaseDatabase.getInstance().getReference().child("totalOrders").child(key)
                                                        .setValue(orderDetails)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    //Showing the toast to user for confirmation
                                                                    Toast.makeText(view.getContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                            }

                                        }
                                    });


                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
*//*

        });
    }*/


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_food_item,parent,false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        Model model = modelArrayList.get(position);
        //Getting the data from the database using the Model class and setting the data
        holder.foodTitle.setText(model.getItemName());
        holder.foodPrice.setText(model.getItemPrice());
        holder.hostelLocation.setText(model.getRestaurantName() + ", " +model.getHotelLocation());
        Picasso.get().load(model.getImageUrl()).into(holder.imageView);

        //Implementing the Onclick Listener to add the order and user details to database
        holder.orderNowBtn.setOnClickListener(view -> {
            final Model[] userDataModel = new Model[1];
            onCancelOrderListener.onConfirmOrder(model);

        });




        }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    //we need view holder to hold each objet form recyclerview and to show it in recyclerview
    class Viewholder extends RecyclerView.ViewHolder {


        TextView foodTitle, foodPrice, hostelLocation;
        Button orderNowBtn;
        ImageView imageView;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            //Assigning the address of the Android Materials to perform appropriate action
            foodTitle = (TextView) itemView.findViewById(R.id.FoodName);
            foodPrice = (TextView) itemView.findViewById(R.id.FoodPrice);
            hostelLocation = (TextView) itemView.findViewById(R.id.HotelLocation);

            imageView = (ImageView) itemView.findViewById(R.id.FoodImage);
            orderNowBtn = (Button) itemView.findViewById(R.id.OrderNowBtn);
        }
    }


}


