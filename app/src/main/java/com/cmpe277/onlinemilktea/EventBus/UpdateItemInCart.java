package com.cmpe277.onlinemilktea.EventBus;

import com.cmpe277.onlinemilktea.Database.CartItem;

public class UpdateItemInCart {
    private CartItem cartItem;
    private  boolean success ;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public UpdateItemInCart(CartItem cartItem) {
        this.cartItem = cartItem;
        this.success = success;
    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }
}
