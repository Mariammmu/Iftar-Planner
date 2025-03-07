package com.mariammuhammad.iftarplanner.Model.Local;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mariammuhammad.iftarplanner.Common.MySharedPrefs;
import com.mariammuhammad.iftarplanner.Model.MealStorage;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealLocalDataSource {


    private static MealLocalDataSource mealLocalDataSource = null;
    private MealDAO mealDAO;
    private AppDataBase appDataBase;
    FirebaseDatabase database;
    DatabaseReference myRef;

    private MealLocalDataSource(Context context) {
        appDataBase = AppDataBase.getInstance(context);
        mealDAO = appDataBase.mealDAO();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Meals");
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

    public Completable deleteAll() {
        return mealDAO.deleteAllMeals();
    }

    public void fetchDataFromFirebase() { //best prectice to pass the string userId in the param of the function
      //  String userId = MySharedPrefs.getInstance().getString("userId", null);
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            return;
        }
              String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("Firebase", "Data fetched: " + dataSnapshot.getValue()); //
                if (!dataSnapshot.exists()) { //
                    Log.e("Firebase", "No data found at path: " + myRef.child("Users").child(userId));
                    return;
                }
                for (DataSnapshot mealSnapshot : dataSnapshot.getChildren()) {
                    Log.i("Firebase", "Meal Snapshot Key: " + mealSnapshot.getKey());
                    for (DataSnapshot dateSnapshot : mealSnapshot.getChildren()) {
                        Log.i("Firebase", "Date Snapshot Key: " + dateSnapshot.getKey());
                        MealStorage savedMeals = dateSnapshot.getValue(MealStorage.class);
                        if (savedMeals != null) {
                            Log.i("Firebase", "Meal fetched: " + savedMeals.toString());
                            mealDAO.insertMeal(savedMeals).subscribeOn(Schedulers.io()).subscribe();
                        } else {
                            Log.e("Firebase", "Meal is null for date " + dateSnapshot.getKey());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("SavedMealsRepository", "Error fetching data from Firebase", error.toException());
            }
        });
    }

}
