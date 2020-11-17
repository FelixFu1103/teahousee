package com.cmpe277.onlinemilktea.Interface;

import com.cmpe277.onlinemilktea.Model.Order;

public interface ILoadTimeFromFirebaseListener {
    void onLoadTimeSuccess(Order order, long estimateTimeInMs);
    void onLoadTimeFailed(String message);
}
