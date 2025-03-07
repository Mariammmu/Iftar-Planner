package com.mariammuhammad.iftarplanner.Common;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPrefs {

    private static SharedPreferences sharedPreferences;
   public static synchronized void init(Context context){
       if(sharedPreferences==null) {
           sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
       }

   }
    public static SharedPreferences getInstance(){
       return sharedPreferences;
    }
}
