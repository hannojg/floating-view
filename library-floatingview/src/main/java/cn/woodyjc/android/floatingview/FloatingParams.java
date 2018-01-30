package cn.woodyjc.android.floatingview;

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

	public static FloatingParams getDefault(int screenW, int screenH) {
		float scale = 9F / 16F; //纵横比
		FloatingParams fp = new FloatingParams();
		fp.width = (int) (screenW * 2F / 3F);
		fp.height = (int) (fp.width * scale);
		fp.x = (int) (screenW / 3F - 5);
		fp.y = 5;
		fp.type = FloatingType.TYPE_SYSTEM;
		return fp;
	}

}
