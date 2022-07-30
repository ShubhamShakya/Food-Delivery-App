package com.dataflair.fooddeliveryapp.mainadmin;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dataflair.fooddeliveryapp.FDConstants;
import com.dataflair.fooddeliveryapp.Model.RestaurantModel;
import com.dataflair.fooddeliveryapp.R;
import com.dataflair.fooddeliveryapp.databinding.FragmentDeleteRestaurantMainAdminBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class DeleteRestaurantMainAdmin extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private FragmentDeleteRestaurantMainAdminBinding binding;
    protected static long totalFoodItemCount;
    private RemoveRestaurantAdapter removeRestaurantAdapter;
    private ArrayList<RestaurantModel> restaurantArrayList;
    private ArrayList<Long> countFoodItems;
    private OnConfirmDelete onConfirmDeleteListner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDeleteRestaurantMainAdminBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        countFoodItems = new ArrayList<>();
        restaurantArrayList = new ArrayList<>();
        onConfirmDeleteListner = new OnConfirmDelete() {
            @Override
            public void onConfirm(String restaurantId, int position) {
                deleteRestaurantPermanently(restaurantId, position);
            }
        };


       /* FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(FDConstants.MAIN_ADMIN).child(FDConstants.RESTAURANT), Model.class)
                        .build();*/

        FirebaseDatabase.getInstance().getReference().child(FDConstants.MAIN_ADMIN).child(FDConstants.RESTAURANT)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isComplete()) {
                            for (DataSnapshot snapshot : task.getResult().getChildren()) {
                                RestaurantModel model = snapshot.child(FDConstants.PERSONAL_DETAILS).getValue(RestaurantModel.class);
                                restaurantArrayList.add(model);
                                countFoodItems.add(snapshot.child(FDConstants.FOOD_ITEMS).getChildrenCount());
                            }
                        }
                    }
                });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.deleteRestaurantRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                removeRestaurantAdapter = new RemoveRestaurantAdapter(restaurantArrayList, countFoodItems, onConfirmDeleteListner);
                binding.deleteRestaurantRecyclerView.setAdapter(removeRestaurantAdapter);
            }
        }, 1000);
    }

    private void deleteRestaurantPermanently(String restaurantId, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Restaurant Permanently!");
        builder.setMessage("Are you sure you want to delete restaurant whose Restaurant ID is-" + restaurantId);
        builder.setIcon(R.drawable.alert_icon_dialog_box);


        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dialog dialog1 = new Dialog(getContext());
                dialog1.setContentView(R.layout.dailog_asking_pass_admin_before_delete_restaurant);

                TextInputLayout textInputLayout = dialog1.findViewById(R.id.password_delete_restaurant_main_admin);
                Button button = dialog1.findViewById(R.id.delete_button_main_admin);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String temp = Objects.requireNonNull(textInputLayout.getEditText()).getText().toString();
                        if (temp.equals("205262")) {
                            FirebaseDatabase.getInstance().getReference().child(FDConstants.MAIN_ADMIN).child(FDConstants.RESTAURANT).child(restaurantId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            snapshot.getRef().removeValue();
                                            dialog1.cancel();
                                            removeRestaurantAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(getContext()," Error Deleting Restaurant "+error.getMessage(), Toast.LENGTH_LONG).show();
                                            dialog1.cancel();
                                        }
                                    });
                        }
                    }
                });


                dialog1.show();
            }
        });

        builder.show();

    }

    interface OnConfirmDelete {
        void onConfirm(String restaurantID, int position);
    }

   /* @Override
    public void onStart() {
        super.onStart();

        //To start listening for the data form the firebase
        removeRestaurantAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        //To stop listening for the data from teh firebase
        removeRestaurantAdapter.stopListening();
    }*/


}
