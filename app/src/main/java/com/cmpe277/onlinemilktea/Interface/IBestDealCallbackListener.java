package com.cmpe277.onlinemilktea.Interface;

import com.cmpe277.onlinemilktea.Model.BestDealModel;

import java.util.List;

public interface IBestDealCallbackListener {
    void onBestDealLoadSuccess(List<BestDealModel> bestDealModels);
    void onBestDealLoadFailed(String message);
}
