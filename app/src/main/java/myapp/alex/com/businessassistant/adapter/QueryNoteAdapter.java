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
import myapp.alex.com.businessassistant.fragment.NoteDetailFragment;
import myapp.alex.com.businessassistant.model.CustomerModel;
import myapp.alex.com.businessassistant.model.NoteModel;
import myapp.alex.com.businessassistant.utils.MaqueeTextView;

public class QueryNoteAdapter extends RecyclerView.Adapter<QueryNoteAdapter.MyViewHolder> {

    private List<NoteModel> mList;
    private Context mContext;
    private FragmentManager manager;

    public QueryNoteAdapter(Context context, List<NoteModel> list, FragmentManager fragmentManager) {
        mContext=context;
        mList = list;
        manager=fragmentManager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_note, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
      //  holder.id.setText(mList.get(position).getOrder_id());
        holder.time.setText(mList.get(position).getTime());
        holder.subject.setText(mList.get(position).getSubject());
        holder.lin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteDetailFragment fragment=NoteDetailFragment.newInstance(mList.get(position));
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

        MaqueeTextView subject,time;
        LinearLayout lin;

        public MyViewHolder(View view) {
            super(view);
            lin= (LinearLayout) view.findViewById(R.id.id_notelin);
            subject = (MaqueeTextView) view.findViewById(R.id.item_note_subject);
            time = (MaqueeTextView) view.findViewById(R.id.item_note_time);
        }
    }
}