package cn.woodyjc.demo.ui;

import android.app.Service;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import cn.woodyjc.demo.R;

import java.io.IOException;

import cn.woodyjc.android.floatingview.FloatingParams;
import cn.woodyjc.android.floatingview.FloatingType;
import cn.woodyjc.android.floatingview.FloatingView;
import cn.woodyjc.android.floatingview.ZoomableFloatingView;

/**
 * Created by June on 2016/8/10.
 */
public class PlayerView implements View.OnTouchListener {
	private static final String TAG = PlayerView.class.getSimpleName();
	private Context      context;
	private FloatingView floatingView;
	private View         view;
	private ImageView    soundMute;
	private ImageView    closeImgBtn;
	private MediaPlayer  player;

	private boolean isMute;

	public PlayerView(Context context) {
		this.context = context;
	}

	public void show() {
		// 创建内容View
		view = createView();

		// 创建FloatingView
		FloatingParams options = new FloatingParams();
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		options.width = (int) (dm.widthPixels * 2F / 3F);
		options.height = (int) (options.width * 9F / 16F);
		options.x = (int) (dm.widthPixels / 3F - 5);
		options.y = 5;
		options.type = FloatingType.TYPE_SYSTEM;
		floatingView = new ZoomableFloatingView(context);
		floatingView.setOptions(options);

		// 把内容View加到FloatingView，并显示
		floatingView.addView(view);
		floatingView.show();
	}

	/**
	 * 创建具体显示内容View
	 */
	public View createView() {
		View view = View.inflate(context, R.layout.video_player_floating_view, null);
		SurfaceView surfaceView = (SurfaceView) view.findViewById(R.id.surfaceview);
		soundMute = (ImageView) view.findViewById(R.id.soundMute);
		closeImgBtn = (ImageView) view.findViewById(R.id.close);
		closeImgBtn.setVisibility(View.GONE);
		surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				Log.d(TAG, "---  surfaceCreated");
				play(holder);
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				Log.d(TAG, "---surfaceChanged width=" + width + ",height=" + height);
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				Log.d(TAG, "---  surfaceDestroyed");
			}
		});
		surfaceView.setOnTouchListener(this);
		soundMute.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AudioManager audioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
				if (isMute) {
					player.setAudioStreamType(AudioManager.STREAM_MUSIC);
					player.setVolume(1, 1);
					player.start();
					soundMute.setImageResource(R.drawable.floatingview_volume);
					isMute = false;
				} else {
					player.setVolume(0, 0);
					soundMute.setImageResource(R.drawable.floatingview_volume_mute);
					isMute = true;
				}
			}
		});
		closeImgBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				player.stop();
				player.release();
				player = null;
				if (floatingView != null) {
					floatingView.remove();
				}
			}
		});
		return view;
	}

	private void play(SurfaceHolder holder) {
		AssetFileDescriptor adf = null;
		try {
			AssetManager assetManager = context.getAssets();
			adf = assetManager.openFd("test.3gp");
		} catch (IOException e) {
			e.printStackTrace();
		}

		player = new MediaPlayer();
		try {
			player.setDisplay(holder);
			player.setDataSource(adf.getFileDescriptor(), adf.getStartOffset(), adf.getLength());
			player.setLooping(true);
			player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.start();
				}
			});
			player.prepareAsync();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean moving;
	private float   lastX;
	private float   lastY;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				moving = false;
				lastX = event.getX();
				lastY = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				float x = event.getX();
				float y = event.getY();
				if (x - lastX > 5 || y - lastY > 5) {
					moving = true;
				}
				break;
			case MotionEvent.ACTION_UP:
				if (!moving) // 判断点击事件
					changeButtonState();
				break;
		}
		return true;
	}

	private void changeButtonState() {
		if (closeImgBtn.getVisibility() == View.VISIBLE) {
			player.start();
			closeImgBtn.setVisibility(View.GONE);
		} else {
			player.pause();
			closeImgBtn.setVisibility(View.VISIBLE);
		}
	}
}
