package cn.shock.dishesclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Addactivity extends AppCompatActivity implements View.OnClickListener{

    String responsebody = "";
    EditText editText_name;
    EditText editText_price;
    EditText editText_instock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addactivity);

        Button button_add = (Button) findViewById(R.id.dishes_add);
        editText_name = (EditText) findViewById(R.id.dishes_add_name);
        editText_price = (EditText) findViewById(R.id.dishes_add_price);
        editText_instock = (EditText) findViewById(R.id.dishes_add_instock);

        button_add.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dishes_add :
                String name = editText_name.getText().toString().trim();
                int price = new Integer(editText_price.getText().toString().trim()).intValue();
                int instock = new Integer(editText_instock.getText().toString().trim()).intValue();
                adddishes(name,price,instock);
                while (true){
                    String flag = responsebody.trim();
                    try {
                        new Thread().sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(flag.equals("true")){
                        Toast.makeText(Addactivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                        responsebody = "";
                        editText_name.setText("");
                        editText_price.setText("");
                        editText_instock.setText("");
                        //线程刷新
                        editText_name.invalidate();
                        editText_price.invalidate();
                        editText_instock.invalidate();


                        break;
                    }
                    else if(!flag.equals("")){
                        Log.i("shockc","add不为空"+flag);

                        Toast.makeText(Addactivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                        responsebody = "";
                        break;
                    }
                }
                break;
        }
    }

    //添加请求
    public  void adddishes(String name,int price , int instock){

        String weatherUrl = "http://"+getResources().getString(R.string.localhost)+":8080/OrderWeb/AddDishes?name="+name+"&price="+price+"&instock="+instock;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responsebody = response.body().string();
                Log.i("shockc",responsebody);
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.i("shockc","网络出错");

            }
        });

    }
}
