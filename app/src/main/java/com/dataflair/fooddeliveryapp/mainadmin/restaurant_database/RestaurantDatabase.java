package com.dataflair.fooddeliveryapp.mainadmin.restaurant_database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dataflair.fooddeliveryapp.FDConstants;

@Database(entities = {ModelRestaurant.class}, version = 2)
public abstract class RestaurantDatabase extends RoomDatabase {
        abstract RestaurantDAO getRestaurantDao();
        private static RestaurantDatabase restaurantDatabase;

        public RestaurantDatabase(){

        }

        public static synchronized RestaurantDatabase getInstance(Context context) {
            if (restaurantDatabase == null) {
                restaurantDatabase = create(context);
            }
            return restaurantDatabase;
        }

        private static RestaurantDatabase create(Context context){
            restaurantDatabase = Room.databaseBuilder(context, RestaurantDatabase.class, "Restaurant_Database.db").fallbackToDestructiveMigration().build();
            return restaurantDatabase;
        }
}
