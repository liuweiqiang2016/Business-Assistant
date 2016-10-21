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
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.utils.FuncUtils;

/**
 * Created by liuweiqiang on 2016/9/8.
 */
public class AddCostFragment extends DialogFragment {


    private EditText et_addservice_name;
    private TextView tv_addservice_id;
    private String mId;
    private ArrayList<String> mNames;

    private static final String MID = "param1";
    private static final String MNAMES = "param2";
    // TODO: Rename and change types and number of parameters
    public static AddCostFragment newInstance(String mId, ArrayList<String> mNames) {
        AddCostFragment fragment = new AddCostFragment();
        Bundle args = new Bundle();
        args.putString(MID,mId);
        args.putStringArrayList(MNAMES,mNames);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getString(MID);
            mNames=getArguments().getStringArrayList(MNAMES);
        }
    }
    public AddCostFragment() {
    }
    //输入完毕后，保存按钮回调处理方法
    //传入参数为：客户名称、开始时间、结束时间
    public interface AddCostListener
    {
        void onAddCostComplete(String name,String c_id);
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
        View view = inflater.inflate(R.layout.fragment_addcost_dialog, null);
        et_addservice_name= (EditText) view.findViewById(R.id.et_addservice_name);
        tv_addservice_id= (TextView) view.findViewById(R.id.tv_addservice_id);

        tv_addservice_id.setText(mId);
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
                                if (et_addservice_name.getText().toString().trim().equals("")){
                                    FuncUtils.showToast(getActivity(),"开销名称不能为空！");
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

                                    if (mNames.contains(et_addservice_name.getText().toString().trim())){
                                        FuncUtils.showToast(getActivity(),"开销名称已存在！");
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
                                    }else {

                                        AddCostListener listener= (AddCostListener) getActivity();
                                            listener.onAddCostComplete(et_addservice_name.getText().toString().trim(),mId);
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


}
