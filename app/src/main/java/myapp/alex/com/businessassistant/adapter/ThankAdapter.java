package myapp.alex.com.businessassistant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import myapp.alex.com.businessassistant.R;

/**
 * Created by uu on 2016/9/7.
 */
public class ThankAdapter extends RecyclerView.Adapter<ThankAdapter.MyViewHolder> {

    private Context mContext;
    private String[] mLinks;
    public ThankAdapter(String[] links, Context mContext) {
        this.mLinks=links;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_thank, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {

        holder.link.setText(mLinks[position]);
    }

    @Override
    public int getItemCount() {
        return mLinks.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView link;//标题名

        public MyViewHolder(View view)
        {
            super(view);
            link = (TextView) view.findViewById(R.id.thank_item_link);
        }
    }

}
