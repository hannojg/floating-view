package cn.woodyjc.android.floatingview;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by June on 2018/1/19.
 */

public class Util {
	/**
	 * 获取状态栏高度
	 *
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		Resources resources = context.getResources();
		int resId = resources.getIdentifier("status_bar_height", "dimen", "android");
		if (resId > 0) {
			return resources.getDimensionPixelSize(resId);
		}
		return 0;
	}
}
