package com.android.zombiearplanet.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.zombiearplanet.R;

public abstract class BaseActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private String[] mActitvityTitles;
	private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mTitle;
    private ViewGroup _contentContainer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_base);
		initialize();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();

	}
	
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

	
	private void initialize(){
		_contentContainer = (ViewGroup) findViewById(R.id.content_frame);
		mActitvityTitles = getNavigationList();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerList =(ListView)findViewById(R.id.left_drawer);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_item,R.id.drawer_item_title,mActitvityTitles));
		mDrawerList.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id) {
				if(position == 0){
					Intent i = new Intent(BaseActivity.this,MainActivity.class);
					startActivity(i);
				}
				if(position == 1){
					Intent i = new Intent(BaseActivity.this,ZombieMapActivity.class);
					startActivity(i);
				}
				if(position == 2){
					Intent i = new Intent(BaseActivity.this,ZombieViewerActivity.class);
					startActivity(i);
				}
			}
		});
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        mDrawerLayout.setVerticalScrollBarEnabled(true);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	

	@Override
	public void setTitle(CharSequence title) {
		mTitle = (String) title;
		getActionBar().setTitle(mTitle);
	}
	
	public String[] getNavigationList(){
		String[] titles = {"Backpack","Zombie Map","Zombie Viewer"};
		return titles;
	}
	
	@Override
	public void setContentView(int layoutResID) {
		LayoutInflater inflater = LayoutInflater.from(this);
		inflater.inflate(layoutResID, this._contentContainer);
	}

}