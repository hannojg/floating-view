package cn.woodyjc.android.floatingview;

import android.util.DisplayMetrics;

/**
 * 悬浮窗参数
 * <p>
 * Created by June on 2018/1/19.
 */

public class FloatingParams {
	/**
	 * 宽
	 */
	public int width;
	/**
	 * 高
	 */
	public int height;
	/**
	 * 左上角顶点x坐标
	 */
	public int x;
	/**
	 * 左上角顶点y坐标
	 */
	public int y;
	/**
	 * 悬浮窗类型 {@link FloatingType}
	 */
	public int type;

	public FloatingParams() {
	}

	public FloatingParams(int width, int height, int x, int y, int type) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public static FloatingParams getDefault(DisplayMetrics dm) {
		FloatingParams options = new FloatingParams();
		options.width = (int) (dm.widthPixels * 2F / 3F);
		options.height = (int) (options.width * 9F / 16F);
		options.x = (int) (dm.widthPixels / 3F - 5);
		options.y = 5;
		options.type = FloatingType.TYPE_SYSTEM;
		return options;
	}

}
