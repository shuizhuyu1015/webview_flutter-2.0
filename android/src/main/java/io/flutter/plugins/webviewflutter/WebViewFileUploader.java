package io.flutter.plugins.webviewflutter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.widget.Toast;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.List;

public class WebViewFileUploader {

  private Activity activity;

  private ValueCallback<Uri[]> filePathCallback;

  private String acceptType;

  WebViewFileUploader(Activity activity ){
    this.activity = activity;
  }

  public void start(ValueCallback<Uri[]> filePathCallback, String acceptType){
    this.filePathCallback = filePathCallback;
    this.acceptType = acceptType;
    if(acceptType != null && acceptType.toLowerCase().contains("image/*")){
      pickImage();
    }else {
      this.filePathCallback.onReceiveValue(null);
      this.filePathCallback = null;
      Toast.makeText(activity, "您设置的AcceptType不是图片", Toast.LENGTH_SHORT).show();
    }
  }

  private void pickImage(){
    Matisse.from(activity)
            .choose(MimeType.ofAll())
            .countable(true)
            .maxSelectable(1)
//                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(new GlideEngine())
            .showPreview(true)
            .forResult(0x001ABC);
  }

  public boolean onActivityResult(int requestCode, int resultCode, Intent data){
    if(requestCode == 0x001ABC){
      if(resultCode == Activity.RESULT_OK){
        List<Uri> uriPaths = Matisse.obtainResult(data);
        if(uriPaths != null && uriPaths.size() > 0){
          filePathCallback.onReceiveValue(new Uri[]{ uriPaths.get(0) });
          filePathCallback = null;
          return true;
        }
      }
      filePathCallback.onReceiveValue(null);
      filePathCallback = null;
      return true;
    }
    return false;
  }

}
