package com.sunain.sampleapp.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.sunain.sampleapp.Adapters.MenuAdapter;
import com.sunain.sampleapp.Fragment.HomeFragment;
import com.sunain.sampleapp.Fragment.ListFragment;
import com.sunain.sampleapp.Fragment.MainFragment;
import com.sunain.sampleapp.Fragment.PostFragment;
import com.sunain.sampleapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class MainActivity extends AppCompatActivity implements DuoMenuView.OnMenuClickListener {
    private FirebaseAuth mAuth;
    String TAG="MainActivity";
    private MenuAdapter mMenuAdapter;
    private ViewHolder mViewHolder;
    TextView t_name,t_email;
    private ArrayList<String> mTitles = new ArrayList<>();
    private static boolean calledAlready = false;
    public static final String[] permissions = new String[]
            {
                    android.Manifest.permission.INTERNET,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }
        checkPermissions();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null)
        {
          return;
        }
        t_name=findViewById(R.id.nav_header_text_title);
        t_email=findViewById(R.id.nav_header_text_sub_title);
        t_name.setText(mAuth.getCurrentUser().getDisplayName());
        t_email.setText(mAuth.getCurrentUser().getEmail());
        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));
        mViewHolder = new ViewHolder();
        handleToolbar();
        handleMenu();
        handleDrawer();
//        goToFragment(new MainFragment(), false);
        goToFragment(new HomeFragment(), false);
        mMenuAdapter.setViewSelected(0, true);
        setTitle(mTitles.get(0));
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    private void handleToolbar() {
        setSupportActionBar(mViewHolder.mToolbar);
    }

    private void handleDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(MainActivity.this,
                mViewHolder.mDuoDrawerLayout,
                mViewHolder.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();
        duoDrawerToggle.setDrawerIndicatorEnabled(true);
    }

    private void handleMenu() {
        mMenuAdapter = new MenuAdapter(mTitles);

        mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
        mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
    }

    private void goToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

//        transaction.add(R.id.container, fragment).commit();
        transaction.replace(R.id.container, fragment).commit();

    }

    private void post_press()
    {

    }
    private void list_press()
    {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            Intent i= new Intent(this,LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            Intent i= new Intent(this,LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onFooterClicked() {
        FirebaseAuth.getInstance().signOut();
        Intent i= new Intent(MainActivity.this,LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onHeaderClicked() {
        Toasty.info(this,"Profile",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        setTitle(mTitles.get(position));
        mMenuAdapter.setViewSelected(position, true);
        switch (position) {
            case 0:
                goToFragment(new HomeFragment(), true);
                break;
            case 1:
                goToFragment(new PostFragment(),true);
                break;
            case 2:
                goToFragment(new ListFragment(),true);
                break;
            case 3:
                finish();
                break;
            default:
//                Toast.makeText(getApplicationContext(),position,Toast.LENGTH_SHORT).show();
                goToFragment(new MainFragment(), false);
                break;
        }
        mViewHolder.mDuoDrawerLayout.closeDrawer();
    }

    private class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        private Toolbar mToolbar;

        ViewHolder() {
            mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
        }
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
