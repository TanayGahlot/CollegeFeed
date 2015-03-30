package in.mobile.connectree.collegefeed;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

import in.mobile.connectree.collegefeed.adapter.PhotosGridAdapter;
import in.mobile.connectree.collegefeed.model.MySingleton;


public class PhotosFragment extends Fragment {
	
	public PhotosFragment(){}

    public static ArrayList<String> image_urls = new ArrayList<String>();
	private RecyclerView mRecyclerView;
    private RecyclerView.Adapter<PhotosGridAdapter.ViewHolder> mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SwipeRefreshLayout swipeLayout;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        final View rootView = inflater.inflate(R.layout.fragment_photos, container, false);

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_photo);

        if(null == savedInstanceState){
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            //SharedPreferences.Editor editor = sharedPref.edit();
            String temp = sharedPref.getString("urls",null);
            image_urls.clear();
            if(temp!=null)
                image_urls.addAll(Arrays.asList(temp.split(",")));
        }

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeLayout.setRefreshing(false);
                    }
                }, 5000);*/
                String url = "http://nitg-app.appspot.com/serve";
                JsonArrayRequest jsObjRequest = new JsonArrayRequest
                        (Request.Method.GET, url, (JSONArray)null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                image_urls.clear();
                                for (int i=0; i<response.length(); i++) {
                                    try {
                                        String url = response.getString(i);
                                        if(!image_urls.contains(url))
                                            image_urls.add(url);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("urls", TextUtils.join(",", image_urls));
                                editor.commit();
                                swipeLayout.setRefreshing(false);
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub
                                Toast.makeText(rootView.getContext(),error.toString(),Toast.LENGTH_LONG).show();
                                swipeLayout.setRefreshing(false);
                            }
                        });
                MySingleton.getInstance(rootView.getContext()).addToRequestQueue(jsObjRequest);
            }
        });

        swipeLayout.setColorSchemeResources(android.R.color.holo_orange_dark);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.photo_recycler_view);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            mLayoutManager = new GridLayoutManager(rootView.getContext(),4);
        else
            mLayoutManager = new GridLayoutManager(rootView.getContext(),3);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new PhotosGridAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        return rootView;
    }
}
