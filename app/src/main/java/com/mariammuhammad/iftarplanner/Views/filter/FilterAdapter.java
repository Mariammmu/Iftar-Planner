package com.mariammuhammad.iftarplanner.Views.filter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.animation.content.Content;
import com.bumptech.glide.Glide;
import com.mariammuhammad.iftarplanner.Model.DTO.Meal;
import com.mariammuhammad.iftarplanner.R;
import com.mariammuhammad.iftarplanner.Views.authentication.signup.SignupFragment;

import java.util.ArrayList;
import java.util.Objects;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private static final String TAG="FilterAdapter";
    private final FilterListener filterListener;

    Context context;
    ArrayList<Meal> meals;
    SharedPreferences sharedPreferences;

    public FilterAdapter(Context context, ArrayList<Meal> meals, FilterListener filterListener) {
        this.context=context;
        this.meals=meals;
        this.filterListener = filterListener;
        sharedPreferences=context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view= inflater.inflate(R.layout.filter_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal=meals.get(position);
        if(meal==null){
            Log.i(TAG, "onBindViewHolder: meal is null at position"+position);
            return;
        }
        holder.tvMealName.setText(meal.strMeal);
        Glide.with(context).load(meal.strMealThumb).placeholder(R.drawable.load).into(holder.mealImage);
        holder.itemView.setOnClickListener(view -> {
            if(sharedPreferences.getString("userId","guest").equals("guest")){
                showDialog("Oops! 🤔\nYou need to sign up first \nto explore this delicious meal.🍽️");
            }
            else {
                if(filterListener!=null){
                    String mealId=meal.idMeal;
                    if(mealId!=null){
                        filterListener.onMealClick(
                                Integer.parseInt(mealId));

                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return meals.size();
    }
    public void showDialog(String message) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setView(dialogView);

        android.app.AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        TextView dialogMessage = dialogView.findViewById(R.id.dialogMessage);
        Button dialogOkButton = dialogView.findViewById(R.id.dialogButton);
        Button dialogSignUpButton = dialogView.findViewById(R.id.dialogSignUpButton);

        dialogMessage.setText(message);

        dialogOkButton.setOnClickListener(v -> alertDialog.dismiss());

        dialogSignUpButton.setOnClickListener(v -> {
            alertDialog.dismiss();
            Navigation.findNavController((Activity) context, R.id.navHostFragment)
                    .navigate(R.id.action_filterFragment_to_signupFragment); });
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvMealName;
        ImageView mealImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMealName=itemView.findViewById(R.id.mealListName);
            mealImage=itemView.findViewById(R.id.mealListImage);
        }
    }
}
