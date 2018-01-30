[![Download](https://api.bintray.com/packages/june/maven/floating-view/images/download.svg) ](https://bintray.com/june/maven/floating-view/_latestVersion)
[![](https://www.jitpack.io/v/woodyhi/AndroidFloatingView.svg)](https://www.jitpack.io/#woodyhi/AndroidFloatingView)

# SimpleAndroidFloatingView
简单android悬浮窗

### import
```
dependencies {
    implementation 'com.github.woodyhi:floating-view:0.1.0'
}
```

### coding
```
// 创建内容View
View contentView = createView();

// 创建FloatingView
floatingView = new FloatingView(context);

// 参数设置
FloatingParams fp = new FloatingParams();
DisplayMetrics dm = new DisplayMetrics();
((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
fp.width = (int) (dm.widthPixels * 2F / 3F);
fp.height = (int) (fp.width * 9F / 16F);
fp.x = (int) (dm.widthPixels / 3F - 5);
fp.y = 5;
fp.type = FloatingType.TYPE_SYSTEM;
floatingView.setFloatingParams(fp);

// 把内容View加到FloatingView，并显示
floatingView.addView(contentView);
floatingView.show();
```

