package myapp.alex.com.businessassistant.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import myapp.alex.com.businessassistant.R;
import myapp.alex.com.businessassistant.progress.NexusRotationCrossDrawable;

/**
 * Created by liuweiqiang on 2016-10-18 10:39:06.
 * 版本升级
 */
public class CheckVersionFragment extends DialogFragment {


    private TextView title;
    private ProgressBar mProgressBar;
//    int type;
    String head;
    String foot;
    private static final String HEAD = "param2";
    private static final String FOOT = "param3";

    // TODO: Rename and change types and number of parameters
    public static CheckVersionFragment newInstance(String head, String foot) {
        CheckVersionFragment fragment = new CheckVersionFragment();
        Bundle args = new Bundle();
        args.putString(HEAD,head);
        args.putString(FOOT,foot);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            head=getArguments().getString(HEAD);
            foot=getArguments().getString(FOOT);
        }
    }
    public CheckVersionFragment() {
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
        View view = inflater.inflate(R.layout.fragment_check_dialog,null);
        title= (TextView) view.findViewById(R.id.check_title);
        mProgressBar= (ProgressBar) view.findViewById(R.id.google_progress);

        title.setText(head);

        Rect bounds = mProgressBar.getIndeterminateDrawable().getBounds();
        mProgressBar.setIndeterminateDrawable(getProgressDrawable());
        mProgressBar.getIndeterminateDrawable().setBounds(bounds);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);
                // Add action buttons
//                .setPositiveButton(foot,
//                        new DialogInterface.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id)
//                            {
////                                SoftUpdateListener listener= (SoftUpdateListener) getActivity();
////                                listener.onSoftUpdate(versionInfoModel.getLink());
//
//                            }
//
//                        });
        return builder.create();

    }

    Drawable getProgressDrawable(){

        Drawable progressDrawable = null;

        progressDrawable = new NexusRotationCrossDrawable.Builder(getActivity())
                .colors(getProgressDrawableColors())
                .build();

        return progressDrawable;

    };

    private int[] getProgressDrawableColors() {
        int[] colors = new int[4];
        colors[0] = getResources().getColor(R.color.red);
        colors[1] = getResources().getColor(R.color.blue);
        colors[2] = getResources().getColor(R.color.yellow);
        colors[3] = getResources().getColor(R.color.green);
        return colors;
    }

}
