package cn.woodyjc.demo.ui;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import cn.woodyjc.demo.R;

import cn.woodyjc.android.floatingview.FloatingParams;
import cn.woodyjc.android.floatingview.FloatingView;
import cn.woodyjc.android.floatingview.Util;
import cn.woodyjc.android.netflow.NetDownFlow;
import cn.woodyjc.android.netflow.NetUpFlow;

/**
 * Created by June on 2016/8/24.
 */
public class NetFlowFloatingView {
	Context      context;
	FloatingView floatingView;
	View         view;
	TextView     upFlowTextView;
	TextView     downFlowTextView;

	NetDownFlow netDownFlow = new NetDownFlow();
	NetUpFlow   netUpFlow   = new NetUpFlow();
	Handler     handler     = new Handler();

	public NetFlowFloatingView(Context context) {
		this.context = context;
		initView();
	}

	private void initView() {
		view = View.inflate(context, R.layout.floating_view_net_flow, null);
		upFlowTextView = (TextView) view.findViewById(R.id.up_flow);
		downFlowTextView = (TextView) view.findViewById(R.id.down_flow);
		view.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return false;
			}
		});
	}

	public void show() {
		FloatingParams options = new FloatingParams();
		options.width = 300;
		options.height = Util.getStatusBarHeight(context);
		options.x = 0;
		options.y = 100;
		floatingView = new FloatingView(context);
		floatingView.setOptions(options);
		floatingView.addView(view);
		floatingView.show();
		handle();
	}

	private void handle() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				downFlowTextView.setText(netDownFlow.getSpeedWithUnit());
				upFlowTextView.setText(netUpFlow.getSpeedWithUnit());
				handler.postDelayed(this, 1000);
			}
		}, 1000);
	}

}
