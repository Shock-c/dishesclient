package cn.shock.dishesclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        starservice();
        //获取按钮
        Button caipin = (Button) findViewById(R.id.caipin);
        Button caidan = (Button) findViewById(R.id.caidan);

        //点击响应
        caipin.setOnClickListener(this);
        caidan.setOnClickListener(this);
    }

    //开启服务方法（）
    public void starservice(){
        Intent startIntent = new Intent(this, MyService.class);
        startService(startIntent);
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.caipin :
                //跳转活动界面
                Intent intent = new Intent(MainActivity.this, canpinsetting.class);
                startActivity(intent);
                break;
            case R.id.caidan :
                Intent intent0 = new Intent(MainActivity.this, Tablelist.class);
                startActivity(intent0);
                break;
            default:break;

        }
    }



}
