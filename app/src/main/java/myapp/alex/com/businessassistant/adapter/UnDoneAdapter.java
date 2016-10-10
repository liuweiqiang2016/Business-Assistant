package myapp.alex.com.businessassistant.adapter;

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
import myapp.alex.com.businessassistant.model.OrderModel;
import myapp.alex.com.businessassistant.utils.MaqueeTextView;

public class UnDoneAdapter extends RecyclerView.Adapter<UnDoneAdapter.MyViewHolder> {

    private List<OrderModel> mList;
    private LayoutInflater mInflater;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public UnDoneAdapter(Context context, List<OrderModel> list) {
        mInflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(mInflater.inflate(
                R.layout.item_undone, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
      //  holder.id.setText(mList.get(position).getOrder_id());
        holder.time.setText(mList.get(position).getTime());
        holder.name.setText(mList.get(position).getName());
        holder.total.setText(mList.get(position).getTotal());

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
        }
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

//    public void upData(List<OrderModel> list){
//        this.mList=list;
//        this.notifyDataSetChanged();
//    };

    class MyViewHolder extends ViewHolder {

        MaqueeTextView id, time, name, total;
        LinearLayout lin;


        public MyViewHolder(View view) {
            super(view);
//            id = (TextView) view.findViewById(R.id.item_undone_id);
            time = (MaqueeTextView) view.findViewById(R.id.item_undone_time);
            name = (MaqueeTextView) view.findViewById(R.id.item_undone_name);
            total = (MaqueeTextView) view.findViewById(R.id.item_undone_total);


        }
    }
}