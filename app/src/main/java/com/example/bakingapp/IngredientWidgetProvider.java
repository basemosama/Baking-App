package com.example.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.example.bakingapp.constants.Constants;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget_provider);
        SharedPreferences sharedPreferences=context.getSharedPreferences(Constants.INGREDIENTS_PREFRENCE_NAME,MODE_PRIVATE);
        String recipeName=sharedPreferences.getString(Constants.RECIPE_NAME_PREFERENCE,"");
        String recipeIngredients=sharedPreferences.getString(Constants.RECIPE_INGREDIENTS_PREFRENCE,"");
        views.setTextViewText(R.id.recipe_name_widget_tv,recipeName + " Ingredients");
        views.setTextViewText(R.id.ingredients_widget_tv,recipeIngredients);

        Intent intent=new Intent(context,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,0);
        views.setOnClickPendingIntent(R.id.ingredients_widget_tv,pendingIntent);
        // Construct the RemoteViews object
        // Instruct the widget manager to update the
        //widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

