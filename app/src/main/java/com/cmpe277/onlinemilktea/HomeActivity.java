package com.cmpe277.onlinemilktea;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.cmpe277.onlinemilktea.Adapter.MyCartAdapter;
import com.cmpe277.onlinemilktea.Common.Common;
import com.cmpe277.onlinemilktea.Database.CartDataSource;
import com.cmpe277.onlinemilktea.Database.CartDatabase;
import com.cmpe277.onlinemilktea.Database.LocalCartDataSource;
import com.cmpe277.onlinemilktea.EventBus.CategoryClick;
import com.cmpe277.onlinemilktea.EventBus.CounterCartEvent;
import com.cmpe277.onlinemilktea.EventBus.FoodItemClick;
import com.cmpe277.onlinemilktea.EventBus.HideFabCart;
import com.cmpe277.onlinemilktea.EventBus.UpdateItemInCart;
import com.cmpe277.onlinemilktea.ui.cart.CartFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private CartDataSource cartDataSource;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @BindView(R.id.fab)
    CounterFab fab;


    @Override
    protected void onResume() {
        super.onResume();
        countCartItem();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(this).cartDAO());

        // go to the cart detail page
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navController.navigate(R.id.nav_cart);
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_menu, R.id.nav_food_detail, R.id.nav_cart)
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        countCartItem();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        navigationView.getMenu().findItem(R.id.nav_signout).setOnMenuItemClickListener(menuItem -> {
           signout();
            //Toast.makeText(this, "sign out clicked", Toast.LENGTH_SHORT).show();
            return true;
        });



//        navigationView.getMenu().findItem(R.id.nav_cart).setOnMenuItemClickListener(menuItem -> {
//
//            //Toast.makeText(this, "cart clicked", Toast.LENGTH_SHORT).show();
////            menuItem.setChecked(true);
////            drawer.closeDrawers();
////            navController.navigate(R.id.nav_cart);
//
//            return true;
//        });

        View headerView = navigationView.getHeaderView(0);
        TextView txt_user = (TextView) headerView.findViewById(R.id.txt_user);
        Common.setSpanString("Hey, ", Common.currentUser.getName(), txt_user);

        TextView txt_email = (TextView) headerView.findViewById(R.id.txt_email);
        Common.setSpanString("Your email: ", Common.currentUser.getEmail(), txt_email);
    }
    //CartDataSource cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(this).cartDAO());

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);
        drawer.closeDrawers();
        switch(menuItem.getItemId()) {

            case R.id.nav_cart:
                navController.navigate(R.id.nav_cart);
                break;
            case R.id.nav_menu:
                navController.navigate(R.id.nav_menu);
                break;
            case R.id.nav_home:
                navController.navigate(R.id.nav_home);
                break;
        }
        return true;
    }

    private void signout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Signout")
                .setMessage("Do you really want to sign out?")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Common.selectedFood = null;
                Common.categorySelected = null;
                Common.currentUser = null;
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public boolean onNavigateUp() {
        return super.onNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register( this );
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister( this );
        super.onStop();
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onHideFabEvent(HideFabCart event) {
        if (event.isHidden()) {
            fab.hide();
        } else {
            fab.show();
        }
    }



    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onCategorySelected(CategoryClick event) {
        if(event.isSuccess())
        {
            navController.navigate(R.id.nav_food_list);

            Toast.makeText( this, "Click to "+event.getCategoryModel().getName(), Toast.LENGTH_SHORT ).show();
        }
    }


    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onFoodItemClick(FoodItemClick event) {
        if(event.isSuccess())
        {
            navController.navigate(R.id.nav_food_detail);

        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onCartClick(UpdateItemInCart event)
    {
        if(event.isSuccess())
        {
            navController.navigate(R.id.nav_cart);

        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onCartCounter(CounterCartEvent event)
    {
        if(event.isSuccess())
        {
            countCartItem();

        }
    }

    private void countCartItem() {
        cartDataSource.countItemCart(Common.currentUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Integer integer) {
                        fab.setCount(integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (!e.getMessage().contains("Query returned empty"))
                            Toast.makeText(HomeActivity.this, "[COUNT CART]"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        else
                            fab.setCount(0);
                    }
                });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
