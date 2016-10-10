package myapp.alex.com.businessassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.model.OrderModel;
import myapp.alex.com.businessassistant.utils.MaqueeTextView;

/**
 * Created by liuweiqiang on 2016/9/18.
 */
public class QueryListAdapter extends BaseAdapter {

    private List<OrderModel> mList;
    private LayoutInflater mInflater;

    public QueryListAdapter(List<OrderModel> list,Context context) {
        this.mList = list;
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_undone, null);
            holder = new ViewHolder();
            holder.time= (MaqueeTextView) convertView.findViewById(R.id.item_undone_time);
            holder.name= (MaqueeTextView) convertView.findViewById(R.id.item_undone_name);
            holder.total= (MaqueeTextView) convertView.findViewById(R.id.item_undone_total);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OrderModel model=mList.get(position);
        holder.time.setText(model.getTime());
        holder.name.setText(model.getName());
        holder.total.setText(model.getTotal());
        return convertView;
    }

    public class ViewHolder {
        MaqueeTextView  time, name, total;
    }

}
