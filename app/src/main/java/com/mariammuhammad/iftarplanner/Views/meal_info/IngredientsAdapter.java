package com.mariammuhammad.iftarplanner.Views.meal_info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mariammuhammad.iftarplanner.R;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyViewHolder> {
    private final Context context;
    private final List<String> ingredients;
    private final List<String> measurements;


    public IngredientsAdapter(Context context, List<String> ingredients, List<String> measurements) {
        this.context = context;

        this.ingredients = ingredients;
        this.measurements = measurements;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ingredient_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String ingredient = ingredients.get(position);
        String measurement = measurements.get(position);

        holder.ingredientName.setText(ingredient);
        holder.measurementName.setText(measurement);

        String imageUrl = "https://www.themealdb.com/images/ingredients/" + ingredient + "-Small.png";
        Glide.with(context)
                .load(imageUrl).placeholder(R.drawable.load)
                .into(holder.ingredientImage);

    }

    @Override
    public int getItemCount() {
        return Math.min(ingredients.size(), measurements.size());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientName, measurementName;


        ImageView ingredientImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredientText);
            ingredientImage = itemView.findViewById(R.id.ingredientImage);
            measurementName = itemView.findViewById(R.id.measurement);
        }
    }

}