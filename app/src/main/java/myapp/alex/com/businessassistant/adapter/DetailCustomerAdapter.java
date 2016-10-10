package myapp.alex.com.businessassistant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.utils.MaqueeTextView;

/**
 * Created by uu on 2016/9/7.
 */
public class DetailCustomerAdapter extends RecyclerView.Adapter<DetailCustomerAdapter.MyViewHolder> {

    private Context mContext;
    private String[] mInfos;
    private String[] mDatas;

    public DetailCustomerAdapter(String[] infos, String[] datas, Context mContext) {
        this.mInfos=infos;
        this.mDatas=datas;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_detail_customer, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {

        holder.dc_name.setText(mInfos[position]);
        holder.dc_namedata.setText(mDatas[position]);
    }

    @Override
    public int getItemCount() {
        return mInfos.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView dc_name;//客户名称
        MaqueeTextView dc_namedata;//填写项

        public MyViewHolder(View view)
        {
            super(view);
            dc_name = (TextView) view.findViewById(R.id.dc_name);
            dc_namedata= (MaqueeTextView) view.findViewById(R.id.dc_name_data);
        }
    }

}
