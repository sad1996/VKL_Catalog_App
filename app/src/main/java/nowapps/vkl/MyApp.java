package nowapps.vkl;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Sadakathulla on 09-08-2017.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/OpenSans_Regular.ttf");
        Fresco.initialize(this);
    }
}