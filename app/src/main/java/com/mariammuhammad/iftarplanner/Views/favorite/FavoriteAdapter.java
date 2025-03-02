package com.mariammuhammad.iftarplanner.Views.favorite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mariammuhammad.iftarplanner.Model.DTO.Meal;
import com.mariammuhammad.iftarplanner.Model.MealStorage;
import com.mariammuhammad.iftarplanner.R;

import java.util.List;
import java.util.zip.Inflater;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {
    private final Context context;
    private final List<MealStorage> favoriteMeals;
    RemoveListener removeListener;


    public FavoriteAdapter(Context context, List<MealStorage> favoriteMeals, RemoveListener removeListener) {
        this.context = context;
        this.favoriteMeals = favoriteMeals;
        this.removeListener=removeListener;
    }

    @NonNull
    @Override
    public FavoriteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view= inflater.inflate(R.layout.favorite_item,parent,false);
        return new MyViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.MyViewHolder holder, int position) {
        MealStorage mealStorage = favoriteMeals.get(position);
        Meal meal = mealStorage.getMeal();

        holder.mealName.setText(meal.strMeal);

        Glide.with(context)
                .load(meal.strMealThumb).placeholder(R.drawable.load)
                .into(holder.mealImage);

        holder.removeButton.setOnClickListener(v -> {
            if (removeListener != null) {
                removeListener.onMealDelete(mealStorage);
            }
        });
//        holder.itemView.setOnClickListener(v -> {
//            if (onFavouriteMealClickListener != null) {
//                onFavouriteMealClickListener.onMealClick(Integer.parseInt(meal.idMeal), meal);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return favoriteMeals.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mealName;
        ImageView mealImage;
        Button removeButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealListName);
            mealImage = itemView.findViewById(R.id.mealListImage);
            removeButton = itemView.findViewById(R.id.removeBtn);
        }
    }
}
