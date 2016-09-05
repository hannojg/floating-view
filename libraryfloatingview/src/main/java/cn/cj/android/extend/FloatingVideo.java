package cn.cj.android.extend;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import cn.cj.android.floatingview.FloatingView;

/**
 * Created by June on 2016/8/24.
 */
public class FloatingVideo extends FloatingView {
	public FloatingVideo(Context context) {
		super(context);
	}

	// 计算出合适的位置
	@Override
	protected void calcAppropriatePosition(WindowManager.LayoutParams lp, float x, float y) {
		if (x < -lp.width / 2) { // 从屏幕左侧移出了，加限制
			lp.x = -lp.width / 2;
		} else if (x > dm.widthPixels - lp.width / 2) { // 移出屏幕右侧
			lp.x = dm.widthPixels - lp.width / 2;
		} else {
			lp.x = (int) x;
		}

		if (y < -statusBarHeight) { // 屏幕顶部靠边
			lp.y = -statusBarHeight;
		} else if (y > dm.heightPixels - lp.height / 2 - statusBarHeight) { // 屏幕底部限制
			lp.y = dm.heightPixels - lp.height / 2 - statusBarHeight;
		} else {
			lp.y = (int) y;
		}
		//        Log.d(TAG, "calcAppropriatePosition()--x:" + x + ",y:" + y + " calc-> x:" + lp.x + ",y:" + lp.y);
	}

	/**
	 * 默认配置
	 * @param context
	 * @return
	 */
	public static Options create(Context context) {
		Options options = new Options();
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		options.width = (int) (dm.widthPixels * 2F / 3F);
		options.height = (int) (options.width * 9F / 16F);
		options.x = (int) (dm.widthPixels / 3F - 5);
		options.y = 5;
		return options;
	}
}
