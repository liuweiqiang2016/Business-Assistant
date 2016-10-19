package myapp.alex.com.businessassistant.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.utils.MaqueeTextView;

/**
 * Created by liuweiqiang on 2016/9/8.
 */
public class DeleteServiceFragment extends DialogFragment {


    MaqueeTextView delete_mt_name,delete_mt_id,delete_mt_price;
    private String mName,mId,mPrice;
    private int mPosition;

    private static final String NAME = "param1";
    private static final String C_ID = "param2";
    private static final String PRICE = "param3";
    private static final String POSITION = "param4";


    // TODO: Rename and change types and number of parameters
    public static DeleteServiceFragment newInstance(String mName, String mStart, String mEnd, int mPosition) {
        DeleteServiceFragment fragment = new DeleteServiceFragment();
        Bundle args = new Bundle();
        args.putString(NAME,mName);
        args.putString(C_ID,mStart);
        args.putString(PRICE, mEnd);
        args.putInt(POSITION,mPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(NAME);
            mId = getArguments().getString(C_ID);
            mPrice = getArguments().getString(PRICE);
            mPosition=getArguments().getInt(POSITION);
        }
    }
    public DeleteServiceFragment() {
    }
    //输入完毕后，保存按钮回调处理方法
    //传入参数为：单价，修改项的position
    public interface DeleteServiceListener
    {
        void onDeleteServiceComplete(int position);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //去除最上方标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        // Get the layout inflateri
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_deleteservice_dialog, null);
        delete_mt_name= (MaqueeTextView) view.findViewById(R.id.delete_mt_name);
        delete_mt_id= (MaqueeTextView) view.findViewById(R.id.delete_mt_id);
        delete_mt_price= (MaqueeTextView) view.findViewById(R.id.delete_mt_price);

        initData();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("删除",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                DeleteServiceListener listener= (DeleteServiceListener) getActivity();
                                listener.onDeleteServiceComplete(mPosition);
                                }

                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder.create();

    }

    //赋值数据
    void initData(){

        delete_mt_name.setText(mName);
        delete_mt_id.setText(mId);
        delete_mt_price.setText(mPrice);

    };

}
