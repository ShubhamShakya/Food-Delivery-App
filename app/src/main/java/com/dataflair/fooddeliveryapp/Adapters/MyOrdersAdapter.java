package com.dataflair.fooddeliveryapp.Adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflair.fooddeliveryapp.FDConstants;
import com.dataflair.fooddeliveryapp.Fragments.HomeFragment;
import com.dataflair.fooddeliveryapp.Fragments.MyOrdersFragment;
import com.dataflair.fooddeliveryapp.Fragments.OnMarkDeliveredListener;
import com.dataflair.fooddeliveryapp.MainActivity;
import com.dataflair.fooddeliveryapp.Model.Model;
import com.dataflair.fooddeliveryapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class MyOrdersAdapter extends FirebaseRecyclerAdapter<Model, MyOrdersAdapter.Viewholder> {


    private FirebaseAuth mAuth;
    private OnMarkDeliveredListener markDeliveredListener;

    public MyOrdersAdapter(@NonNull FirebaseRecyclerOptions<Model> options, OnMarkDeliveredListener markDeliveredListener) {
        super(options);
        this.markDeliveredListener = markDeliveredListener;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onBindViewHolder(@NonNull MyOrdersAdapter.Viewholder holder, int position, @NonNull Model model) {

        //Getting the data from the database and setting the values to appropriate view
        holder.foodTitle.setText(model.getItemName());
        holder.foodPrice.setText(model.getItemPrice());
        holder.hostelLocation.setText(model.getHotelLocation());
        Picasso.get().load(model.getImageUrl()).into(holder.imageView);

        if(model.isFoodOrderDelivered()){
            holder.markAsDelivered.setVisibility(View.VISIBLE);
            holder.cancelOrderBtn.setText(R.string.order_completed);
            holder.cancelOrderBtn.setClickable(false);
            holder.cancelOrderBtn.setAlpha(0.5f);
            holder.markAsDelivered.setClickable(false);
            holder.markAsDelivered.setAlpha(0.5f);
        }else {
            //Implementing the OnClick Listener to delete the data from the database
            holder.cancelOrderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                    builder1.setTitle(R.string.cancel_order);
                    builder1.setIcon(R.drawable.alert_icon_dialog_box);
                    builder1.setMessage(R.string.are_you_sure_you_want_to_delete);
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    //Getting user id from the gmail sing in
                                    //String userId = GoogleSignIn.getLastSignedInAccount(view.getContext()).getId();

                                    //Getting the user email from google sign in
                                    String firebaseUserEmail = mAuth.getCurrentUser().getEmail();

                                    //used as a key for database
                                    String emailAsFirebaseKey = firebaseUserEmail.substring(0, firebaseUserEmail.lastIndexOf('@'));

                                    //Path to the database
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("myOrders").child(emailAsFirebaseKey);
                                    reference.orderByChild(FDConstants.FOOD_ITEM_ORDER_ID).equalTo(model.getFoodItemOrderId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            for (DataSnapshot ds : snapshot.getChildren()) {

                                                //getting the parent node of the data
                                                String key = ds.getKey();

                                                //removing the data from the database
                                                reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            FirebaseDatabase.getInstance().getReference().child("totalOrders")
                                                                    .child(key).removeValue()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {

                                                                                //Showing the Toast message to the user
                                                                                Toast.makeText(view.getContext(), "Order Canceled Successfully", Toast.LENGTH_SHORT).show();

                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                });


                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            });

            holder.markAsDelivered.setVisibility(View.VISIBLE);

            holder.markAsDelivered.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    markDeliveredListener.markAsDelivered(model.getFoodItemOrderId());
                    holder.markAsDelivered.setClickable(false);
                    holder.cancelOrderBtn.setText(R.string.order_completed);
                    holder.cancelOrderBtn.setClickable(false);
                    holder.cancelOrderBtn.setAlpha(0.5f);
                    holder.markAsDelivered.setAlpha(0.5f);
                }
            });

        }


    }


    @NonNull
    @Override
    public MyOrdersAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //the data objects are inflated into the xml file single_data_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_food_orders, parent, false);
        return new MyOrdersAdapter.Viewholder(view);

    }

    //we need view holder to hold each objet form recyclerview and to show it in recyclerview
    class Viewholder extends RecyclerView.ViewHolder {


        TextView foodTitle, foodPrice, hostelLocation,markAsDelivered;
        Button cancelOrderBtn;
        ImageView imageView;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            //Assigning the address of the Android Materials to perform appropriate action
            foodTitle = (TextView) itemView.findViewById(R.id.FoodNameMyOrders);
            foodPrice = (TextView) itemView.findViewById(R.id.FoodPriceMyOrders);
            hostelLocation = (TextView) itemView.findViewById(R.id.HotelLocationMyOrders);

            imageView = (ImageView) itemView.findViewById(R.id.FoodImageMyOrders);
            cancelOrderBtn = (Button) itemView.findViewById(R.id.CancelOrderNowBtnMyOrders);

            markAsDelivered = itemView.findViewById(R.id.mark_as_delivered);
        }
    }


}


