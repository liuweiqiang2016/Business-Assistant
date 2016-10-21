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
public class DeleteCostFragment extends DialogFragment {


    MaqueeTextView delete_mt_name,delete_mt_id,delete_mt_price;
    private String mName,mId;
    private int mPosition;

    private static final String NAME = "param1";
    private static final String C_ID = "param2";
    private static final String POSITION = "param4";


    // TODO: Rename and change types and number of parameters
    public static DeleteCostFragment newInstance(String mName, String mId, int mPosition) {
        DeleteCostFragment fragment = new DeleteCostFragment();
        Bundle args = new Bundle();
        args.putString(NAME,mName);
        args.putString(C_ID,mId);
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
            mPosition=getArguments().getInt(POSITION);
        }
    }
    public DeleteCostFragment() {
    }
    //删除开销后，回调
    public interface DeleteCostListener
    {
        void onDeleteCostComplete(int position);
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
        View view = inflater.inflate(R.layout.fragment_deletecost_dialog, null);
        delete_mt_name= (MaqueeTextView) view.findViewById(R.id.delete_mt_name);
        delete_mt_id= (MaqueeTextView) view.findViewById(R.id.delete_mt_id);

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
                                DeleteCostListener listener= (DeleteCostListener) getActivity();
                                listener.onDeleteCostComplete(mPosition);
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

    };

}
