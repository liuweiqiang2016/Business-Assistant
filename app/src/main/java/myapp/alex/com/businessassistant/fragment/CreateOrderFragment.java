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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zeone.framework.db.sqlite.DbUtils;
import com.zeone.framework.db.sqlite.Selector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.model.ServiceModel;
import myapp.alex.com.businessassistant.utils.MyDbUtils;

/**
 * Created by uu on 2016/9/8.
 */
public class CreateOrderFragment extends DialogFragment {


    private Spinner spinner;
    private TextView tv_price;
    private EditText et_num;

    List<ServiceModel> modelList;

    //输入完毕后，保存按钮回调处理方法
    //传入参数为：服务编号、单价、数量
    public interface CreateInputListener
    {
        void onCreateInputComplete(ServiceModel model, String num);
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
        View view = inflater.inflate(R.layout.fragment_order_dialog, null);
        spinner= (Spinner) view.findViewById(R.id.sp_service);
        tv_price= (TextView) view.findViewById(R.id.tv_price);
        et_num= (EditText) view.findViewById(R.id.et_num);

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
                                String num=et_num.getText().toString();
                                if (num.trim().equals("")){
                                    Toast.makeText(getActivity(),"数量不能为空！",Toast.LENGTH_SHORT).show();

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
                                    CreateInputListener listener= (CreateInputListener) getActivity();
                                    int position=spinner.getSelectedItemPosition();
                                    listener.onCreateInputComplete(modelList.get(position),num);
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

    //spinner赋值数据
    void initData(){

        DbUtils db= MyDbUtils.getInstance().Db(getActivity());
        modelList = db.findAll(Selector.from(ServiceModel.class).where("C_Show","=","1"));
        final List<String> list=new ArrayList<>();
        list.clear();
        if (modelList!=null) {
            for (int i=0;i<modelList.size();i++){
                list.add(modelList.get(i).getName());
            };
        }else{
            return;
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_price.setText(modelList.get(position).getPrice()+"元");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    };

}
