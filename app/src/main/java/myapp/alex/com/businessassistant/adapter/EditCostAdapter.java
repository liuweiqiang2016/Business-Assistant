package myapp.alex.com.businessassistant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.model.CostTypeModel;
import myapp.alex.com.businessassistant.utils.MaqueeTextView;

public class EditCostAdapter extends RecyclerView.Adapter<EditCostAdapter.MyViewHolder> {

    private List<CostTypeModel> mList;
    private LayoutInflater mInflater;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public EditCostAdapter(Context context, List<CostTypeModel> list) {
        mInflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(mInflater.inflate(
                R.layout.item_edit_cost, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
      //  holder.id.setText(mList.get(position).getOrder_id());
        holder.name.setText(mList.get(position).getName());
        holder.id.setText(mList.get(position).getC_id()+"");

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addData(int position,CostTypeModel model) {
        //mDatas.add(position, "Insert One");
        mList.add(position,model);
        notifyItemInserted(position);
    }
    public void addData(CostTypeModel model) {
        //mDatas.add(position, "Insert One");
        mList.add(model);
        notifyItemInserted(mList.size()-1);
    }


    public void removeData(int position) {
        //mDatas.remove(position);
        notifyItemRemoved(position);
        mList.remove(position);
    }

//    public void upData(List<OrderModel> list){
//        this.mList=list;
//        this.notifyDataSetChanged();
//    };

    class MyViewHolder extends ViewHolder {

        MaqueeTextView name;
        TextView id;
        public MyViewHolder(View view) {
            super(view);
            name= (MaqueeTextView) view.findViewById(R.id.item_cost_name);
            id= (TextView) view.findViewById(R.id.item_cost_id);

        }
    }
}