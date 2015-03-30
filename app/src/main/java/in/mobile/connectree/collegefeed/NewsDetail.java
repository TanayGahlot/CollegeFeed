package in.mobile.connectree.collegefeed;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;

import in.mobile.connectree.collegefeed.model.MySingleton;

public class NewsDetail extends Activity {
    NetworkImageView mNetworkImageView;
    ImageLoader mImageLoader;
    TextView nameText, aboutText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);

        Bundle extras = getIntent().getExtras();

        int position = extras.getInt("position");

        mNetworkImageView = (NetworkImageView) findViewById(R.id.newsNetworkImageView);
        nameText = (TextView) findViewById(R.id.newsNameText);
        aboutText = (TextView) findViewById(R.id.newsDetailText);

        String temp = null;
        String url = "http://nitg-app.appspot.com/images/";
        try {
            temp = url+WhatsHotFragment.news.getJSONObject(position).getString("url")+"."+WhatsHotFragment.news.getJSONObject(position).getString("image-type");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String subject = "";
        try {
            subject = WhatsHotFragment.news.getJSONObject(position).getString("subject");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String detail = "";
        try {
            detail = WhatsHotFragment.news.getJSONObject(position).getString("detail");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mImageLoader = MySingleton.getInstance(this).getImageLoader();
        mNetworkImageView.setImageUrl(temp, mImageLoader);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);

        mNetworkImageView.setLayoutParams(new RelativeLayout.LayoutParams(metrics.widthPixels,metrics.heightPixels/2));

        /*if(mNetworkImageView.getWidth()>mNetworkImageView.getHeight())
            mNetworkImageView.setLayoutParams();
        else
            mNetworkImageView.setLayoutParams(new RelativeLayout.LayoutParams(mNetworkImageView.getWidth(), metrics.heightPixels));*/

        nameText.setText(subject);
        aboutText.setText(detail);


        try {
            if(!MainActivity.newsRead.get(WhatsHotFragment.news.getJSONObject(position).getString("url")))
                MainActivity.unreadCount--;
            MainActivity.newsRead.put(WhatsHotFragment.news.getJSONObject(position).getString("url"),true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainActivity.changeCounter(MainActivity.mainActivityContext);
        SharedPreferences sharedPref = ((Activity)MainActivity.mainActivityContext).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("newsread", MainActivity.newsRead.toString());
        editor.commit();

    }
}
