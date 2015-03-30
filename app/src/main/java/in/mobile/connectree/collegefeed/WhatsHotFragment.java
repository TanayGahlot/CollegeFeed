package in.mobile.connectree.collegefeed;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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

import in.mobile.connectree.collegefeed.adapter.NewsListAdapter;
import in.mobile.connectree.collegefeed.model.MySingleton;


public class WhatsHotFragment extends Fragment {

	public WhatsHotFragment(){}

	private RecyclerView mRecyclerView;
    private RecyclerView.Adapter<NewsListAdapter.ViewHolder> mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
	private SwipeRefreshLayout swipeLayout;
    View rootView;
    public static JSONArray news;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_whats_hot, container, false);

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_news);

        if(null == savedInstanceState){
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            //SharedPreferences.Editor editor = sharedPref.edit();
            String temp = sharedPref.getString("news",null);
            news = null;
            if(temp!=null)
                try {
                    news = new JSONArray(temp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String url = "http://nitg-app.appspot.com/text/news.txt";
                JsonArrayRequest jsObjRequest = new JsonArrayRequest
                        (Request.Method.GET, url, (JSONArray)null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                news = response;
                                mAdapter.notifyDataSetChanged();
                                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("news", response.toString());
                                editor.commit();
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        if(!MainActivity.newsRead.containsKey(response.getJSONObject(i).getString("url"))){
                                            MainActivity.newsRead.put(response.getJSONObject(i).getString("url"),false);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                editor.putString("newsread", MainActivity.newsRead.toString());
                                editor.commit();
                                MainActivity.countUpgrade();
                                MainActivity.changeCounter(MainActivity.mainActivityContext);
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


        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.news_recycler_view);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new NewsListAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        return rootView;
    }
}
