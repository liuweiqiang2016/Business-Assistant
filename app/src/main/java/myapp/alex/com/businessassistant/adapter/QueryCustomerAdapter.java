package myapp.alex.com.businessassistant.adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.fragment.CustomerDetailFragment;
import myapp.alex.com.businessassistant.model.CustomerModel;
import myapp.alex.com.businessassistant.utils.MaqueeTextView;

public class QueryCustomerAdapter extends RecyclerView.Adapter<QueryCustomerAdapter.MyViewHolder> {

    private List<CustomerModel> mList;
    private Context mContext;
    private FragmentManager manager;

    public QueryCustomerAdapter(Context context, List<CustomerModel> list, FragmentManager fragmentManager) {
        mContext=context;
        mList = list;
        manager=fragmentManager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_customer_query, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
      //  holder.id.setText(mList.get(position).getOrder_id());
        if (mList.get(position).getSource().equals("0")){
            holder.source.setText("订单生成");
        }else{
            holder.source.setText("自主添加");
        }
        holder.name.setText(mList.get(position).getName());
        holder.tel.setText(mList.get(position).getTel());
        holder.lin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerDetailFragment fragment=CustomerDetailFragment.newInstance(mList.get(position));
                fragment.show(manager,"tag");
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mList==null){
            return 0;
        }else {
            return mList.size();
        }
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

        MaqueeTextView source, name, tel;
        LinearLayout lin;


        public MyViewHolder(View view) {
            super(view);
            lin= (LinearLayout) view.findViewById(R.id.id_customerlin);
            source = (MaqueeTextView) view.findViewById(R.id.item_customer_source);
            name = (MaqueeTextView) view.findViewById(R.id.item_customer_name);
            tel = (MaqueeTextView) view.findViewById(R.id.item_customer_tel);
        }
    }
}