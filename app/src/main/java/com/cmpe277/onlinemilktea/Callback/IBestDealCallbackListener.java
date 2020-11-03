package com.cmpe277.onlinemilktea.Callback;

import com.cmpe277.onlinemilktea.Model.BestDealModel;
import com.cmpe277.onlinemilktea.Model.PopularCategoryModel;

import java.util.List;

public interface IBestDealCallbackListener {
    void onBestDealLoadSuccess(List<BestDealModel> bestDealModels);
    void onBestDealLoadFailed(String message);
}
