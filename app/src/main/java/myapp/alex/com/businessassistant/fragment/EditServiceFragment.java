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
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Field;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.utils.MaqueeTextView;

/**
 * Created by liuweiqiang on 2016/9/8.
 */
public class EditServiceFragment extends DialogFragment {


    private EditText et_editorder_price;
    MaqueeTextView mt_name,mt_id;
    private String mName,mId,mPrice;
    private int mPosition;

    private static final String NAME = "param1";
    private static final String C_ID = "param2";
    private static final String PRICE = "param3";
    private static final String POSITION = "param4";


    // TODO: Rename and change types and number of parameters
    public static EditServiceFragment newInstance(String mName, String mStart, String mEnd, int mPosition) {
        EditServiceFragment fragment = new EditServiceFragment();
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
    public EditServiceFragment() {
    }
    //输入完毕后，保存按钮回调处理方法
    //传入参数为：单价，修改项的position
    public interface EditOrderListener
    {
        void onEditPriceComplete(String edit_price,int position);
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
        View view = inflater.inflate(R.layout.fragment_editservice_dialog, null);
        mt_name= (MaqueeTextView) view.findViewById(R.id.mt_name);
        mt_id= (MaqueeTextView) view.findViewById(R.id.mt_id);
        et_editorder_price= (EditText) view.findViewById(R.id.et_editorder_price);

        initData();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                //单价不能为空
                                if (et_editorder_price.getText().toString().trim().equals("")){
                                    Toast.makeText(getActivity(),"单价不能为空！",Toast.LENGTH_SHORT).show();

                                    try
                                    {
                                        Field field = dialog.getClass()
                                                .getSuperclass().getDeclaredField(
                                                        "mShowing" );
                                        field.setAccessible( true );
                                        // 将mShowing变量设为false，表示对话框已关闭
                                        field.set(dialog, false );
                                        dialog.dismiss();
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }else{
                                    EditOrderListener listener= (EditOrderListener) getActivity();
                                    listener.onEditPriceComplete(et_editorder_price.getText().toString(),mPosition);
                                    try
                                    {
                                        Field field = dialog.getClass()
                                                .getSuperclass().getDeclaredField(
                                                        "mShowing" );
                                        field.setAccessible( true );
                                        // 将mShowing变量设为false，表示对话框已关闭
                                        field.set(dialog, true );
                                        dialog.dismiss();
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try
                {
                    Field field = dialog.getClass()
                            .getSuperclass().getDeclaredField(
                                    "mShowing" );
                    field.setAccessible( true );
                    // 将mShowing变量设为false，表示对话框已关闭
                    field.set(dialog, true );
                    dialog.dismiss();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        return builder.create();

    }

    //赋值数据
    void initData(){

        mt_name.setText(mName);
        mt_id.setText(mId);
        et_editorder_price.setText(mPrice);

    };

}
