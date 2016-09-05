package cn.cj.android.floatingview;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import cn.cj.android.extend.FloatingVideo;
import cn.cj.android.extend.ScaleFloatingVideo;


/**
 * Created by June on 2016/8/13.
 */
public class FloatingViewManager {
	private Context       context;
	private WindowManager wm;
	private FloatingView  floatingView;

	public FloatingViewManager(Context context) {
		this.context = context;
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}

	/**
	 * movable
	 *
	 * @param view
	 * @param options
	 */
	public void addViewToWindow(View view, FloatingView.Options options) {
		floatingView = new FloatingView(context);
		floatingView.setOptions(options);
		floatingView.addView(view);
		wm.addView(floatingView, floatingView.getWindowLayoutParams());
	}

	/**
	 * movable and scalable
	 *
	 * @param view
	 * @param options
	 */
	public void addScaleViewToWindow(View view, FloatingView.Options options) {
		floatingView = new ScaleFloatingView(context);
		floatingView.setOptions(options);
		floatingView.addView(view);
		wm.addView(floatingView, floatingView.getWindowLayoutParams());
	}

	/**
	 * movable
	 *
	 * @param view
	 * @param options
	 */
	public void addScaleVideoViewToWindow(View view, FloatingView.Options options) {
		floatingView = new ScaleFloatingVideo(context);
		floatingView.setOptions(options);
		floatingView.addView(view);
		wm.addView(floatingView, floatingView.getWindowLayoutParams());
	}

	public void removeView() {
		wm.removeViewImmediate(floatingView);
	}


}
