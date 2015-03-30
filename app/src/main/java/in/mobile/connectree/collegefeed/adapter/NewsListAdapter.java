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

import in.mobile.connectree.collegefeed.NewsDetail;
import in.mobile.connectree.collegefeed.R;
import in.mobile.connectree.collegefeed.WhatsHotFragment;
import in.mobile.connectree.collegefeed.model.MySingleton;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    ImageLoader mImageLoader;
    String url = "http://nitg-app.appspot.com/images/";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imgView;
        public TextView subjectView, detailView;
        public ViewHolder(View v) {
            super(v);
            imgView = (ImageView)v.findViewById(R.id.newsImage);
            subjectView = (TextView) v.findViewById(R.id.newsSubject);
            detailView = (TextView) v.findViewById(R.id.newsDetail);

            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent i = new Intent(v.getContext(),NewsDetail.class);
                i.putExtra("position", (Integer)imgView.getTag());
                v.getContext().startActivity(i);
                }
            });
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public NewsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
    	
    	View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card, parent, false);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) parent.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);

        ImageView imgView = (ImageView)itemView.findViewById(R.id.newsImage);

        int rotation = display.getRotation();

        if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270)
            imgView.setLayoutParams(new RelativeLayout.LayoutParams(metrics.widthPixels/5,metrics.widthPixels/5));
        else
            imgView.setLayoutParams(new RelativeLayout.LayoutParams(metrics.widthPixels/4,metrics.widthPixels/4));
        

        
        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setPadding(8, 8, 8, 8);

        mImageLoader = MySingleton.getInstance(parent.getContext().getApplicationContext()).getImageLoader();

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
            temp = url+ WhatsHotFragment.news.getJSONObject(position).getString("url")+"."+WhatsHotFragment.news.getJSONObject(position).getString("image-type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mImageLoader.get(temp, ImageLoader.getImageListener(holder.imgView,R.drawable.ic_photos,R.drawable.ic_photos));
        holder.imgView.setTag(position);
        String subject = "";
        try {
            subject = WhatsHotFragment.news.getJSONObject(position).getString("subject");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.subjectView.setText(subject);

        String detail = "";
        try {
            detail = WhatsHotFragment.news.getJSONObject(position).getString("detail");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.detailView.setText(detail);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(WhatsHotFragment.news==null)
            return 0;
        else
            return WhatsHotFragment.news.length();
    }
}