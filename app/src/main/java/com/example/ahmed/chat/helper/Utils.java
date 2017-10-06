package com.example.ahmed.chat.helper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ahmed on 06/10/17.
 */

public class Utils {

    static Typeface typeface;
    private static Typeface getFont(Context ctx) {
        if (typeface == null) {
            typeface = Typeface.createFromAsset(ctx.getAssets(), "CaviarDreams.ttf");
        }
        return typeface;
    }

    public static void setFont(Context ctx ,TextView textView){
        textView.setTypeface(getFont(ctx));
    }
    public static String getVersionNumber(Context context){
        String [] versionNumberarr;
        String versionNumberString = "version number ";
        try {
            String versionNumber = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            versionNumberarr = versionNumber.split("\\.");

            versionNumberString += versionNumber;
            if(!versionNumberarr[2].equals("0"))
                versionNumberString +=" Alpha";
            else if(! versionNumberarr[1].equals("0"))
                versionNumberString +=" Beta";

        } catch (Exception e) {
            versionNumberString = "";
        }finally {
            return versionNumberString;
        }
    }
}
