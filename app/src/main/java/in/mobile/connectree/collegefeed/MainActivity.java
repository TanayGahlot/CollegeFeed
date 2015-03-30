package in.mobile.connectree.collegefeed;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import in.mobile.connectree.collegefeed.adapter.NavDrawerListAdapter;
import in.mobile.connectree.collegefeed.model.NavDrawerItem;
import in.mobile.connectree.collegefeed.model.TypefaceSpan;

public class MainActivity extends ActionBarActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	//private CharSequence mDrawerTitle;
    private SpannableString mDrawerTitle;
	// used to store app title
	//private CharSequence mTitle;
    private SpannableString mTitle;
	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
    public static Hashtable<String, Boolean> newsRead = new Hashtable<String, Boolean>();
    public static int unreadCount;
    public static Context mainActivityContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        mainActivityContext = this;

        if(savedInstanceState == null)
            unreadCount = 0;

        mTitle = mDrawerTitle = new SpannableString(getTitle());
        mDrawerTitle.setSpan(new TypefaceSpan(this, "android_7.ttf"), 0, mDrawerTitle.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTitle.setSpan(new TypefaceSpan(this, "android_7.ttf"), 0, mTitle.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //mTitle = mDrawerTitle = getTitle();

        // load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array

        // What's hot, We  will add a counter here
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPref.edit();
        String read = sharedPref.getString("newsread",null);
        if(read!=null) {
            String value = read.substring(1,read.length()-1);
            String[] keyValuePairs = value.split(",");
            for (String pair : keyValuePairs)                        //iterate over the pais
            {
                String[] entry = pair.split("=");                   //split the pairs to get key and value
                newsRead.put(entry[0].trim(), Boolean.valueOf(entry[1].trim()));
            }
        }
        Enumeration e = newsRead.elements();
        while(e.hasMoreElements()){
            if(e.nextElement() == false)
                unreadCount++;
        }

		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1), true, String.valueOf(unreadCount)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// Time table
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // Reach Us
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action b                                                                                                                                                                                                                                                                                                                                                                                                                                                        ar app icon and behaving it as toggle button

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				//R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
            mDrawerLayout.openDrawer(mDrawerList);
		}
        //mDrawerLayout.openDrawer(mDrawerList);
        getSupportActionBar().setTitle(mDrawerTitle);

	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        if(sharedPref.getBoolean("reminder",false)){
            menu.findItem(R.id.action_remind).setChecked(true);
        }
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);

        try {
            if(((Fragment)getSupportFragmentManager().findFragmentByTag("TimeTableFragment")).isVisible() && !drawerOpen) {
                menu.setGroupVisible(R.id.group_action_refresh, true);
                menu.findItem(R.id.action_remind).setVisible(true);
            }
        }
        catch (Exception e) {
        }

		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
        String tag = "";
		switch (position) {
            case 0:
                fragment = new WhatsHotFragment();
                tag = "WhatsHotFragment";
                break;
            case 1:
                fragment = new FindPeopleFragment();
                tag = "FindPeopleFragment";
                break;
            case 2:
                fragment = new PhotosFragment();
                tag = "PhotosFragment";
                break;
            case 3:
                fragment = new CommunityFragment();
                tag = "CommunityFragment";
                break;
            case 4:
                fragment = new TimeTableFragment();
                tag = "TimeTableFragment";
                break;
            case 5:
                fragment = new ReachUsFragment();
                tag = "ReachUsFragment";
                break;
            default:
                break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.frame_container, fragment, tag);
            transaction.commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
        mTitle = new SpannableString(title);
        mTitle.setSpan(new TypefaceSpan(this, "android_7.ttf"), 0, mTitle.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //getSupportActionBar().setTitle(mTitle);
    }

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

    public static void changeCounter(Context c){
        ((TextView)((Activity)c).findViewById(R.id.counter)).setText(String.valueOf(unreadCount));
    }

    public static void countUpgrade(){
        Enumeration e = newsRead.elements();
        unreadCount = 0;
        while(e.hasMoreElements()){
            if(e.nextElement() == false)
                unreadCount++;
        }
    }

}
