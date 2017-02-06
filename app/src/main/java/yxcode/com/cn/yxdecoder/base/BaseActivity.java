package yxcode.com.cn.yxdecoder.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import yxcode.com.cn.yxdecoder.R;


public abstract class BaseActivity extends AppCompatActivity {

  private LinearLayout rootLayout;
  private TextView titleView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    super.setContentView(R.layout.activity_base);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
      localLayoutParams.flags =
          (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
    }
    initToolbar();
  }

  private void initToolbar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    if (null != toolbar) {
      setSupportActionBar(toolbar);

    }
    titleView = (TextView) findViewById(R.id.toolbarTitle);
    if(null != titleView){
      CharSequence label = getTitle();
      if(null != label){
        titleView.setText(label);
      }
      super.setTitle("");
    }
    showBackArray(true);
  }

  @Override
  public void setContentView(int layoutId) {
    setContentView(View.inflate(this, layoutId, null));
  }

  @Override
  public void setContentView(View view) {
    rootLayout = (LinearLayout) findViewById(R.id.root_layout);
    if (null == rootLayout) return;
    rootLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT));
  }

  // Show or Hide toolbar back button.
  public void showBackArray(boolean b) {
    getSupportActionBar().setDisplayHomeAsUpEnabled(b);
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return super.onSupportNavigateUp();
  }

  @Override
  public void setTitle(CharSequence title) {
    titleView.setText(title);
  }

  @Override
  public void setTitle(int titleId) {
    titleView.setText(titleId);
  }

  @Override
  public void setTitleColor(int textColor) {
    titleView.setTextColor(textColor);
  }
}
