package com.linshengt.newsapplication.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linshengt.newsapplication.R;
import com.linshengt.newsapplication.Utils.PreferenceUtil;
import com.linshengt.newsapplication.View.CustomCheckBox;
import com.linshengt.newsapplication.View.TitleBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class TextSizeFragment extends Fragment {
    private TitleBar titleBar;
    private Context context;
    private View rootView;
    private TextView textView;
    private CustomCheckBox customCheckBox;
    private String TAG = "TextSizeFragment";

    public TextSizeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();

        rootView =  inflater.inflate(R.layout.fragment_text_size, null);
        findView();
        initView();
        return rootView;
    }


    private void findView(){
        titleBar = (TitleBar) rootView.findViewById(R.id.titleBar);
        customCheckBox = (CustomCheckBox) rootView.findViewById(R.id.customCheckBox);
        textView = (TextView) rootView.findViewById(R.id.textView);
    }

    private void initView(){
        titleBar.setCommonTitle(View.VISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
        titleBar.setTitleBarTile(context.getString(R.string.TextSize));
        titleBar.setTv_left(context.getString(R.string.reverseBack));

        titleBar.setOnTv_leftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        if(PreferenceUtil.readInt(context, "CustuomCheckBoxStatus") == 1){
            customCheckBox.setmCustuomCheckBoxStatus(CustomCheckBox.CustuomCheckBoxStatus.START);
            textView.setTextSize(10);
            context.setTheme(R.style.littleTextSize);
        }else if(PreferenceUtil.readInt(context, "CustuomCheckBoxStatus") == 2){
            customCheckBox.setmCustuomCheckBoxStatus(CustomCheckBox.CustuomCheckBoxStatus.THIRD);
            textView.setTextSize(12);
            context.setTheme(R.style.middleTextSize);
        }else if(PreferenceUtil.readInt(context, "CustuomCheckBoxStatus") == 3){
            customCheckBox.setmCustuomCheckBoxStatus(CustomCheckBox.CustuomCheckBoxStatus.TWO_THIRD);
            textView.setTextSize(15);
            context.setTheme(R.style.largeTextSize);
        }else if(PreferenceUtil.readInt(context, "CustuomCheckBoxStatus") == 4){
            customCheckBox.setmCustuomCheckBoxStatus(CustomCheckBox.CustuomCheckBoxStatus.END);
            textView.setTextSize(18);
            context.setTheme(R.style.SlargeTextSize);
        }

        customCheckBox.setOnClickListener(new CustomCheckBox.onClickListener() {
            @Override
            public void setOnclickListener(CustomCheckBox.CustuomCheckBoxStatus Status) {
                if(CustomCheckBox.CustuomCheckBoxStatus.START == Status){
                    PreferenceUtil.write(context, "CustuomCheckBoxStatus", 1);
                    textView.setTextSize(10);
                }else if(CustomCheckBox.CustuomCheckBoxStatus.THIRD == Status){
                    PreferenceUtil.write(context, "CustuomCheckBoxStatus", 2);
                    textView.setTextSize(12);
                }else if(CustomCheckBox.CustuomCheckBoxStatus.TWO_THIRD == Status){
                    PreferenceUtil.write(context, "CustuomCheckBoxStatus", 3);
                    textView.setTextSize(15);
                }else if(CustomCheckBox.CustuomCheckBoxStatus.END == Status){
                    PreferenceUtil.write(context, "CustuomCheckBoxStatus", 4);
                    textView.setTextSize(18);
                }

                   PreferenceUtil.write(context, "CustuomCheckBoxStatusIsChange", true);

            }
        });
    }

}
