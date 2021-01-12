package com.example.myapplication.Preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.R;

public class SPreferences {
    /**BEGIN: Var definition*/
    private static Context context;
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;
    private static double _defaultValueDouble = 0d;
    /**END: Var definition*/

    /**Saves a value from any type in the shared preferences by the given KEY
     * @param key: the key under the given value should be saved
     * @param value: Value from a generic type T which should be saved
     * @param context*/
    public static <T> boolean Save(String key, T value, Context context){
        init(context);
        if(value instanceof String){
            editor.putString(key, (String) value);
        }else if(value instanceof Double){
            editor.putLong(key,Double.doubleToRawLongBits((Double)value));
        }
        else if(value instanceof Integer){
            editor.putInt(key, (Integer) value);
        }
        else if(value instanceof Boolean){
            editor.putBoolean(key, (Boolean) value);
        }
        return editor.commit();
        //TODO add more types --> This should be a generic method

    }

    /**Gets a value from the shared preferences by the given KEY and type of you variable class
     * @param key: tje key under the given value should be saved
     * @param _class: The class  your value is type of
     * @param context*/
    public static <T> T Get(String key, Class _class, Context context){
        init(context);
        if(_class == Double.class){
            return (T)(new Double(Double.longBitsToDouble(sp.getLong(key, Double.doubleToLongBits(_defaultValueDouble)))));
        }
        else if(_class == Integer.class){
            return (T)(new Integer(sp.getInt(key,context.getResources().getInteger(R.integer.defaultInteger))));
        }else if(_class == String.class){
            return (T)(new String(sp.getString(key,context.getString(R.string.defaultValue_string))));
        }else if(_class == Boolean.class){
            return (T)(new Boolean(sp.getBoolean(key,context.getResources().getBoolean(R.bool.defaultBoolean))));
        }
        return null;
    }

    /**Initialize the shared preferences if needed*/
    private static void init(Context ctx){
        if(sp == null || editor == null){ //if the editor or the preferences are null (not init), init them
            context = ctx;
            sp = context.getSharedPreferences(ctx.getString(R.string.preferences_name), Activity.MODE_PRIVATE);
            editor = sp.edit();
        }

    }

    /**Clear the shared preferences
     * @IMPORTANT: There is no rollback possible. If you use this method your shared preferences
     *              will be completely deleted and your app will be on a NEW INSTALL*/
    public static void ClearSharedPreferences(Context context){
        init(context);
        sp.edit().clear().commit();
    }
}
