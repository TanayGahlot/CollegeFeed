package in.mobile.connectree.collegefeed.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import org.json.JSONException;

import in.mobile.connectree.collegefeed.CommunityDetail;
import in.mobile.connectree.collegefeed.CommunityFragment;
import in.mobile.connectree.collegefeed.R;
import in.mobile.connectree.collegefeed.model.MySingleton;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {

    ImageLoader mImageLoader;
    String url = "http://nitg-app.appspot.com/images/";
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imgView;
        public TextView txtView;
        public ViewHolder(View v) {
            super(v);
            imgView = (ImageView)v.findViewById(R.id.communityImage);
            txtView = (TextView)v.findViewById(R.id.communityText);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(),CommunityDetail.class);
                    i.putExtra("position", (Integer)imgView.getTag());
                    v.getContext().startActivity(i);
                }
            });
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public CommunityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
    	
    	View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_card, parent, false);

        ImageView imgView = (ImageView)itemView.findViewById(R.id.communityImage);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) parent.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);

        int rotation = display.getRotation();

        if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270)
            imgView.setLayoutParams(new RelativeLayout.LayoutParams(metrics.widthPixels/3,metrics.widthPixels/3));
        else
            imgView.setLayoutParams(new RelativeLayout.LayoutParams(metrics.widthPixels/2,metrics.widthPixels/2));

        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        mImageLoader = MySingleton.getInstance(parent.getContext().getApplicationContext()).getImageLoader();
        
        //itemView.setPadding(18, 18, 18, 18);
     

        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String temp = null;
        try {
            temp = url+CommunityFragment.communities.getJSONObject(position).getString("url")+"."+CommunityFragment.communities.getJSONObject(position).getString("image-type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mImageLoader.get(temp, ImageLoader.getImageListener(holder.imgView,R.drawable.ic_photos,R.drawable.ic_photos));
        holder.imgView.setTag(position);
        String name = "";
        try {
            name = CommunityFragment.communities.getJSONObject(position).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.txtView.setText(name);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        if(CommunityFragment.communities==null)
            return 0;
        else
            return CommunityFragment.communities.length();
    }
}
