package cn.woodyjc.floatingwindow.ui;

import android.app.Service;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.june.floatingwindow.R;

import java.io.IOException;

import cn.woodyjc.android.extend.FloatingVideo;
import cn.woodyjc.android.floatingview.FloatingView;
import cn.woodyjc.android.floatingview.FloatingViewManager;

/**
 * Created by June on 2016/8/10.
 */
public class PlayerView implements View.OnTouchListener {
	private static final String TAG = PlayerView.class.getSimpleName();
	Context             context;
	FloatingViewManager floatingViewManager;
	View                view;
	ImageView           soundMute;
	ImageView           closeImgBtn;
	MediaPlayer         player;

	boolean isMute;

	public PlayerView(Context context) {
		this.context = context;
	}

	public void show() {
		view = createView();
		floatingViewManager = new FloatingViewManager(context);
		FloatingView.Options options = FloatingVideo.create(context);
		floatingViewManager.addScaleVideoViewToWindow(view, options);
	}

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

					//                    player.setVolume(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC), audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
//					float right = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) * 1F / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//					Log.d(TAG, right + "----audioManager " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
					player.setVolume(1, 1);
					player.start();
					soundMute.setImageResource(R.drawable.floatingview_volume);
					isMute = false;
				} else {
					Log.d(TAG, "----audioManager m  " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
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
				if (floatingViewManager != null) {
					floatingViewManager.removeView();
					floatingViewManager = null;
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
//			player.setDataSource(path);
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

	boolean moving;
	float   lastX;
	float   lastY;

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
