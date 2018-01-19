package cn.woodyjc.android.extend;

import android.content.Context;
import android.view.WindowManager;

import cn.woodyjc.android.floatingview.FloatingView;

/**
 * Created by June on 2016/8/24.
 */
public class SomeFloatingView extends FloatingView {
	public SomeFloatingView(Context context) {
		super(context);
	}

	// 计算出合适的位置
	protected void calcAppropriatePosition1(WindowManager.LayoutParams lp, float x, float y) {
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

}
