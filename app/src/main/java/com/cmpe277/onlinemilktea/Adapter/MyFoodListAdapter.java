package com.cmpe277.onlinemilktea.Adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cmpe277.onlinemilktea.Interface.IRecyclerClickListener;
import com.cmpe277.onlinemilktea.Common.Common;
import com.cmpe277.onlinemilktea.Database.CartDataSource;
import com.cmpe277.onlinemilktea.Database.CartDatabase;
import com.cmpe277.onlinemilktea.Database.LocalCartDataSource;
import com.cmpe277.onlinemilktea.EventBus.FoodItemClick;
import com.cmpe277.onlinemilktea.Model.FoodModel;
import com.cmpe277.onlinemilktea.R;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public class MyFoodListAdapter  extends RecyclerView.Adapter<MyFoodListAdapter.MyViewHolder> {
    private Context context ;
    private List<FoodModel> foodModelList ;
    private CompositeDisposable compositeDisposable;
    private CartDataSource cartDataSource;

    public MyFoodListAdapter(Context context, List<FoodModel> foodModelList) {
        this.context = context;
        this.foodModelList = foodModelList;
        this.compositeDisposable = new CompositeDisposable();
        this.cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(context).cartDAO());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder( LayoutInflater.from( context )
                .inflate(R.layout.layout_food_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with( context ).load( foodModelList.get( position ).getImage() ).into( holder.img_food_image );
        holder.txt_food_price.setText( new StringBuilder( "$" )
                .append( foodModelList.get( position ).getPrice() ));
        holder.txt_food_name.setText( new StringBuilder(  )
                .append( foodModelList.get( position ).getName() ));

        holder.setListener( (view, pos) -> {
            Common.selectedFood = foodModelList.get( pos );
            Common.selectedFood.setKey(String.valueOf(pos));
            EventBus.getDefault().postSticky( new FoodItemClick(true,foodModelList.get( pos )) );

        } );

        // click add cart button on food list page
//        holder.img_cart.setOnClickListener(v -> {
//            CartItem cartItem = new CartItem();
//            cartItem.setUid(Common.currentUser.getUid());
//            cartItem.setUserPhone(Common.currentUser.getPhone());
//
//            cartItem.setFoodId(foodModelList.get(position).getId());
//            cartItem.setFoodName(foodModelList.get(position).getName());
//            cartItem.setFoodImage(foodModelList.get(position).getImage());
//
//            cartItem.setFoodPrice(Double.parseDouble(String.valueOf(foodModelList.get(position).getPrice())));
//            cartItem.setFoodQuantity(1);
//            cartItem.setFoodExtraPrice(0.0);
//            cartItem.setFoodAddon("Default");
//            cartItem.setFoodSize("Default");
//
////            compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
////                    .subscribeOn(Schedulers.io())
////                    .observeOn(AndroidSchedulers.mainThread())
////                    .subscribe(() -> {
////                        Toast.makeText(context, "Add to cart success", Toast.LENGTH_SHORT).show();
////                                // send a notify to homeactivity to update counter in cart
////                        EventBus.getDefault().postSticky(new CounterCartEvent(true));
////                    }, throwable -> {
////                        Toast.makeText(context, "[cart error]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
////                    }));
//            cartDataSource.getItemWithAllOptionsInCart(Common.currentUser.getUid(),
//                    cartItem.getFoodId(),
//                    cartItem.getFoodSize(),
//                    cartItem.getFoodAddon())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new SingleObserver<CartItem>() {
//                        @Override
//                        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
//
//                        }
//
//                        @Override
//                        public void onSuccess(@io.reactivex.annotations.NonNull CartItem cartItemFromDB) {
//                            if (cartItemFromDB.equals(cartItem)) {
//                                // already in database just update
//                                cartItem.setFoodExtraPrice(cartItem.getFoodExtraPrice());
//                                cartItem.setFoodAddon(cartItem.getFoodAddon());
//                                cartItem.setFoodSize(cartItem.getFoodSize());
//                                cartItem.setFoodQuantity(cartItemFromDB.getFoodQuantity() + cartItem.getFoodQuantity());
//
//                                cartDataSource.updateCartItems(cartItemFromDB)
//                                        .subscribeOn(Schedulers.io())
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .subscribe(new SingleObserver<Integer>() {
//                                            @Override
//                                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
//
//                                            }
//
//                                            @Override
//                                            public void onSuccess(@io.reactivex.annotations.NonNull Integer integer) {
//                                                Toast.makeText(context, "update cart success", Toast.LENGTH_SHORT).show();
//                                                EventBus.getDefault().postSticky(new CounterCartEvent(true));
//                                            }
//
//                                            @Override
//                                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
//                                                Toast.makeText(context, "[UPDATE CART]"+e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                EventBus.getDefault().postSticky(new CounterCartEvent(true));
//                                            }
//                                        });
//                            } else {
//                                // item not available in cart
//                                compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
//                                        .subscribeOn(Schedulers.io())
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .subscribe(() -> {
//
//                                            Toast.makeText(context, "add to cart success", Toast.LENGTH_SHORT).show();
//                                            EventBus.getDefault().postSticky(new CounterCartEvent(true));
//                                        }, throwable -> {
//                                            Toast.makeText(context, "[CART ERROR]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
//
//                                        }));
//                            }
//                        }
//
//                        @Override
//                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
//                            if (e.getMessage().contains("empty")) {
//                                // default if cart is empty this goes
//
//                                compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
//                                        .subscribeOn(Schedulers.io())
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .subscribe(() -> {
//
//                                            Toast.makeText(context, "add to cart success", Toast.LENGTH_SHORT).show();
//                                            EventBus.getDefault().postSticky(new CounterCartEvent(true));
//                                        }, throwable -> {
//                                            Toast.makeText(context, "[CART ERROR]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
//
//                                        }));
//                            } else
//                                Toast.makeText(context, "[GET CART]"+e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//        });


    }

    @Override
    public int getItemCount() {
        return foodModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Unbinder unbinder ;
        @BindView( R.id.txt_food_name )
        TextView txt_food_name;
        @BindView( R.id.txt_food_price )
        TextView txt_food_price;
        @BindView( R.id.img_food_image )
        ImageView img_food_image ;
        @BindView( R.id.img_fav )
        ImageView img_fav ;
//        @BindView( R.id.img_quick_cart )
//        ImageView img_cart ;

        IRecyclerClickListener listener ;

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super( itemView );
            unbinder = ButterKnife.bind( this,itemView );
            itemView.setOnClickListener(this );
        }

        @Override
        public void onClick(View view) {
            listener.onItemClickListener( view,getAdapterPosition() );

        }
    }
}
