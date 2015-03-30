package in.mobile.connectree.collegefeed;


import android.app.Activity;
import android.content.Context;
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

public class CommunityDetail extends Activity {
    NetworkImageView mNetworkImageView;
    ImageLoader mImageLoader;
    TextView nameText, aboutText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_detail);

        Bundle extras = getIntent().getExtras();

        int position = extras.getInt("position");

        mNetworkImageView = (NetworkImageView) findViewById(R.id.communityNetworkImageView);
        nameText = (TextView) findViewById(R.id.communityNameText);
        aboutText = (TextView) findViewById(R.id.communityDetailText);

        String temp = null;
        String url = "http://nitg-app.appspot.com/images/";
        try {
            temp = url+CommunityFragment.communities.getJSONObject(position).getString("url")+"."+CommunityFragment.communities.getJSONObject(position).getString("image-type");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String name = "";
        try {
            name = CommunityFragment.communities.getJSONObject(position).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String about = "";
        try {
            about = CommunityFragment.communities.getJSONObject(position).getString("about");
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

        nameText.setText(name);
        aboutText.setText(about);
    }
}
