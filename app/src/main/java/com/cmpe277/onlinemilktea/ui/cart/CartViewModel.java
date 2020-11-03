package com.cmpe277.onlinemilktea.ui.cart;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmpe277.onlinemilktea.Common.Common;
import com.cmpe277.onlinemilktea.Database.CartDataSource;
import com.cmpe277.onlinemilktea.Database.CartDatabase;
import com.cmpe277.onlinemilktea.Database.CartItem;
import com.cmpe277.onlinemilktea.Database.LocalCartDataSource;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CartViewModel extends ViewModel {

    private MutableLiveData<List<CartItem>> mutableLiveDataCartItems ;
    private CartDataSource cartDataSource;
    private CompositeDisposable  compositeDisposable;

    public CartViewModel() {
        compositeDisposable = new CompositeDisposable();
    }

    public void initCartDataSource(Context context) {
        cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(context).cartDAO());
    }


    public void onStop() {
        compositeDisposable.clear();
    }

    public MutableLiveData<List<CartItem>> getMutableLiveDataCartItems() {
        if (mutableLiveDataCartItems == null) {
            mutableLiveDataCartItems = new MutableLiveData<List<CartItem>>();
        }
        getAllCartItems();
        return mutableLiveDataCartItems;
    }

    private void getAllCartItems() {
        compositeDisposable.add(cartDataSource.getAllCart(Common.currentUser.getUid())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(cartItems -> {
            mutableLiveDataCartItems.setValue(cartItems);
        }, throwable -> {
            mutableLiveDataCartItems.setValue(null);
        }));
    }
}