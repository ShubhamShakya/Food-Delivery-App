package com.dataflair.fooddeliveryapp.mainadmin.restaurant_database;

import android.os.AsyncTask;
import android.view.Display;

import java.util.List;

public class RestaurantRepository {
    private RestaurantDatabase restaurantDatabase;
    private List<List<ModelRestaurant>> listRestaurant;
    private RestaurantDAO dao;


    public RestaurantRepository(RestaurantDatabase db){
        restaurantDatabase = db;
        dao = db.getRestaurantDao();
        //listRestaurant = db.getRestaurantDao().getAllRestaurantInformation();
    }

    public void insert(ModelRestaurant modelRestaurant){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                dao.addRestaurant(modelRestaurant);
            }
        });
    }

    public void delete(ModelRestaurant modelRestaurant){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteRestaurant(modelRestaurant);
            }
        });
    }

    public List<ModelRestaurant> getAllRestaurantInformation(){
        return dao.getAllRestaurantInformation();
    }

    public ModelRestaurant getRestaurantInfo(String restaurantId){
        return dao.getRestaurantInformation(restaurantId);
    }


}
