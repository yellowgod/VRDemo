package demo.bwei.com.vrdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.vr.sdk.widgets.common.VrWidgetView;
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;
public class MainActivity extends AppCompatActivity {

    private VrPanoramaView vr;
    private MyAsyncclass async;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化View
        initView();
        //自定义异步
        async = new MyAsyncclass();
        async.execute();

        //执行实现类
        vr.setEventListener(new MyVREvent());

    }

    private void initView() {
        vr = (VrPanoramaView)findViewById(R.id.vr);
        vr.setInfoButtonEnabled(false);
        vr.setFullscreenButtonEnabled(false);
        vr.setDisplayMode(VrWidgetView.DisplayMode.FULLSCREEN_STEREO);


    }
    class MyAsyncclass  extends AsyncTask<Void,Void,Bitmap>{
        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                InputStream open = getAssets().open("andes.jpg");

                Bitmap bitmap = BitmapFactory.decodeStream(open);

                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            VrPanoramaView.Options option = new VrPanoramaView.Options();
            option.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
            vr.loadImageFromBitmap(bitmap,option);
            super.onPostExecute(bitmap);
        }
    }

    @Override
    protected void onPause() {
        vr.pauseRendering();
        super.onPause();
    }

    @Override
    protected void onResume() {
        vr.resumeRendering();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        vr.shutdown();
        super.onDestroy();
        if(async!=null){
            //退出activity的时候 如果异步没有退出  就退出
            if(async.isCancelled()){
                async.cancel(true);
            }
        }
    }
    class MyVREvent extends VrPanoramaEventListener{
        @Override
        public void onLoadSuccess() {
            super.onLoadSuccess();

        }

        @Override
        public void onLoadError(String errorMessage) {
            super.onLoadError(errorMessage);



            Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
        }
    }
}
