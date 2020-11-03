package com.cmpe277.onlinemilktea.ui.foodlist;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.cmpe277.onlinemilktea.Adapter.MyFoodListAdapter;
import com.cmpe277.onlinemilktea.Common.Common;
import com.cmpe277.onlinemilktea.Model.FoodModel;
import com.cmpe277.onlinemilktea.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FoodListFragment extends Fragment {

    private FoodListViewModel sendViewModel;

    Unbinder unbinder ;
    @BindView( R.id.recycler_food_list )
    RecyclerView recycler_food_list  ;

    LayoutAnimationController layoutAnimationController ;
    MyFoodListAdapter adapter ;


    @SuppressLint("FragmentLiveDataObserve")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of( this ).get( FoodListViewModel.class );
        View root = inflater.inflate(R.layout.fragment_food_list, container, false );

        unbinder = ButterKnife.bind( this,root );
        initView();
        sendViewModel.getMutableLiveDataFoodList().observe( this, new Observer<List<FoodModel>>() {
            @Override
            public void onChanged(List<FoodModel> foodModels) {
                adapter = new MyFoodListAdapter( getContext(),foodModels );
                recycler_food_list.setAdapter( adapter );
                recycler_food_list.setLayoutAnimation( layoutAnimationController );
            }
        } );
        return root;
    }

    private void initView() {
//adđ
        ((AppCompatActivity)getActivity())
                .getSupportActionBar()
                .setTitle( Common.categorySelected.getName() );
        recycler_food_list.setHasFixedSize( true );
        recycler_food_list.setLayoutManager( new LinearLayoutManager( getContext() ) );

        layoutAnimationController = AnimationUtils.loadLayoutAnimation( getContext(),R.anim.layout_item_from_left );


    }
}
