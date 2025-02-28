package com.mariammuhammad.iftarplanner.Views.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mariammuhammad.iftarplanner.Model.DTO.Category;
import com.mariammuhammad.iftarplanner.Model.DTO.Meal;
import com.mariammuhammad.iftarplanner.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private static final String TAG = "DailyInspirationAdapter";
    private final OnCategoryClickListener onCategoryClickListener;
    Context context;
    ArrayList<Category> categories;

    public CategoryAdapter(Context context, ArrayList<Category> categories, OnCategoryClickListener onCategoryClickListener) {
        this.context = context;
        this.categories = categories;
        this.onCategoryClickListener = onCategoryClickListener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category categoryItems= categories.get(position);
        holder.categoryName.setText(categoryItems.getStrCategory());
        Log.i("TAG", "onBindViewHolder: "+categories.size());
        Glide.with(context).load(categoryItems.getStrCategoryThumb()).placeholder(R.drawable.load).into(holder.categoryImage);
        holder.itemView.setOnClickListener(v -> {
            onCategoryClickListener.onCategoryClick(categories.get(position).getStrCategory());
        });


    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
    public void setList(ArrayList<Category> newCategories) {
        this.categories.clear();
        this.categories.addAll(newCategories);
        notifyDataSetChanged();
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(String categoryName);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.txtCategory);
            categoryImage = itemView.findViewById(R.id.imageLoad);
        }
    }
}

