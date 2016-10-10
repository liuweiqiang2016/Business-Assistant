package myapp.alex.com.businessassistant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.model.OrderServiceModel;

/**
 * Created by uu on 2016/9/7.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private List<OrderServiceModel> mList=new ArrayList<>();
    private Context mContext;

    public OrderAdapter(List<OrderServiceModel> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_orderlist, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {

        holder.name.setText(mList.get(position).getName());
        holder.price.setText(mList.get(position).getPrice()+"");
        holder.num.setText(mList.get(position).getNum()+"");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView name;//项目名称
        TextView num;//项目数量
        TextView price;//项目单价


        public MyViewHolder(View view)
        {
            super(view);
            name = (TextView) view.findViewById(R.id.service_name);
            num = (TextView) view.findViewById(R.id.service_num);
            price = (TextView) view.findViewById(R.id.service_price);
        }
    }

}
