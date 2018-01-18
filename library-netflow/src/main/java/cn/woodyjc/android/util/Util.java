package cn.woodyjc.android.util;

import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.Field;

/**
 * Created by June on 2016/8/12.
 */
public class Util {

	public static Integer getStatusBarHeight1(Context context) {
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object obj = clazz.newInstance();
			Field field = clazz.getField("status_bar_height");
			Object value = field.get(obj);
			int resId = Integer.parseInt(value.toString());
			return (int) context.getResources().getDimension(resId);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int getStatusBarHeight11(Context context) {
		Resources resources = context.getResources();
		int resId = resources.getIdentifier("status_bar_height", "dimen", "android");
		if (resId > 0) {
			return (int) resources.getDimensionPixelSize(resId);
		}
		return 0;
	}

}
