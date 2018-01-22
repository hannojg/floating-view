package cn.woodyjc.android.floatingview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * 悬浮窗容器
 * <p>
 * Created by June on 2018/1/19.
 */

public class FloatingView extends FrameLayout {
	private static final String TAG       = FloatingView.class.getSimpleName();
	// 刷新位置所需的最短移动距离
	private static final int    THRESHOLD = 5;

	// 因为WindowManager.LayoutParams中的坐标原点不是屏幕左上角顶点(而在屏幕左边界和状态栏下边相交点)，所以要全屏幕自由移动就要考虑状态栏的高
	protected int statusBarHeight; // 状态栏的高

	private Context        context;
	private FloatingParams floatingParams;
	private boolean        canDrag; // 防止多点触摸时导致view漂移
	private float          firstTouchedX;
	private float          firstTouchedY;

	// 悬浮窗的左上角顶点相对屏幕的坐标
	private float tempX;
	private float tempY;

	protected DisplayMetrics             dm;
	private   WindowManager              wm;
	protected WindowManager.LayoutParams winParams;

	public FloatingView(@NonNull Context context) {
		super(context);

		this.context = context;
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		statusBarHeight = Util.getStatusBarHeight(context);
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);

		winParams = new WindowManager.LayoutParams();
		winParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
		winParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		winParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
		winParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
		winParams.format = PixelFormat.TRANSLUCENT;
		winParams.gravity = Gravity.TOP | Gravity.LEFT;
	}

	public WindowManager.LayoutParams getWindowLayoutParams() {
		return winParams;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// >>>自由拖动计算<<<

		// 相对于屏幕的点坐标值
		float ex = ev.getRawX();
		float ey = ev.getRawY();

		switch (ev.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				canDrag = true;
				firstTouchedX = ev.getX();
				firstTouchedY = ev.getY();
				//				Log.d(TAG, "dispatchTouchEvent()--(getRawX=" + ex + ",getRawY=" + ey + ") , (getX=" + firstTouchedX + ",getY=" + firstTouchedY + ") , (lp.x=" + winParams.x + ",lp.y=" + winParams.y + ") ,statusBarHeight=" + statusBarHeight);
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				canDrag = false;// 大于一个点时，因为会飘移而禁止拖动
				break;
			case MotionEvent.ACTION_MOVE:
				tempX = ex - firstTouchedX;
				tempY = ey - firstTouchedY - statusBarHeight;
				//                Log.d(TAG, "dispatchTouchEvent()--x:" + tempX + ",y:" + tempY);
				if (canDrag) {
					if (Math.abs(tempX - winParams.x) > THRESHOLD || Math.abs(tempY - winParams.y) > THRESHOLD) {
						updateLayout((int) tempX, (int) tempY);
						//						calcAppropriatePosition(winParams, tempX, tempY);
						//						updateLayout();
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				canDrag = false;
				break;
		}
		return super.dispatchTouchEvent(ev);
	}

	protected void updateLayout(int x, int y) {
		Point point = calcAppropriatePosition(winParams, x, y);
		winParams.x = point.x;
		winParams.y = point.y;
		wm.updateViewLayout(this, winParams);
	}

	/**
	 * 计算出悬浮窗位置坐标，默认会约束计算不超出屏幕边界，可重载本方法来自定义可以移动位置范围
	 */
	protected Point calcAppropriatePosition(WindowManager.LayoutParams lp, float x, float y) {
		int tmpX = 0;
		int tmpY = 0;
		if (x < 0) { // 左
			tmpX = 0;
		} else if (x > dm.widthPixels - lp.width) { // 右
			tmpX = dm.widthPixels - lp.width;
		} else {
			tmpX = (int) x;
		}

		if (y < -statusBarHeight) { // 上
			tmpY = -statusBarHeight;
		} else if (y > dm.heightPixels - lp.height - statusBarHeight) { // 下
			tmpY = dm.heightPixels - lp.height - statusBarHeight;
		} else {
			tmpY = (int) y;
		}
		//		Log.d(TAG, "calcAppropriatePosition()--x:" + x + ",y:" + y + " calc-> x:" + tmpX + ",y:" + tmpY);
		return new Point(tmpX, tmpY);
	}

	public FloatingParams getFloatingParams() {
		return floatingParams;
	}

	public void setFloatingParams(FloatingParams floatingParams) {
		this.floatingParams = floatingParams;
		if (floatingParams == null) {
			floatingParams = FloatingParams.getDefault(dm);
		}

		winParams.width = floatingParams.width;
		winParams.height = floatingParams.height;
		winParams.x = floatingParams.x;
		winParams.y = floatingParams.y;
		winParams.type = FloatingType.TYPE_SYSTEM;
	}

	/**
	 * 显示悬浮窗
	 */
	public void show() {
		wm.addView(this, getWindowLayoutParams());
	}

	/**
	 * 移除悬浮窗
	 */
	public void remove() {
		wm.removeViewImmediate(this);
	}
}
