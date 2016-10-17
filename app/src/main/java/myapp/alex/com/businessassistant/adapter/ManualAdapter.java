package myapp.alex.com.businessassistant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.utils.MaqueeTextView;

/**
 * Created by uu on 2016/9/7.
 */
public class ManualAdapter extends RecyclerView.Adapter<ManualAdapter.MyViewHolder> {

    private Context mContext;
    private String[] mTitles;
    private String[] mContents;
    public ManualAdapter(String[] titles, String[] contents, Context mContext) {
        this.mTitles=titles;
        this.mContents=contents;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_manual, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {

        holder.title.setText(mTitles[position]);
        holder.content.setText(mContents[position]);
    }

    @Override
    public int getItemCount() {
        return mTitles.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView title;//标题名
        TextView content;//内容

        public MyViewHolder(View view)
        {
            super(view);
            title = (TextView) view.findViewById(R.id.manual_item_title);
            content= (TextView) view.findViewById(R.id.manual_item_content);
        }
    }

}
