package cn.cj.floatingwindow.ui;

import android.app.Service;
import android.content.Context;
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

import cn.cj.android.extend.FloatingVideo;
import cn.cj.android.floatingview.FloatingView;
import cn.cj.android.floatingview.FloatingViewManager;

/**
 * Created by June on 2016/8/10.
 */
public class PlayerView implements View.OnTouchListener {
	private static final String TAG = PlayerView.class.getSimpleName();
	//    String path = "/storage/emulated/legacy/疯狂原始人_DVDscr中字_51rrkan.com.rmvb";
	//    String path = "/storage/emulated/legacy/TYSX/dl/Q200_f76e7e41-9616-4258-9b5a-b66132c0f225.3gp"
	String path = "http://lteby1.tv189.com/6/mobi/vod/ts01/TVM/video/3gp/TVM/HuNanHD/2016/08/09/4395129c-c98f-4683-b8bf-a0c707f97259/Q350/Q350.3gp?sign=9988c87f046c5e3a6a389ac95aa29e93&tm=57ad5b20&vw=2&ver=v1.1&t=57ad7740&qualityCode=452&version=1&guid=6a49a20a-d1c2-ff4b-c901-324114049a8f&app=115020310073&cookie=57ad5a70dfd97&session=57ad5a70dfd97&uid=104300052488646160804&uname=17721050902&time=20160812131408&videotype=2&cid=C38619987&cname=&cateid=&dev=000001&ep=14&isp=&etv=&os=30&ps=0099&clienttype=GT-I9500&deviceid=null&appver=5.2.19.7&res=1080%2A1920&channelid=059998&pid=1000000228&orderid=1100479128457&nid=&netype=11&cp=00000055&sp=00000014&ip=180.168.5.182&ipSign=d2c09011944e08b56027145f44c4827b&cdntoken=api_57ad5b203bab8&pvv=0";
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

	public void show(String path) {
		this.path = path;
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
					player.setVolume(0, 1);
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
		player = new MediaPlayer();
		try {
			player.setDisplay(holder);
			player.setDataSource(path);
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

	boolean moveing;
	float   lastX;
	float   lastY;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				moveing = false;
				lastX = event.getX();
				lastY = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				float x = event.getX();
				float y = event.getY();
				if (x - lastX > 5 || y - lastY > 5) {
					moveing = true;
				}
				break;
			case MotionEvent.ACTION_UP:
				if (!moveing) // 判断点击事件
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
