package com.example.paw.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.paw.myapplication.model.Event;
import com.example.paw.myapplication.MyApplication;
import com.example.paw.myapplication.R;
import com.example.paw.myapplication.recycleview.MyRecycleViewAdapter;
import com.example.paw.myapplication.recycleview.SimpleItemTouchHelperCallback;
import com.example.paw.myapplication.room.config.AppDatabase;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MyRecycleViewAdapter myAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        for (Event e: MyApplication.getInstance().getEventsToDisplay()) {
//            AppDatabase.getInstance(this).eventDao().insert(e);
//        }
        MyApplication.getInstance().setEventsToDisplay(new ArrayList<>(AppDatabase.getInstance(this).eventDao().getAll()));
        System.out.println("##### Eventy " + MyApplication.getInstance().getEventsToDisplay().size());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "New event added", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Event newEvent = new Event();
                newEvent.setTitle("new");
                newEvent.setDate(new Date());
                newEvent.setDescription("new");

                myAdapter.addEvent(newEvent);
                recyclerView.scrollToPosition(myAdapter.getItemCount() - 1);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        TextView drawerTitle = (TextView) headerView.findViewById(R.id.drawer_title);
        TextView drawerMail = (TextView) headerView.findViewById(R.id.drawer_mail);

        drawerTitle.setText(MyApplication.getInstance().getUser().getName());
        drawerMail.setText(MyApplication.getInstance().getUser().getEmail());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recycler_fragment1);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter = new MyRecycleViewAdapter();
        myAdapter.setEvents(MyApplication.getInstance().getEventsToDisplay());
        recyclerView.setAdapter(myAdapter);


        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(myAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            logOut(item.getActionView());
        } else if(id == R.id.action_exit){
            closeApp();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_all) {
            myAdapter.setEvents(MyApplication.getInstance().getEventsToDisplay());
            myAdapter.notifyDataSetChanged();
        } else if (id == R.id.nav_present) {
//            ArrayList<Event> presentItems = new ArrayList<>();
//            Date now = new GregorianCalendar(2000, Calendar.FEBRUARY, 11).getTime();
//            for (Event e : MyApplication.getInstance().getEventsToDisplay()) {
//                if (e.getDate().getTime() > now.getTime()){
//                 presentItems.add(e);
//                }
//            }
//
//            myAdapter.setEvents(presentItems);
//            myAdapter.notifyDataSetChanged();

        } else if (id == R.id.nav_past) {

//            ArrayList<Event> pastItems = new ArrayList<>();
//            Date now = new GregorianCalendar(2000, Calendar.FEBRUARY, 11).getTime();
//            for (Event e : MyApplication.getInstance().getEventsToDisplay()) {
//                if (e.getDate().getTime() < now.getTime()){
//                    pastItems.add(e);
//                }
//            }
//
//            myAdapter.setEvents(pastItems);
//            myAdapter.notifyDataSetChanged();

        } else if (id == R.id.nav_fav) {

        } else if (id == R.id.nav_joined) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logOut(View view) {
        SharedPreferences sp = getSharedPreferences("Login", 0);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("Unm", null);
        Ed.putString("Psw", null);
        Ed.commit();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void closeApp(){
        finishAndRemoveTask();
    }
}
