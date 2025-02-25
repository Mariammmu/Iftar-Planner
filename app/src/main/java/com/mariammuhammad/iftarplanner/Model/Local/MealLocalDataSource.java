package com.mariammuhammad.iftarplanner.Model.Local;

import android.content.Context;
import android.content.SharedPreferences;

import com.mariammuhammad.iftarplanner.Model.MealStorage;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class MealLocalDataSource {


    private static MealLocalDataSource mealLocalDataSource = null;
    private MealDAO mealDAO;
    private AppDataBase appDataBase;
    SharedPreferences sharedPreferences;
//    FirebaseDatabase database;
//    DatabaseReference myRef;

    private MealLocalDataSource(Context context) {
        appDataBase = AppDataBase.getInstance(context);
        mealDAO = appDataBase.mealDAO();
        sharedPreferences = context.getSharedPreferences("MyPrefs", 0);
//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("meals");
    }

    public static MealLocalDataSource getInstance(Context context) {
        if (mealLocalDataSource == null) {
            mealLocalDataSource = new MealLocalDataSource(context);
        }
        return mealLocalDataSource;
    }

    public Single<List<MealStorage>> getAllFavourites() {
        return mealDAO.getAllFavouriteMeals();
    }

    public Single<List<MealStorage>> getAllPlans() {
        return mealDAO.getAllPlannedMeals();
    }

    public Completable insert(MealStorage mealStorage) {
        return mealDAO.insertMeal(mealStorage);
    }

    public Completable delete(MealStorage mealStorage) {
        return mealDAO.deleteMeal(mealStorage);
    }

    public Completable deleteAll(){return mealDAO.deleteAllMeals();}

//    public void fetchDataFromFirebase() {
//        String userId = sharedPreferences.getString("userId", null);
//        myRef.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.i("MainActivity", "onDataChange: " + dataSnapshot.getChildren());
//                for (DataSnapshot mealSnapshot : dataSnapshot.getChildren()) {
//                    for (DataSnapshot dateSnapshot : mealSnapshot.getChildren()) {
//                        MealStorage savedMeals = dateSnapshot.getValue(MealStorage.class);
//                        if (savedMeals != null) {
//                            mealDAO.insertMeal(savedMeals).subscribeOn(Schedulers.io()).subscribe();
//                        }
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("SavedMealsRepository", "Error fetching data from Firebase", error.toException());
//            }
//        });
}
