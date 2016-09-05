package cn.cj.android.floatingview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Created by June on 2016/8/13.
 */
public class FloatingView extends FrameLayout {
	private static final String TAG       = "FloatingView";
	private static final int    THRESHOLD = 5;
	private Context context;

	private boolean canDrag; // 防止多点触摸时导致view漂移
	float touchStartX, touchStartY;
	float tempX, tempY;

	protected        DisplayMetrics             dm;
	protected static int                        statusBarHeight; // 状态栏的高
	private          WindowManager              wm;
	protected        WindowManager.LayoutParams params;
	private          Options                    options;

	private OnTouchListener onTouchListener;

	public FloatingView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	// 初始化
	private void init() {
		statusBarHeight = getStatusBarHeight(context);
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);

		params = new WindowManager.LayoutParams();
		params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
		params.format = PixelFormat.TRANSLUCENT;
		params.gravity = Gravity.TOP | Gravity.LEFT;
	}

	public static int getStatusBarHeight(Context context) {
		Resources resources = context.getResources();
		int resId = resources.getIdentifier("status_bar_height", "dimen", "android");
		if (resId > 0) {
			return resources.getDimensionPixelSize(resId);
		}
		return 0;
	}

	public WindowManager.LayoutParams getWindowLayoutParams() {
		return params;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		float ex = ev.getRawX();
		float ey = ev.getRawY();

		switch (ev.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				canDrag = true;
				touchStartX = ev.getX();
				touchStartY = ev.getY();
				//                Log.d(TAG, "dispatchTouchEvent()--getRawX=" + ex + ",getX" + touchStartX + ",getRawY=" + ey + ",getY" + touchStartY + ",lp.x=" + params.x + ",lp.y=" + params.y + ",statusBarHeight=" + statusBarHeight);
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				canDrag = false;
				break;
			case MotionEvent.ACTION_MOVE:
				tempX = ex - touchStartX;
				tempY = ey - touchStartY - statusBarHeight;
				//                Log.d(TAG, "dispatchTouchEvent()--x:" + tempX + ",y:" + tempY);
				if (canDrag) {
					if (Math.abs(tempX - params.x) > THRESHOLD || Math.abs(tempY - params.y) > THRESHOLD) {
						calcAppropriatePosition(params, tempX, tempY);
						wm.updateViewLayout(this, params);
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				canDrag = false;
				break;
		}
		return super.dispatchTouchEvent(ev);
	}

	protected void updateFloatingViewLayout() {
		wm.updateViewLayout(this, params);
	}

	// 计算出合适的位置，默认为不可超出屏幕。可以重写该方法
	protected void calcAppropriatePosition(WindowManager.LayoutParams lp, float x, float y) {
		if (x < 0) { // 左
			lp.x = 0;
		} else if (x > dm.widthPixels - lp.width) { // 右
			lp.x = dm.widthPixels - lp.width;
		} else {
			lp.x = (int) x;
		}

		if (y < -statusBarHeight) { // 上
			lp.y = -statusBarHeight;
		} else if (y > dm.heightPixels - lp.height - statusBarHeight) { // 下
			lp.y = dm.heightPixels - lp.height - statusBarHeight;
		} else {
			lp.y = (int) y;
		}
		//        Log.d(TAG, "calcAppropriatePosition()--x:" + x + ",y:" + y + " calc-> x:" + lp.x + ",y:" + lp.y);
	}

	public void setOptions(Options options) {
		this.options = options;
		if (options == null) { //
			DisplayMetrics dm = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(dm);
			params.width = (int) (dm.widthPixels * 2F / 3F);
			params.height = (int) (params.width * 9F / 16F);
			params.x = (int) (dm.widthPixels / 3F - 5);
			params.y = 5;

		} else {
			params.width = options.width;
			params.height = options.height;
			params.x = options.x;
			params.y = options.y;
		}
	}

	public static class Options {
		public int width;
		public int height;
		public int x;
		public int y;

		public Options() {

		}
	}


}
