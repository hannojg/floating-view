package cn.woodyjc.android.floatingview;

import android.util.DisplayMetrics;

/**
 * 悬浮窗参数
 *
 * Created by June on 2018/1/19.
 */

public class FloatingParams {

	public int width;
	public int height;
	public int x;
	public int y;

	public int type; // 显示级别

	public FloatingParams(){
	}

	public FloatingParams(int width, int height, int x, int y, int type) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public static FloatingParams getDefault(DisplayMetrics dm){
		FloatingParams options = new FloatingParams();
		options.width = (int) (dm.widthPixels * 2F / 3F);
		options.height = (int) (options.width * 9F / 16F);
		options.x = (int) (dm.widthPixels / 3F - 5);
		options.y = 5;
		options.type = FloatingType.TYPE_SYSTEM;
		return options;
	}

}
