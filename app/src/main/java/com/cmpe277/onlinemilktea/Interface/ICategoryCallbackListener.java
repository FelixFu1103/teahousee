package com.cmpe277.onlinemilktea.Interface;

import com.cmpe277.onlinemilktea.Model.CategoryModel;

import java.util.List;

public interface ICategoryCallbackListener {
    void onCategoryLoadSuccess(List<CategoryModel> categoryModelList);
    void onCategoryLoadFailed(String message);
}
