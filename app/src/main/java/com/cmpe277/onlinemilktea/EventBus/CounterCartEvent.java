package com.cmpe277.onlinemilktea.EventBus;

import com.cmpe277.onlinemilktea.Model.FoodModel;

public class CounterCartEvent {
    private  boolean success ;

    public CounterCartEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
