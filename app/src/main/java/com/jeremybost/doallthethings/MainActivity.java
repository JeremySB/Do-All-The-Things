package com.jeremybost.doallthethings;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.jeremybost.doallthethings.MainFragments.MapItemsFragment;
import com.jeremybost.doallthethings.MainFragments.SettingsFragment;
import com.jeremybost.doallthethings.MainFragments.TodoListFragment;
import com.jeremybost.doallthethings.MainFragments.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements
        TodoListFragment.OnListFragmentInteractionListener,
        MapItemsFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return switchDisplay(item.getItemId());
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        switchDisplay(R.id.navigation_todo);
    }

    private boolean switchDisplay(int itemId) {
        boolean validSwitch = false;
        Fragment frag = null;
        switch (itemId) {
            case R.id.navigation_todo:
                validSwitch = true;
                frag = TodoListFragment.newInstance();
                break;
            case R.id.navigation_map:
                validSwitch = true;
                frag = MapItemsFragment.newInstance();
                break;
            case R.id.navigation_settings:
                validSwitch = true;
                frag = SettingsFragment.newInstance();
                break;
        }
        if(frag != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, frag);
            transaction.commit();
        }

        return validSwitch;
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
