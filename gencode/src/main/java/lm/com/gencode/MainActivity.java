package lm.com.gencode;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import lm.com.gencode.account.AccountManager;


/**
 * Created by lihaifeng on 16/11/6.
 * Desc :
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_common).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btn = (Button)view;
                EditOneActivity.launch(MainActivity.this,EditOneActivity.TYPE_COMMON,btn.getText().toString());
            }
        });

        findViewById(R.id.btn_inner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btn = (Button)view;
                EditOneActivity.launch(MainActivity.this,EditOneActivity.TYPE_INNER,btn.getText().toString());
            }
        });

        findViewById(R.id.btn_fenji).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditTwoActivity.launch(MainActivity.this);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_login){
            String token = AccountManager.getToken(getApplicationContext());
            if(TextUtils.isEmpty(token)){
                LoginActivity.launch(this);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);

        String token = AccountManager.getToken(getApplicationContext());
        if(!TextUtils.isEmpty(token)){
            menu.findItem(R.id.action_login).setTitle("欢迎使用");
        }


        return true;
    }
}
