package com.dataflair.fooddeliveryapp.restaurantadmin;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dataflair.fooddeliveryapp.FDConstants;
import com.dataflair.fooddeliveryapp.Fragments.AddItemFragment;
import com.dataflair.fooddeliveryapp.Model.FoodItem;
import com.dataflair.fooddeliveryapp.Model.Model;
import com.dataflair.fooddeliveryapp.OnEditDeleteFoodItemListener;
import com.dataflair.fooddeliveryapp.R;
import com.dataflair.fooddeliveryapp.databinding.FragmentRemoveFootItemBinding;
import com.dataflair.fooddeliveryapp.login.MainLogin;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RemoveFootItemFragment extends Fragment {

    private FragmentRemoveFootItemBinding binding;
    private RecyclerView recyclerView;
    private RemoveFoodItemAdapter removeFoodItemAdapter;
    private ArrayList<Model> modelArrayList;

    public RemoveFootItemFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRemoveFootItemBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.deleteFoodItemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(FDConstants.MAIN_ADMIN).child(FDConstants.RESTAURANT).child(MainLogin.currentRestaurantMobileNumber).child(FDConstants.FOOD_ITEMS), Model.class)
                        .build();

        removeFoodItemAdapter=new RemoveFoodItemAdapter(options, new OnEditDeleteFoodItemListener() {

            @Override
            public void onConfirmDelete(int position, FoodItem foodItem) {
                    showMessage(getView(),position,foodItem);
            }

            @Override
            public void onEditFoodItem(int foodItemPosition) {
                AddItemFragment addItemFragment = new AddItemFragment();

                Bundle bundle = new Bundle();
                bundle.putInt(FDConstants.FOOD_ITEM_UPDATE_ADAPTER_POSITION,foodItemPosition);

                addItemFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.restaurant_admin_dashboard_layout,addItemFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        binding.deleteFoodItemRecyclerView.setAdapter(removeFoodItemAdapter);
    }

    private void permanentlyDeleteFoodItem(String restaurantId,int position,FoodItem foodItem) {
                FirebaseDatabase.getInstance().getReference().child(FDConstants.MAIN_ADMIN).child(FDConstants.RESTAURANT)
                        .child(restaurantId).child(FDConstants.FOOD_ITEMS)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    Model model = dataSnapshot.getValue(Model.class);
                                    if(model.getItemName().equals(foodItem.getItemName()) && model.getItemPrice().equals(foodItem.getItemPrice())){
                                        dataSnapshot.getRef().removeValue();
                                        break;
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(),"Unable to Delete Food Item, Please Try Again! "+error.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
    }

    @Override
    public void onStart() {
        super.onStart();

        //To start listening for the data form the firebase
        removeFoodItemAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        //To stop listening for the data from teh firebase
        removeFoodItemAdapter.stopListening();
    }

    public void showMessage(View view,int position,FoodItem foodItem){
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog_box_restaurant_id_edit_delete_food_item,null);
         EditText txt_inputText = (EditText)mView.findViewById(R.id.txt_input);
        //Button btn_cancel = (Button)mView.findViewById(R.id.btn_cancel);
        Button btn_okay = (Button)mView.findViewById(R.id.btn_okay);
        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permanentlyDeleteFoodItem(txt_inputText.getText().toString(),position,foodItem);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


}