package com.example.chen.news.utils;

/**
 * Created by yhz on 2018/1/25.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by chen on 2018/9/15.
 */
public class ThemeUtil {

    /**
     * 裁剪侧滑栏背景
     * @param bitmap 用户选取的图片
     * @return return
     */
    public static Bitmap getMenuBg(Bitmap bitmap){
        if(bitmap!=null) {
            return Bitmap.createBitmap(bitmap, 0, bitmap.getHeight()/2-bitmap.getWidth()/4, bitmap.getWidth(),
                    bitmap.getWidth()/2, null, false);
        }
        Bitmap colorBitmap = Bitmap.createBitmap(100, 100,
                Bitmap.Config.ARGB_8888);
        colorBitmap.eraseColor(getDefaultColor());
        return colorBitmap;
    }

    /**
     * 裁剪课表背景
     * @param bitmap 用户选取的图片
     * @return 课表背景
     */
    public static Bitmap getCourseBg(Bitmap bitmap){
        if(bitmap!=null){
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getWidth()/6*9, null, false);
        }
        Bitmap colorBitmap = Bitmap.createBitmap(100, 100,
                Bitmap.Config.ARGB_8888);
        colorBitmap.eraseColor(getDefaultColor());
        return colorBitmap;

    }
    //

    /**
     * 裁剪社区背景
     * @param bitmap 用户选取的图片
     * @return 社区背景
     */
    public static Bitmap getCommunityBg(Bitmap bitmap){
        if(bitmap!=null) {
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getWidth()/2, null, false);
        }
        Bitmap colorBitmap = Bitmap.createBitmap(100, 100,
                Bitmap.Config.ARGB_8888);
        colorBitmap.eraseColor(getDefaultColor());
        return colorBitmap;
    }

    /**
     * 获取主色
     * @param context 上下文
     * @return 主色
     */
    public static int getMainColor(Context context){
        int mainColor = -3285258;
        if (context!=null){
            mainColor = context.getSharedPreferences("theme", MODE_PRIVATE)
                    .getInt("mainColor", -3285258);
        }
        return mainColor;
    }

    /**
     * 获取图标的颜色
     * @param context 上下文
     * @return 图标色
     */
    public static int getIconColor(Context context){
        int iconColor = Color.rgb(0,0,0);
        if(context!=null){
            iconColor = context.getSharedPreferences("theme", MODE_PRIVATE)
                    .getInt("iconColor", Color.rgb(0,0,0));
        }
        return iconColor;
    }

    /**
     * 修改图标的颜色
     * @param icon 要修改的图标
     */
    public static void changeIconColor(ImageView icon,Context context){
        if(icon!=null){
            int iconColor = getIconColor(context);
            float[] colorMatrix = new float[]{
                    0, 0, 0, 0, Color.red(iconColor),
                    0, 0, 0, 0, Color.green(iconColor),
                    0, 0, 0, 0, Color.blue(iconColor),
                    0, 0, 0, (float) Color.alpha(iconColor) / 255, 0
            };
            icon.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        }
    }

    /**
     * 修改图标的颜色
     * @param icon 要修改的图标
     */
    public static void changeIconColor(Drawable icon,Context context){
        if(icon!=null){
            int iconColor = getIconColor(context);
            float[] colorMatrix = new float[]{
                    0, 0, 0, 0, Color.red(iconColor),
                    0, 0, 0, 0, Color.green(iconColor),
                    0, 0, 0, 0, Color.blue(iconColor),
                    0, 0, 0, (float) Color.alpha(iconColor) / 255, 0
            };
            icon.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        }
    }

    /**
     * 修改图标为你要的颜色的颜色
     * @param icon 要修改的图标
     * @param color 要修改的颜色
     */
    public static void changeIconColor(ImageView icon,int color){
        if(icon!=null){
            float[] colorMatrix = new float[]{
                    0, 0, 0, 0, Color.red(color),
                    0, 0, 0, 0, Color.green(color),
                    0, 0, 0, 0, Color.blue(color),
                    0, 0, 0, (float) Color.alpha(color) / 255, 0
            };
            icon.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        }
    }

    /**
     * 修改图标为你要的颜色的颜色
     * @param icon 要修改的图标
     * @param color 要修改的颜色
     */
    public static void changeIconColor(Drawable icon,int color){
        if(icon!=null){
            float[] colorMatrix = new float[]{
                    0, 0, 0, 0, Color.red(color),
                    0, 0, 0, 0, Color.green(color),
                    0, 0, 0, 0, Color.blue(color),
                    0, 0, 0, (float) Color.alpha(color) / 255, 0
            };
            icon.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        }

    }

    /**
     * 获取默认颜色
     * @return 默认颜色
     */
    public static int getDefaultColor(){
        return Color.rgb(102,178,252);
    }
}
