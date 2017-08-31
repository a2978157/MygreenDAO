package retrofit.mifeng.us.mygreendao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private StudentMsgBeanDao msgBeanDao;
    private DaoSession daoSession;
    private ListView lv;
    private List<StudentMsgBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //初始化
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "student.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();
        //得到数据库
        msgBeanDao = daoSession.getStudentMsgBeanDao();
    }

    private void initView() {
        Button zeng = (Button) findViewById(R.id.zeng);
        Button shan = (Button) findViewById(R.id.shan);
        Button gai = (Button) findViewById(R.id.gai);
        Button cha = (Button) findViewById(R.id.cha);
        lv = (ListView) findViewById(R.id.lv);
        zeng.setOnClickListener(this);
        shan.setOnClickListener(this);
        gai.setOnClickListener(this);
        cha.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zeng:
                //增加
                StudentMsgBean studentMsgBean = new StudentMsgBean();
                studentMsgBean.setName("zone");
                studentMsgBean.setStudentNum("123456");
                msgBeanDao.insert(studentMsgBean);
                break;
            case R.id.shan:
                //删除
                List<StudentMsgBean> list2 = msgBeanDao.queryBuilder()
                        .build().list();
                for (int i = 0; i < list2.size(); i++) {
                    String name = list2.get(i).getName();
                    if (name.equals("zone")) {
                        msgBeanDao.deleteByKey(list2.get(i).getId());//通过 Id 来删除数据
                        /*msgBeanDao.delete(list2.get(i));//通过传入实体类的实例来删除数据*/
                    }
                }
                break;
            case R.id.gai:
                //改
                List<StudentMsgBean> list3 = msgBeanDao.queryBuilder()
                        /*.offset(1)//偏移量，相当于 SQL 语句中的 skip
                        .limit(3)//只获取结果集的前 3 个数据
                        .orderAsc(StudentMsgBeanDao.Properties.StudentNum)//通过 StudentNum 这个属性进行正序排序
                        .where(StudentMsgBeanDao.Properties.Name.eq("zone"))//数据筛选，只获取 Name = "zone" 的数据。*/
                        .build()
                        .list();
                for (int i = 0; i < list3.size(); i++) {
                    list3.get(i).setStudentNum("zone==========>");
                    msgBeanDao.update(list3.get(i));
                }
                break;
            case R.id.cha:
                //查
               /* StudentMsgBean load = msgBeanDao.load(1l);//通过id直接查询
                load.getName();*/
                list = msgBeanDao.queryBuilder()
                        .build()
                        .list();
                lv.setAdapter(new MyAdapter());
                if (list.size() > 0) {
                } else {
                    Toast.makeText(this, "没有数据了！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = View.inflate(MainActivity.this, R.layout.listview, null);
            TextView name = (TextView) v.findViewById(R.id.name);
            TextView studentNum = (TextView) v.findViewById(R.id.studentNum);
            name.setText(list.get(position).getName());
            studentNum.setText(list.get(position).getStudentNum());
            return v;
        }
    }
}
