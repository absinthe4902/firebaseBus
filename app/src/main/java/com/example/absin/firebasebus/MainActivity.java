
package com.example.absin.firebasebus;
 import android.content.Intent;
 import android.provider.ContactsContract;
 import android.support.v7.app.AppCompatActivity;
 import android.os.Bundle;
 import android.text.Html;
        import android.view.MotionEvent;
        import android.view.View;
 import android.widget.ArrayAdapter;
 import android.widget.ImageView;
        import android.widget.LinearLayout;
 import android.widget.ListView;
 import android.widget.TextView;
        import android.widget.Toast;

 import java.io.File;
 import java.io.FileReader;
 import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.net.URLEncoder;
        import java.io.BufferedReader;
        import java.io.IOException;
 import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private String fileName = "star.list";
    MyAdapter_star adapter_star;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView)findViewById(R.id.main_list);
        adapter_star = new MyAdapter_star(this, MainActivity.this );
        loadFile();


        //즐겨찾기 기능 추가(미완)
        /*
        final ArrayList<String> names = new ArrayList<>();

        Intent intent2 = getIntent();
        String  name = intent2.getStringExtra("RouteName"); //인텐트로 버스이름 받아오기
        names.add(name); //추가

        //이부분에서 에러남
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, names);
        list.setAdapter(adapter2);//리스트에 추가
        adapter2.clear();
        adapter2.addAll(names);

        adapter2.notifyDataSetChanged();
*/
        /*텍스트 뷰에 줄 넣으려고*/
        TextView textView = (TextView) findViewById(R.id.main_search);
        LinearLayout go_search = (LinearLayout) findViewById(R.id.go_to_search); //검색 화면으로 가기 위해서 이 화면을 클릭해야한다

        final LinearLayout main_map = (LinearLayout) findViewById(R.id.main_tap_map); //final은 내부 클래스에서 동작을 해야할때 써야된다는데 혹시 어떻게 될지 몰라서 써두었다.
        final LinearLayout main_home = (LinearLayout) findViewById(R.id.main_tap_home);
        final LinearLayout main_memo = (LinearLayout) findViewById(R.id.main_tap_secretary); //메모장
        final LinearLayout main_alarm = (LinearLayout) findViewById(R.id.main_tap_alarm);

//        String sitename = "검색창 수정예정";
//        textView.setText(Html.fromHtml("<u>" + sitename + "</u>"));


        //검색창을 누르면 검색 화면으로 넘어가는 코드
        go_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), search_barJ.class);
                startActivity(intent);
            }
        });

        //텍스트 부분 누르면 안 넘어가져서 넘어가게 따로 만듬.
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), search_barJ.class);
                startActivity(intent);
            }
        });

        //메모 이동
        main_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), memo.class);
                startActivity(intent);
            }
        });



        //메인 화면에 저장한 메모 뜨게 하기 (새로고침이 필요할듯)
        TextFileManager mTextFileManager = new TextFileManager(MainActivity.this);
        String memoData = mTextFileManager.load(); //저장한 데이터를 불러온다
        TextView mMemo = (TextView) findViewById(R.id.main_memo);
        mMemo.setText(memoData); //해당 데이터를 메인화면에 뜨게 한다


        main_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        main_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
                startActivity(intent);
            }
        });


    }

    public void settingClick(View v) {
        Intent intent = new Intent(getApplicationContext(), setting.class);
        startActivity(intent);
    }

    private void loadFile() {
        File file = new File(getFilesDir(), fileName);
        FileReader fr = null;
        BufferedReader bufrd = null;
        String str;

        if (file.exists()) {
            try {
                fr = new FileReader(file);
                bufrd = new BufferedReader(fr);

                while ((str = bufrd.readLine()) != null) {
                    adapter_star.addItem(str);
                }

                bufrd.close();
                fr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        list.setAdapter(adapter_star);

    }
}