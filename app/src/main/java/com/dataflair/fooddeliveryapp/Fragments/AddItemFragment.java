package com.dataflair.fooddeliveryapp.Fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dataflair.fooddeliveryapp.Activities.AdminActivity;
import com.dataflair.fooddeliveryapp.FDConstants;
import com.dataflair.fooddeliveryapp.Model.FoodItem;
import com.dataflair.fooddeliveryapp.R;
import com.dataflair.fooddeliveryapp.login.MainLogin;
import com.dataflair.fooddeliveryapp.restaurantadmin.DataAddedSuccessfully;
import com.dataflair.fooddeliveryapp.restaurantadmin.RestaurantAdminDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class AddItemFragment extends Fragment {


    Button submitBtn;
    ImageView imageView;
    TextInputLayout itemNameEditTxt, itemPriceEditTxt, hotelLocationEditTxt;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    Uri imageUri;
    ProgressBar progressBarAddItem;
    private int updateFoodItemPosition;
    private FoodItem mUpdateFoodItem;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddItemFragment() {
        // Required empty public constructor
    }

    public static AddItemFragment newInstance(String param1, String param2) {
        AddItemFragment fragment = new AddItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        //Assigning the address of the android materials
        imageView = (ImageView) view.findViewById(R.id.AddItemImg);
        itemNameEditTxt =  view.findViewById(R.id.AddItemNamEditText);
        itemPriceEditTxt = view.findViewById(R.id.AddItemPriceEditTxt);
        hotelLocationEditTxt =  view.findViewById(R.id.AddHotelLocationEditText);
        submitBtn = (Button) view.findViewById(R.id.SubmitItemBtn);
        progressBarAddItem=(ProgressBar) view.findViewById(R.id.progressBarAddItem);

        databaseReference = FirebaseDatabase.getInstance().getReference().child(FDConstants.MAIN_ADMIN).child(FDConstants.RESTAURANT).child(MainLogin.currentRestaurantMobileNumber);
        storageReference = FirebaseStorage.getInstance().getReference();

        Log.e("Add Item Fragment "," isUpdateFoodItem : " + RestaurantAdminDashboard.isUpdateFoodItem);

        if(RestaurantAdminDashboard.isUpdateFoodItem) {
            // position of food item which is going to be edited.
            updateFoodItemPosition = getArguments().getInt(FDConstants.FOOD_ITEM_UPDATE_ADAPTER_POSITION);
            // get details of food item which is going to be edited.
            mUpdateFoodItem = RestaurantAdminDashboard.foodItemList.get(updateFoodItemPosition);

            if (mUpdateFoodItem != null) {
                Picasso.get().load(mUpdateFoodItem.getFoodItemImgUrl()).into(imageView);
                Objects.requireNonNull(itemNameEditTxt.getEditText()).setText(mUpdateFoodItem.getItemName());
                Objects.requireNonNull(itemPriceEditTxt.getEditText()).setText(mUpdateFoodItem.getItemPrice());
            }
            submitBtn.setText(requireContext().getResources().getText(R.string.update));
        }


        //Setting onClick Listener for the imageView To select image
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                //setting the intent action to get content
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                //setting the upload content type as image
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);

            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBarAddItem.setVisibility(View.VISIBLE);
                submitBtn.setVisibility(View.GONE);

                //getting the data from the text view
                String itemName = Objects.requireNonNull(itemNameEditTxt.getEditText()).getText().toString();
                String itemPrice = Objects.requireNonNull(itemPriceEditTxt.getEditText()).getText().toString();
                //String hotelLocation = Objects.requireNonNull(hotelLocationEditTxt.getEditText()).getText().toString();

                //checking all the fields are filled or not and performing the upload data action
                if (itemName.isEmpty() || itemPrice.isEmpty()) {
                    progressBarAddItem.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Please Enter Details", Toast.LENGTH_SHORT).show();
                } else if (imageUri == null && !RestaurantAdminDashboard.isUpdateFoodItem) {
                    progressBarAddItem.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Please Upload Image", Toast.LENGTH_SHORT).show();
                } else {
                    //calling the method to add/update data to firebase
                    if (RestaurantAdminDashboard.isUpdateFoodItem) {
                        updateData(new FoodItem(imageUri==null ? null : imageUri.toString(), itemName, itemPrice,mUpdateFoodItem.getFoodItemId()));
                    } else {
                        uploadData(imageUri, itemName, itemPrice);
                    }
                }
            }
        });

        return view;
    }



    private void updateData(FoodItem foodItem) {
        //Hash map to store values
        HashMap<String,Object> foodDetails = new HashMap<>();
        RestaurantAdminDashboard.isUpdateFoodItem = false;


        //adding the data to hashmap
        if(imageUri == null){
            foodDetails.put(FDConstants.FOOD_ITEM_NAME, foodItem.getItemName());
            foodDetails.put(FDConstants.FOOD_ITEM_PRICE, foodItem.getItemPrice());
            foodDetails.put(FDConstants.FOOD_ITEM_IMAGE_URL,mUpdateFoodItem.getFoodItemImgUrl());
        }else{
            foodDetails.put(FDConstants.FOOD_ITEM_NAME, foodItem.getItemName());
            foodDetails.put(FDConstants.FOOD_ITEM_PRICE, foodItem.getItemPrice());
            foodDetails.put(FDConstants.FOOD_ITEM_IMAGE_URL, foodItem.getFoodItemImgUrl());
        }


        databaseReference.child(FDConstants.FOOD_ITEMS).child(foodItem.getFoodItemId()).updateChildren(foodDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBarAddItem.setVisibility(View.GONE);
                        submitBtn.setVisibility(View.VISIBLE);
                        DataAddedSuccessfully dataAddedSuccessfully = new DataAddedSuccessfully();
                        dataAddedSuccessfully.show(getActivity().getSupportFragmentManager(),"");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext()," Error in Updated Data, Please Try After Sometime",Toast.LENGTH_LONG).show();
                        progressBarAddItem.setVisibility(View.GONE);
                        submitBtn.setVisibility(View.VISIBLE);
                    }
                });

    }

    private void uploadData(Uri imageUri, String itemName, String itemPrice) {

        //setting the file name as current time with milli Seconds to make the image name unique
        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

        //uploading the image to firebase
        fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //generating the unique key to add data under this node
                            String push = databaseReference.push().getKey().toString();

                            //Hash map to store values
                            HashMap<String,String> foodDetails = new HashMap();

                            //adding the data to hashmap
                            foodDetails.put(FDConstants.FOOD_ITEM_NAME, itemName);
                            foodDetails.put(FDConstants.FOOD_ITEM_PRICE,itemPrice);
                            foodDetails.put(FDConstants.FOOD_ITEM_IMAGE_URL, uri.toString());
                            foodDetails.put(FDConstants.FOOD_ITEM_ID,push);

                            //uploading the data to the firebase
                            databaseReference.child(FDConstants.FOOD_ITEMS).child(push).setValue(foodDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressBarAddItem.setVisibility(View.GONE);
                                    submitBtn.setVisibility(View.VISIBLE);

                                   /* //Calling the same intent to reset all the current data
                                    Intent intent = new Intent(getContext(), AdminActivity.class);
                                    getActivity().startActivity(intent);
                                    getActivity().finish();*/

                                    //Showing the toast to user for confirmation
                                    Toast.makeText(getContext(), getString(R.string.food_item_added_to_menu), Toast.LENGTH_LONG).show();

                                    //Removing fields of Add Item.
                                    Objects.requireNonNull(itemNameEditTxt.getEditText()).setText("");
                                    Objects.requireNonNull(itemPriceEditTxt.getEditText()).setText("");
                                    imageView.setImageURI(null);

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {

                            //Showing the toast message to the user to reUpload the data
                            Toast.makeText(getContext(), "Failed To Upload Please,Try Again", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });

    }

    private String getFileExtension(Uri imageUri) {

        //getting the image extension
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
        return extension;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            //Getting the image from the device and setting the image to imageView
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}