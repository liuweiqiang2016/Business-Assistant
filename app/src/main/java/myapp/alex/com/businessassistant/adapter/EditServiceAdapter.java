package myapp.alex.com.businessassistant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.List;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.model.ServiceModel;
import myapp.alex.com.businessassistant.utils.MaqueeTextView;

public class EditServiceAdapter extends RecyclerView.Adapter<EditServiceAdapter.MyViewHolder> {

    private List<ServiceModel> mList;
    private LayoutInflater mInflater;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public EditServiceAdapter(Context context, List<ServiceModel> list) {
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
        holder.name.setText(mList.get(position).getName());
        holder.id.setText(mList.get(position).getC_id()+"");
        holder.price.setText(mList.get(position).getPrice()+"");

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

    public void addData(int position,ServiceModel model) {
        //mDatas.add(position, "Insert One");
        mList.add(position,model);
        notifyItemInserted(position);
    }
    public void addData(ServiceModel model) {
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

        MaqueeTextView id,name,price;
        public MyViewHolder(View view) {
            super(view);
            name = (MaqueeTextView) view.findViewById(R.id.item_undone_time);
            price = (MaqueeTextView) view.findViewById(R.id.item_undone_name);
            id = (MaqueeTextView) view.findViewById(R.id.item_undone_total);


        }
    }
}