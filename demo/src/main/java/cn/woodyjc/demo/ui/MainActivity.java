package cn.woodyjc.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import cn.woodyjc.demo.R;

import cn.woodyjc.demo.service.FloatingWindowService;

public class MainActivity extends AppCompatActivity {

	TextView netdownTV;
	TextView netupTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		netdownTV = findViewById(R.id.netdownflow);
		netupTV = findViewById(R.id.netupflow);
		findViewById(R.id.play).setOnClickListener((v) -> {
				Intent intent = new Intent(getApplicationContext(), FloatingWindowService.class);
				intent.putExtra(FloatingWindowService.PARAM_KEY, FloatingWindowService.PARAM_VALUE);
				startService(intent);
		});

	}


}
