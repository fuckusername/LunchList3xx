package com.example.lunchlist;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.app.TabActivity;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends TabActivity {
    List<Restaurant> model=new ArrayList<Restaurant>();
    RestaurantAdapter adapter = null;

    EditText name =null;
    EditText address= null;
    EditText notes = null;
    RadioGroup types = null;
    Restaurant current = null;
    int progress =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_main);

        name = (EditText)findViewById(R.id.name);
        address = (EditText)findViewById(R.id.addr);
        types = (RadioGroup)findViewById(R.id.types);
        notes = (EditText)findViewById(R.id.notes);

        //Tab1
        TabHost.TabSpec spec=getTabHost().newTabSpec("tag1");
        spec.setContent(R.id.restaurants);
        spec.setIndicator("List", getResources().getDrawable(R.drawable.list));
        getTabHost().addTab(spec);

        //Tab2
        spec=getTabHost().newTabSpec("tag2");
        spec.setContent(R.id.details);
        spec.setIndicator("Details", getResources().getDrawable(R.drawable.restaurant));
        getTabHost().addTab(spec);

        getTabHost().setCurrentTab(0);

        //Button SAVE
        Button save=(Button)findViewById(R.id.save);
        save.setOnClickListener(onSave);

        //RadioGroup
        //types.setOnCheckedChangeListener(new TypesListener());
        //types.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId){
//                    case R.id.take_out:
//
//                }
//            }
//        });

        //List view Adapter
        ListView list = (ListView)findViewById(R.id.restaurants);

        adapter = new RestaurantAdapter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(onListClick);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        new MenuInflater(this).inflate(R.menu.options, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.toast) {
            String message="No restaurant selected";
            if (current!=null) {
                message=current.getNotes();
            }
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            return(true);
        }
        if(item.getItemId()==R.id.run){
            setProgressBarVisibility(true);
            progress=0;
            new Thread(longTask).start();
            return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

    private void doSomeLongWork(final int incr){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress+=incr;
                setProgress(progress);
            }
        });
        SystemClock.sleep(250);
    }

    private Runnable longTask = new Runnable() {
        @Override
        public void run() {
            for(int i=0;i<20; i++){
                doSomeLongWork(500);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setProgressBarVisibility(false);
                }
            });
        }
    };

    private View.OnClickListener onSave = new View.OnClickListener() {
        public void onClick(View v) {
            current=new Restaurant();
            current.setName(name.getText().toString());
            current.setAddress(address.getText().toString());
            current.setNotes(notes.getText().toString());
            switch (types.getCheckedRadioButtonId()) {
                case R.id.sit_down:
                    current.setType("sit_down");
                    break;
                case R.id.take_out:
                    current.setType("take_out");
                    break;
                case R.id.delivery:
                    current.setType("delivery");
                    break;
            }
            adapter.add(current);
        }
    };

    class TypesListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.take_out:
                    Log.d("myLog", "onCheckedChanged: Take out");
                    Toast.makeText(MainActivity.this,"Take out", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.sit_down:
                    Log.d("myLog","onCheckChanged: Sit down");
                    Toast.makeText(MainActivity.this,"Sit down", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.delivery:
                    Log.d("mtLog", "onCheckChange: Delivery");
                    Toast.makeText(MainActivity.this,"Delivery", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Restaurant r = model.get(position);
            name.setText(r.getName());
            address.setText(r.getAddress());
            if (r.getType().equals("sit_down")) {
                types.check(R.id.sit_down);
            }
            else if (r.getType().equals("take_out")) {
                types.check(R.id.take_out);
            }
            else {
                types.check(R.id.delivery);
            }
            getTabHost().setCurrentTab(1);
        }
    };

    class RestaurantAdapter extends ArrayAdapter<Restaurant> {
        RestaurantAdapter() {
            super(MainActivity.this, R.layout.rowlistview, model);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View row = convertView;
            RestaurantHolder holder = null;
            if(row == null){
                LayoutInflater inflater = getLayoutInflater();

                row = inflater.inflate(R.layout.rowlistview, parent, false);
                holder = new RestaurantHolder(row);
                row.setTag(holder);
            }
            else
                holder = (RestaurantHolder)row.getTag();

            holder.populateFrom(model.get(position));
            return row;
        }
    }

    static class RestaurantHolder{
        private TextView name = null;
        private TextView address = null;
        private ImageView icon = null;

        RestaurantHolder(View row){
            name = (TextView)row.findViewById(R.id.title);
            address = (TextView)row.findViewById(R.id.address);
            icon = (ImageView)row.findViewById(R.id.icon);
        }

        void populateFrom(Restaurant r){
            name.setText(r.getName());
            address.setText(r.getAddress());
            if(r.getType().equals("sit_down")){
                icon.setImageResource(R.drawable.pen_8);
            }
            else if(r.getType().equals("take_out")){
                icon.setImageResource(R.drawable.pen_8);
            }
            else
                icon.setImageResource(R.drawable.pen_8);
        }
    }

}
