package com.mariammuhammad.iftarplanner.Model.Remote.Ingredients;

import com.mariammuhammad.iftarplanner.Model.DTO.RootIngredients;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class IngredientsRemoteDataSource {

        private static final String BASE_URL="https://www.themealdb.com/api/json/v1/1/";

        private static IngredientsRemoteDataSource ingredientsRemoteDataSource=null;
         private Retrofit retrofit;
         IngredientesService ingredientesService;

      public  IngredientsRemoteDataSource(){
          retrofit= new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                  .addCallAdapterFactory(RxJava3CallAdapterFactory.create()).build();
          ingredientesService = retrofit.create(IngredientesService.class);
      }

      public static IngredientsRemoteDataSource getInstance(){
          if(ingredientsRemoteDataSource==null){
              ingredientsRemoteDataSource= new IngredientsRemoteDataSource();
          }
          return ingredientsRemoteDataSource;
      }


    public Single<RootIngredients> getIngredients() {

          return ingredientesService.getAllIngredients();

      }

      }

