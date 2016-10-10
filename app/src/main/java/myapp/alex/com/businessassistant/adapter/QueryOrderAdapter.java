package myapp.alex.com.businessassistant.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.activity.OrderDetailActivity;
import myapp.alex.com.businessassistant.model.OrderModel;
import myapp.alex.com.businessassistant.utils.MaqueeTextView;

public class QueryOrderAdapter extends RecyclerView.Adapter<QueryOrderAdapter.MyViewHolder> {

    private List<OrderModel> mList;
    private Context mContext;

    public QueryOrderAdapter(Context context, List<OrderModel> list) {
        mContext=context;
        mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_undone, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
      //  holder.id.setText(mList.get(position).getOrder_id());
        holder.time.setText(mList.get(position).getTime());
        holder.name.setText(mList.get(position).getName());
        holder.total.setText(mList.get(position).getTotal());
        holder.lin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("orderid", mList.get(position).getOrder_id());
                //tag 0:UnDoneActivity 1:QueryOrderActivity
                intent.putExtra("tag", "1");
                intent.setClass(mContext, OrderDetailActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addData(int position) {
        //mDatas.add(position, "Insert One");
        notifyItemInserted(position);
    }


    public void removeData(int position) {
        //mDatas.remove(position);
        notifyItemRemoved(position);
    }

    class MyViewHolder extends ViewHolder {

        MaqueeTextView id, time, name, total;
        LinearLayout lin;


        public MyViewHolder(View view) {
            super(view);
//            id = (TextView) view.findViewById(R.id.item_undone_id);
            lin= (LinearLayout) view.findViewById(R.id.id_undonelin);
            time = (MaqueeTextView) view.findViewById(R.id.item_undone_time);
            name = (MaqueeTextView) view.findViewById(R.id.item_undone_name);
            total = (MaqueeTextView) view.findViewById(R.id.item_undone_total);


        }
    }
}