package in.mobile.connectree.collegefeed.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import in.mobile.connectree.collegefeed.PhotosFragment;
import in.mobile.connectree.collegefeed.R;
import in.mobile.connectree.collegefeed.model.MySingleton;

/**
 * Created by vidit on 19/3/15.
 */
public class SliderAdapter extends PagerAdapter {

    Context context;

    public SliderAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return PhotosFragment.image_urls.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View viewItem = inflater.inflate(R.layout.network_image, container, false);

        NetworkImageView mNetworkImageView = (NetworkImageView) viewItem.findViewById(R.id.networkImageView);

        ImageLoader mImageLoader;

        // Get the ImageLoader through your singleton class.
        mImageLoader = MySingleton.getInstance(context).getImageLoader();

        // Set the URL of the image that should be loaded into this view, and
        // specify the ImageLoader that will be used to make the request.
        mNetworkImageView.setImageUrl(PhotosFragment.image_urls.get(position), mImageLoader);

        ((ViewPager)container).addView(viewItem);

        return viewItem;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
