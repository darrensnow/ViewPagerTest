package com.example.darren.viewpagertest.devicesadd;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.darren.viewpagertest.R;

import java.util.List;

public class AddDeviceAdapter extends RecyclerView.Adapter<AddDeviceAdapter.MyHolder> {

    private static final String TAG = "AddDeviceAdapter";

    private Context context;
    private List<String> list;

    public AddDeviceAdapter(Context context, List<String> list){
        this.context = context;
        this.list = list;
        Log.d(TAG,"list:" + list);
    }


    public void update(List<String> list){
        this.list = list;
        Log.d(TAG,"list after update:" + list);
        notifyDataSetChanged();
    }

    /**
     *这个方法是创建viewholder的
     * 就是将xml传入给viewholder
     */
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.add_device_item,parent,false);
        Log.d(TAG,"view" + view);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    /**
     * 该方法为我们操作item的地方
     **/
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        TextView textView = holder.device_name;
        textView.setText(list.get(position));
        Log.d(TAG,"position:" + position);
        Log.d(TAG,"text:" + list.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.device_name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d(TAG,"item count" +list.size());
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView device_name;

        public MyHolder(View itemView) {
            super(itemView);
            device_name = itemView.findViewById(R.id.device_name);
        }
    }
}
