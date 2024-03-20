package com.manager.password.layout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.manager.password.R;
import com.manager.password.adapter.CustomListAdapter;
import com.manager.password.database.DatabaseHelper;
import com.manager.password.entity.PasswordInfo;

import java.util.List;

public  abstract class PopupLayout {

    View popupView;
    PopupWindow popupWindow;


    public void create(final View view, int resourceId){

        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(resourceId, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        //Initialize the elements of our window, install the handler
        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener((v, event) -> {

            //Close the window when clicked
            popupWindow.dismiss();
            return true;
        });
    }



}
