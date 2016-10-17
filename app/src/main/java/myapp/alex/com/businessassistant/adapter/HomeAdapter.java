package myapp.alex.com.businessassistant.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import myapp.alex.com.businessassistant.R;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    //	private List<String> mDatas;
    private LayoutInflater mInflater;

    private String[] mItems_txt;
    private int[] mItems_img;
    private int mTotal=0;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


//	public HomeAdapter(Context context, List<String> datas)
//	{
//		mInflater = LayoutInflater.from(context);
//		mDatas = datas;
//	}

    public HomeAdapter(Context context, String[] items_txt, int[] items_img,int total) {
        mInflater = LayoutInflater.from(context);
        mItems_img = items_img;
        mItems_txt = items_txt;
        mTotal=total;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(mInflater.inflate(
                R.layout.item_home_undone, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv.setText(mItems_txt[position]);
        holder.imageView.setImageResource(mItems_img[position]);
        if (position==1){
            if (mTotal>0){
                holder.total.setText(mTotal+"");
            }else{
                holder.total.setText("");
            }
        }
//        if (position==9){
//            if (mTotal>0){
//                holder.total.setTextSize(12);
//                holder.total.setText("新");
//            }else{
//                holder.total.setText("");
//            }
//        }

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
            //长按操作，暂时不处理
//			holder.itemView.setOnLongClickListener(new OnLongClickListener()
//			{
//				@Override
//				public boolean onLongClick(View v)
//				{
//					int pos = holder.getLayoutPosition();
//					mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
//					removeData(pos);
//					return false;
//				}
//			});
        }
    }

    @Override
    public int getItemCount() {
        return mItems_txt.length;
    }

    public void addData(int position) {
        //mDatas.add(position, "Insert One");
        notifyItemInserted(position);
    }
    public void upData(int position,int total) {
        //mDatas.add(position, "Insert One");
        mTotal=total;
        notifyItemChanged(position);
    }


    public void removeData(int position) {
        //mDatas.remove(position);
        notifyItemRemoved(position);
    }

    class MyViewHolder extends ViewHolder {

        TextView tv, total;
        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.id_tv);
            imageView = (ImageView) view.findViewById(R.id.id_img);
            total = (TextView) view.findViewById(R.id.id_total);


        }
    }
}