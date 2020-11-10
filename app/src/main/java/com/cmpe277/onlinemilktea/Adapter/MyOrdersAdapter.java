package com.cmpe277.onlinemilktea.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cmpe277.onlinemilktea.Common.Common;
import com.cmpe277.onlinemilktea.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmpe277.onlinemilktea.Database.CartItem;
import com.cmpe277.onlinemilktea.Model.Order;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyViewHolder> {

    private Context context;
    private List<Order> orderList;
    private List<CartItem> cartItemList;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;


    public MyOrdersAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_order_item, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(context).load(orderList.get(position).getCartItemList().get(0).getFoodImage())
                .into(holder.img_order);

        calendar.setTimeInMillis(orderList.get(position).getCreatedDate());
        Date date = new Date(orderList.get(position).getCreatedDate());

        holder.txt_order_date.setText(new StringBuilder(Common.getDateOfWeek(calendar.get(Calendar.DAY_OF_WEEK)))
                .append(" ")
                .append(simpleDateFormat.format(date)));

        holder.txt_order_number.setText(new StringBuilder("Order number: ").append("\n").append(orderList.get(position).getOrderNumber()));
        holder.txt_order_comment.setText(new StringBuilder("Comment: ").append("\n").append(orderList.get(position).getComment()));
        holder.txt_order_status.setText(new StringBuilder("Status: ").append(Common.convertStatusToText(orderList.get(position).getOrderStatus())));

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_order_date)
        TextView txt_order_date;
        @BindView(R.id.txt_order_status)
        TextView txt_order_status;
        @BindView(R.id.txt_order_comment)
        TextView txt_order_comment;
        @BindView(R.id.txt_order_number)
        TextView txt_order_number;
        @BindView(R.id.img_order)
        ImageView img_order;

        Unbinder unbinder;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            unbinder = ButterKnife.bind(this, itemView);
        }

    }
}