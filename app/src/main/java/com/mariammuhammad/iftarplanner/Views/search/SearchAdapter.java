package com.mariammuhammad.iftarplanner.Views.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mariammuhammad.iftarplanner.Model.DTO.Category;
import com.mariammuhammad.iftarplanner.Model.DTO.Country;
import com.mariammuhammad.iftarplanner.Model.DTO.Ingredient;
import com.mariammuhammad.iftarplanner.R;
import com.mariammuhammad.iftarplanner.Views.filter.FilterListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    private Context context;
    private List<Object> searchList;
    private SearchListener searchListener;

    public SearchAdapter(Context context, List<Object> searchList, SearchListener searchListener) {
        this.context = context;
        this.searchList = new ArrayList<>(searchList);
        this.searchListener = searchListener;
    }

    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ingredient_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.MyViewHolder holder, int position) {
        Object item = searchList.get(position);

        if (item instanceof Ingredient) {
            holder.tvItemName.setText(((Ingredient) item).getStrIngredient());
            holder.itemView.setOnClickListener(v -> searchListener.onItemClick(item));
            String imageUrl = "https://www.themealdb.com/images/ingredients/" +
                    ((Ingredient) item).getStrIngredient() + "-Small.png";
            holder.loadImage(imageUrl);
        } else if (item instanceof Category) {
            holder.tvItemName.setText(((Category) item).getStrCategory());
            holder.loadImage(((Category) item).getStrCategoryThumb());
            holder.itemView.setOnClickListener(v -> searchListener.onItemClick(item));
        } else if (item instanceof Country) {
            holder.tvItemName.setText(((Country) item).getStrArea());
            String countryCode = COUNTRY_CODE_MAP.getOrDefault(((Country) item).getStrArea(), "xx");
            String flagUrl = "https://www.themealdb.com/images/icons/flags/big/64/" + countryCode + ".png";
            holder.loadImage(flagUrl);
            holder.itemView.setOnClickListener(v -> searchListener.onItemClick(item));
        }
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    private static final Map<String, String> COUNTRY_CODE_MAP = new HashMap<>();

    static {
        COUNTRY_CODE_MAP.put("American", "us");
        COUNTRY_CODE_MAP.put("British", "gb");
        COUNTRY_CODE_MAP.put("Canadian", "ca");
        COUNTRY_CODE_MAP.put("Chinese", "cn");
        COUNTRY_CODE_MAP.put("Croatian", "hr");
        COUNTRY_CODE_MAP.put("Dutch", "nl");
        COUNTRY_CODE_MAP.put("Egyptian", "eg");
        COUNTRY_CODE_MAP.put("French", "fr");
        COUNTRY_CODE_MAP.put("Greek", "gr");
        COUNTRY_CODE_MAP.put("Indian", "in");
        COUNTRY_CODE_MAP.put("Irish", "ie");
        COUNTRY_CODE_MAP.put("Italian", "it");
        COUNTRY_CODE_MAP.put("Jamaican", "jm");
        COUNTRY_CODE_MAP.put("Japanese", "jp");
        COUNTRY_CODE_MAP.put("Kenyan", "ke");
        COUNTRY_CODE_MAP.put("Malaysian", "my");
        COUNTRY_CODE_MAP.put("Mexican", "mx");
        COUNTRY_CODE_MAP.put("Moroccan", "ma");
        COUNTRY_CODE_MAP.put("Polish", "pl");
        COUNTRY_CODE_MAP.put("Portuguese", "pt");
        COUNTRY_CODE_MAP.put("Russian", "ru");
        COUNTRY_CODE_MAP.put("Spanish", "es");
        COUNTRY_CODE_MAP.put("Thai", "th");
        COUNTRY_CODE_MAP.put("Tunisian", "tn");
        COUNTRY_CODE_MAP.put("Turkish", "tr");
        COUNTRY_CODE_MAP.put("Vietnamese", "vn");
    }


    public void updateSearch(List<Object> items) {
        searchList.clear();
        searchList.addAll(items);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        ImageView itemImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.ingredientText);
            itemImage = itemView.findViewById(R.id.ingredientImage);

        }

        public void loadImage(String imageUrl) {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                itemImage.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext()).load(imageUrl).placeholder(R.drawable.load).into(itemImage);
            } else {
                itemImage.setVisibility(View.GONE);
            }
        }
    }
}
