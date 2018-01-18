package cn.woodyjc.android.extend;

import android.content.Context;
import android.view.MotionEvent;

/**
 * view的两点缩放计算
 * Created by June on 2016/8/17.
 */
public class ScaleFloatingVideo extends FloatingVideo {
	private final String TAG = ScaleFloatingVideo.class.getSimpleName();
	private Context context;

	private boolean scaletouch;
	private boolean zoom;
	private double  startDis;
	private double  lastDis;

	public ScaleFloatingVideo(Context context) {
		super(context);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int count = event.getPointerCount();

		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				scaletouch = true;
				zoom = false;

				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				if (scaletouch && count == 2) {
					zoom = true;
					float dx = event.getX(0) - event.getX(1);
					float dy = event.getY(0) - event.getY(1);
					startDis = Math.sqrt(dx * dx + dy * dy); // 两个触摸点的初始距离
					lastDis = 0;
				}
//				Log.d(TAG, "---action pointer down");

				break;
			case MotionEvent.ACTION_MOVE:
				if (scaletouch && zoom && count == 2) {
					float dx = event.getX(0) - event.getX(1);
					float dy = event.getY(0) - event.getY(1);
					double distance = Math.sqrt(dx * dx + dy * dy);
//					Log.d(TAG, "---action move -> lastDis:" + lastDis + ", distance:" + distance + ", delta:" + (distance - lastDis));

					if (lastDis != 0 && Math.abs(distance - lastDis) > 10) { // 两个触摸点距离的变化量
						if (distance > startDis) { // zoomin
							updateSize((int) (distance - lastDis));
						} else if (distance < startDis) { // zoomout
							updateSize((int) (distance - lastDis));
						}
					}
					lastDis = distance;
				}

				break;
			case MotionEvent.ACTION_UP:
				break;
			case MotionEvent.ACTION_POINTER_UP:
				if (count == 2) { // 由多个点到剩一个点时，解除缩放状态
					zoom = false;
					scaletouch = false;
				}

//				Log.d(TAG, "---action pointer up");
				break;
		}
//		Log.d(TAG, "---------scaletouch:" + scaletouch + ", count:" + count);

		return super.dispatchTouchEvent(event);
	}

	/**
	 * 根据增量大小，计算view的尺寸大小和位置
	 *
	 * @param delta
	 */
	protected void updateSize(int delta) {
		int dw = delta;
		int dh = (int) ((float) params.height / params.width * dw);
		int w = params.width + dw;
		int h = params.height + dh;
		if (w > dm.widthPixels || w < dm.widthPixels / 2) { // 设置缩放边界
			return;
		}
		params.width = w;
		params.height = h;
//		Log.d(TAG, "=========== w:" + params.width + ", h:" + params.height);
		super.calcAppropriatePosition(params, params.x - dw / 2, params.y - dh / 2);
		super.updateFloatingViewLayout();
	}
}