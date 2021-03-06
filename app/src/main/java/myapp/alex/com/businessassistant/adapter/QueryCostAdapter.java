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
import myapp.alex.com.businessassistant.fragment.CostDetailFragment;
import myapp.alex.com.businessassistant.model.CostModel;
import myapp.alex.com.businessassistant.utils.MaqueeTextView;

public class QueryCostAdapter extends RecyclerView.Adapter<QueryCostAdapter.MyViewHolder> {

    private List<CostModel> mList;
    private Context mContext;
    private FragmentManager manager;

    public QueryCostAdapter(Context context, List<CostModel> list,FragmentManager fragmentManager) {
        mContext=context;
        mList = list;
        manager=fragmentManager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_cost, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
      //  holder.id.setText(mList.get(position).getOrder_id());
        holder.time.setText(mList.get(position).getTime());
        holder.name.setText(mList.get(position).getName());
        holder.total.setText(mList.get(position).getMoney());
        holder.lin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CostDetailFragment fragment=CostDetailFragment.newInstance(mList.get(position));
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

        MaqueeTextView id, time, name, total;
        LinearLayout lin;


        public MyViewHolder(View view) {
            super(view);
//            id = (TextView) view.findViewById(R.id.item_undone_id);
            lin= (LinearLayout) view.findViewById(R.id.id_costlin);
            time = (MaqueeTextView) view.findViewById(R.id.item_cost_time);
            name = (MaqueeTextView) view.findViewById(R.id.item_cost_name);
            total = (MaqueeTextView) view.findViewById(R.id.item_cost_total);
        }
    }
}