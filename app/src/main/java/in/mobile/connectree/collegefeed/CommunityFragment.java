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

import in.mobile.connectree.collegefeed.adapter.CommunityAdapter;
import in.mobile.connectree.collegefeed.model.MySingleton;

public class CommunityFragment extends Fragment {
	
	public CommunityFragment(){}

	private RecyclerView mRecyclerView;
    private RecyclerView.Adapter<CommunityAdapter.ViewHolder> mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeLayout;
    public static JSONArray communities;
    View rootView;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        rootView = inflater.inflate(R.layout.fragment_community, container, false);

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_community);

        if(null == savedInstanceState){
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            //SharedPreferences.Editor editor = sharedPref.edit();
            String temp = sharedPref.getString("comm",null);
            communities = null;
            if(temp!=null)
                try {
                    communities = new JSONArray(temp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String url = "http://nitg-app.appspot.com/text/communities.txt";
                JsonArrayRequest jsObjRequest = new JsonArrayRequest
                        (Request.Method.GET, url, (JSONArray)null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                communities = response;
                                mAdapter.notifyDataSetChanged();
                                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("comm", response.toString());
                                editor.commit();
                                swipeLayout.setRefreshing(false);
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub
                                Toast.makeText(rootView.getContext(), error.toString(), Toast.LENGTH_LONG).show();
                                swipeLayout.setRefreshing(false);
                            }
                        });
                MySingleton.getInstance(rootView.getContext()).addToRequestQueue(jsObjRequest);
            }
        });

        swipeLayout.setColorSchemeResources(android.R.color.holo_orange_dark);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.community_recycler_view);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            mLayoutManager = new GridLayoutManager(rootView.getContext(),3);
        else
            mLayoutManager = new GridLayoutManager(rootView.getContext(),2);

        mRecyclerView.setLayoutManager(mLayoutManager);
        
        mAdapter = new CommunityAdapter();
        
        mRecyclerView.setAdapter(mAdapter);
        
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        
        return rootView;
    }
}
