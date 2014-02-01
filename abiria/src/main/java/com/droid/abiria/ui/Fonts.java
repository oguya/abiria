package com.droid.abiria.ui;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import java.lang.reflect.Type;

/**
 * Created by james on 01/02/14.
 */
public class Fonts {
    private Typeface boldFont;
    private Typeface thinFont;
    private Typeface thinItalic;

    private Context context;

    public Fonts(Context context){
        this.context = context;
    }

    public void setTypeface(TextView textView){
        if(textView != null){
            if(textView.getTypeface() != null && textView.getTypeface().isBold()){
                textView.setTypeface(getBoldFont());
            }else if(textView.getTypeface() != null && textView.getTypeface().isItalic()){
                textView.setTypeface(getThinItalicFont());
            }
            else{
                textView.setTypeface(getThinFont());
            }
        }
    }

    private Typeface getBoldFont(){
        if(boldFont == null){
            boldFont = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto_Bold.ttf");
        }
        return this.boldFont;
    }

    private Typeface getThinFont(){
        if(thinFont == null){
            thinFont = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto_Thin.ttf");
        }
        return this.thinFont;
    }

    private Typeface getThinItalicFont(){
        if(thinItalic == null){
            thinItalic = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto_ThinItalic.ttf");
        }
        return this.thinItalic;
    }

}
