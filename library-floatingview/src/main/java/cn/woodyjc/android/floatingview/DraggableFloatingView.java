package cn.woodyjc.android.floatingview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

/**
 * 自由拖动
 * <p>
 * Created by June on 2018/1/23.
 */
public class DraggableFloatingView extends BaseFloatingView {
	private String TAG = DraggableFloatingView.class.getSimpleName();

	protected int statusBarHeight; // 状态栏的高

	private boolean enableDraggable = true;
	private boolean dragFlag; // 防止多点触摸时导致view漂移
	private float   touchedX;
	private float   touchedY;

	// 悬浮窗的左上角顶点相对屏幕的坐标
	private float disX;
	private float disY;

	public DraggableFloatingView(@NonNull Context context) {
		super(context);
	}

	public void setDraggable(boolean enable) {
		this.enableDraggable = enable;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (enableDraggable) {
			// >>>自由拖动计算<<<

			// 相对于屏幕的点坐标值
			float ex = ev.getRawX();
			float ey = ev.getRawY();

			switch (ev.getActionMasked()) {
				case MotionEvent.ACTION_DOWN:
					dragFlag = true;
					touchedX = ev.getX();
					touchedY = ev.getY();
					//	Log.d(TAG, "dispatchTouchEvent()--(getRawX=" + ex + ",getRawY=" + ey + ") , (getX=" + touchedX + ",getY=" + touchedY + ") , (lp.x=" + winParams.x + ",lp.y=" + winParams.y + ") ,statusBarHeight=" + statusBarHeight);
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					dragFlag = false;// 大于一个点时，因为会飘移而禁止拖动
					break;
				case MotionEvent.ACTION_MOVE:
					disX = ex - touchedX;
					disY = ey - touchedY - statusBarHeight;
					//	Log.d(TAG, "dispatchTouchEvent()--x:" + disX + ",y:" + disY);
					if (dragFlag) {
						int[] position = new int[2];
						getLocationInWindow(position);
						updateLayout((int) disX, (int) disY);
					}
					break;
				case MotionEvent.ACTION_UP:
					dragFlag = false;
					break;
			}
		}
		return super.dispatchTouchEvent(ev);
	}

}
