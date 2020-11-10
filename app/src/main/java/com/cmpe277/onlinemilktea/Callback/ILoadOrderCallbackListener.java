package com.cmpe277.onlinemilktea.Callback;

import com.cmpe277.onlinemilktea.Model.Order;

import java.util.List;

public interface ILoadOrderCallbackListener {
    void onLoadOrderSuccess(List<Order> orderList);
    void onLoadOrderFailed(String message);
}
