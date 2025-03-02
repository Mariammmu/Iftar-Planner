package com.mariammuhammad.iftarplanner.Model.Local;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mariammuhammad.iftarplanner.Model.MealStorage;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealLocalDataSource {


    private static MealLocalDataSource mealLocalDataSource = null;
    private MealDAO mealDAO;
    private AppDataBase appDataBase;
    SharedPreferences sharedPreferences;
    FirebaseDatabase database;
    DatabaseReference myRef;

    private MealLocalDataSource(Context context) {
        appDataBase = AppDataBase.getInstance(context);
        mealDAO = appDataBase.mealDAO();
        sharedPreferences = context.getSharedPreferences("MyPrefs", 0);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("meals");
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

    public void fetchDataFromFirebase() {
        String userId = sharedPreferences.getString("userId", null);
        if (userId == null) {
            Log.e("Firebase", "User ID is null, unable to fetch data.");
            return;
        }

        myRef.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
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
                            mealDAO.insertMeal(savedMeals)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()) // Ensure UI updates on the main thread
                                    .subscribe(
                                            () -> Log.i("Firebase", "Meal inserted successfully."),
                                            throwable -> Log.e("Firebase", "Error inserting meal", throwable)
                                    );
                        } else {
                            Log.e("Firebase", "Meal is null for date " + dateSnapshot.getKey());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching data from Firebase", error.toException());
            }
        });
    }


}
