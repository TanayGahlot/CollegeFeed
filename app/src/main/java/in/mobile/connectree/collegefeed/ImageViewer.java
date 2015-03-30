package in.mobile.connectree.collegefeed;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import in.mobile.connectree.collegefeed.adapter.SliderAdapter;

public class ImageViewer extends Activity {
	ViewPager viewPager;

    @Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.image_viewer);
	      
	      Bundle extras = getIntent().getExtras();

	      int position = extras.getInt("position");

          viewPager = (ViewPager) findViewById(R.id.viewPager);
          PagerAdapter adapter = new SliderAdapter(ImageViewer.this);
          viewPager.setAdapter(adapter);
          viewPager.setCurrentItem(position);
          //viewPager.setPageTransformer(true, new DepthPageTransformer());
    }
}
