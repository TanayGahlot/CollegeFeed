package in.mobile.connectree.collegefeed.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

import in.mobile.connectree.collegefeed.ImageViewer;
import in.mobile.connectree.collegefeed.PhotosFragment;
import in.mobile.connectree.collegefeed.R;
import in.mobile.connectree.collegefeed.model.MySingleton;

public class PhotosGridAdapter extends RecyclerView.Adapter<PhotosGridAdapter.ViewHolder> {

    ImageLoader mImageLoader;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imgView;

        public ViewHolder(ImageView iv) {
            super(iv);
            imgView = iv;
            imgView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
	
					Intent i = new Intent(imgView.getContext(),ImageViewer.class);
					i.putExtra("position", (Integer)imgView.getTag());
					imgView.getContext().startActivity(i);
				}
            });
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public PhotosGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
    	ImageView imageView;

        mImageLoader = MySingleton.getInstance(parent.getContext().getApplicationContext()).getImageLoader();

        imageView = new ImageView(parent.getContext());

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) parent.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);

        int rotation = display.getRotation();

        if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270)
            imageView.setLayoutParams(new GridView.LayoutParams(metrics.widthPixels/4,metrics.widthPixels/4));
        else
            imageView.setLayoutParams(new GridView.LayoutParams(metrics.widthPixels/3,metrics.widthPixels/3));

        
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setPadding(8, 8, 8, 8);
     

        ViewHolder vh = new ViewHolder(imageView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        mImageLoader.get(PhotosFragment.image_urls.get(position), ImageLoader.getImageListener(holder.imgView,R.drawable.ic_photos,R.drawable.ic_photos));
    	holder.imgView.setTag(position);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return PhotosFragment.image_urls.size();
    }
}