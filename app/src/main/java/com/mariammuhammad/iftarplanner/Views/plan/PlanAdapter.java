package com.mariammuhammad.iftarplanner.Views.plan;

import android.app.AlertDialog;
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
import com.mariammuhammad.iftarplanner.Views.favorite.RemoveListener;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {
    private final Context context;
    private final List<MealStorage> planMeals;
    SpecificMealListener specificMealListener;

    RemoveListener removeListener;


    public PlanAdapter(Context context, List<MealStorage> planMeals,SpecificMealListener specificMealListener ) {
        this.context = context;
        this.planMeals = planMeals;
        this.specificMealListener=specificMealListener;
    }

    @NonNull
    @Override
    public PlanAdapter.PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.plan_item, parent, false);
        return new PlanAdapter.PlanViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull PlanAdapter.PlanViewHolder holder, int position) {
        MealStorage mealStorage = planMeals.get(position);
        Meal meal = mealStorage.getMeal();

        holder.mealName.setText(meal.strMeal);

        Glide.with(context)
                .load(meal.strMealThumb)
                .into(holder.mealImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                specificMealListener.onMealClick(Integer.parseInt(meal.idMeal), meal);
            }
        });

        holder.removeBtn.setOnClickListener(view -> {
            showConfirmationDialog(mealStorage);
        });

    }

    @Override
    public int getItemCount() {
        return planMeals.size();
    }

    private void showConfirmationDialog(MealStorage mealStorage) {
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this meal?");

        builder.setNegativeButton("No",(dialog,which)->{
            dialog.dismiss();
        });

        builder.setPositiveButton("Yes",(dialog,which)->{
            if(removeListener!=null){
                removeListener.onMealDelete(mealStorage);
            }
        });

        builder.show();

    }

    public class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView mealName;
        ImageView mealImage;

        Button removeBtn;
        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);


                mealName = itemView.findViewById(R.id.mealListName);
                mealImage = itemView.findViewById(R.id.mealListImage);
                removeBtn=itemView.findViewById(R.id.removeBtnPlan);
            }
        }
    }

