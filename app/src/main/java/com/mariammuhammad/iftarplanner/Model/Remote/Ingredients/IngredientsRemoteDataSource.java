package com.mariammuhammad.iftarplanner.Model.Remote.Ingredients;

import com.mariammuhammad.iftarplanner.Model.Repo.RootIngredients;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IngredientsRemoteDataSource {

        private static final String BASE_URL="https://www.themealdb.com/api/json/v1/1/";

        private static IngredientsRemoteDataSource ingredientsRemoteDataSource=null;
         private Retrofit retrofit;
         IngredientesService ingredientesService;
      public  IngredientsRemoteDataSource(){
          retrofit= new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
      }

      public static IngredientsRemoteDataSource getInstance(){
          if(ingredientsRemoteDataSource==null){
              ingredientsRemoteDataSource= new IngredientsRemoteDataSource();
          }
          return ingredientsRemoteDataSource;
      }

      public void getIngredients(IngredientsNetworkCallback ingredientsNetworkCallback){
          ingredientesService.getAllIngredients().enqueue(new Callback<RootIngredients>() {
              @Override
              public void onResponse(Call<RootIngredients> call, Response<RootIngredients> response) {
                  if(response.isSuccessful()){
                      ingredientsNetworkCallback.onSuccessResultIngrediants(response.body().getIngredients());
                  }
                  else{
                      ingredientsNetworkCallback.onFailureResult("Error Un able to fetch the ingredients"+response.message());
                  }
              }

              @Override
              public void onFailure(Call<RootIngredients> call, Throwable throwable) {

                  ingredientsNetworkCallback.onFailureResult(throwable.getMessage());

              }
          });
          
      }
}
