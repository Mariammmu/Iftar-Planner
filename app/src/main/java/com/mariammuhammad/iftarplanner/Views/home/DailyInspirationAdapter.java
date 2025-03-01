package com.mariammuhammad.iftarplanner.Views.home;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.mariammuhammad.iftarplanner.Model.DTO.Meal;
import com.mariammuhammad.iftarplanner.R;

import java.util.ArrayList;
import java.util.Objects;

public class DailyInspirationAdapter extends RecyclerView.Adapter<DailyInspirationAdapter.MyViewHolder> {
private static final String TAG = "DailyInspirationAdapter";
        Context context;
        ArrayList<Meal> meals;

        SharedPreferences sharedPreferences;
        OnItemClickListener onItemClickListener;
        CardView cardView;

        public DailyInspirationAdapter(Context context, ArrayList<Meal> meals) {
            this.context = context;
            this.meals = meals;
            sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.random_meal, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Meal mealsItem = meals.get(position);
        holder.mealName.setText(mealsItem.strMeal);
        Glide.with(context).load(mealsItem.strMealThumb).placeholder(R.drawable.load).into(holder.mealImage);

        holder.randomIdCard.setOnClickListener(v -> {
            if (sharedPreferences.getString("userId", "guest").equals("guest")) {
                showDialog("Oops! 🤔\nYou need to sign up first \nto explore this delicious meal. 🍽️");
            } else {
                onItemClickListener.onClicks(mealsItem);
            }
        });
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
                    .navigate(R.id.action_homeFragment_to_signupFragment);        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public void setList(ArrayList<Meal> mealModelArrayList) {
        this.meals = mealModelArrayList;
        notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
            void onClicks(Meal meal);

        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView mealName;
           ImageView mealImage;
          //  LottieAnimationView mealImage;
            CardView randomIdCard;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                mealName = itemView.findViewById(R.id.txtCategory);
                mealImage = itemView.findViewById(R.id.randomWait);
                randomIdCard = itemView.findViewById(R.id.category_card);
            }
        }
    }

