package com.cmpe277.onlinemilktea.ui.menu;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cmpe277.onlinemilktea.Interface.ICategoryCallbackListener;
import com.cmpe277.onlinemilktea.Common.Common;
import com.cmpe277.onlinemilktea.Model.CategoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuViewModel extends ViewModel implements ICategoryCallbackListener {

    private MutableLiveData<List<CategoryModel>> categoryListMultable ;
    private MutableLiveData<String> messageError = new MutableLiveData<>() ;
    private ICategoryCallbackListener categoryCallbackListener ;

    public MenuViewModel() {
        categoryCallbackListener = this;
    }


    public MutableLiveData<List<CategoryModel>> getCategoryListMultable() {
        if(categoryListMultable == null)
        {
            categoryListMultable = new MutableLiveData<>(  );
            messageError = new MutableLiveData<>(  );
            loadCategories();
        }
        return categoryListMultable ;

    }

    private void loadCategories() {
        List<CategoryModel>tempList = new ArrayList<>(  );
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference( Common.CATEGORY_REF);
        categoryRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnapshot : snapshot.getChildren())
                {
                    CategoryModel categoryModel =  itemSnapshot.getValue(CategoryModel.class);
                    categoryModel.setMenu_id( itemSnapshot.getKey() );
                    tempList.add(categoryModel );
                }
                categoryCallbackListener.onCategoryLoadSuccess( tempList );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                categoryCallbackListener.onCategoryLoadFailed( error.getMessage() );
            }
        } );
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }


    @Override
    public void onCategoryLoadSuccess(List<CategoryModel> categoryModelList) {
        categoryListMultable.setValue( categoryModelList);
    }

    @Override
    public void onCategoryLoadFailed(String message) {
        messageError.setValue( message );
    }
}