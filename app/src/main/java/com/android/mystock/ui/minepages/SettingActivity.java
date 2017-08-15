package com.android.mystock.ui.minepages;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.mystock.R;
import com.android.mystock.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.top_title)
    TextView topTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        topTitle.setText(R.string.mine_setting);

    }

    @OnClick({R.id.rl1, R.id.rl2, R.id.rl3, R.id.rl4, R.id.rl5, R.id.rl6, R.id.rl7, R.id.rl8, R.id.rl9, R.id.rl10, R.id.top_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl1:
                break;
            case R.id.rl2:
                break;
            case R.id.rl3:
                break;
            case R.id.rl4:
                break;
            case R.id.rl5:
                break;
            case R.id.rl6:
                break;
            case R.id.rl8:
                break;
            case R.id.rl9:
                break;
            case R.id.rl10:
                break;
            case R.id.top_back:
                finish();
                break;
        }
    }
}
