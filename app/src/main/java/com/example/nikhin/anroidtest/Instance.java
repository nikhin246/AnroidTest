package com.example.nikhin.anroidtest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class Instance extends AppCompatActivity {

    Context ct;
    int pos = 0;
    ListView lview;
    InstanceAdapter adapter;
    Button b1;
    ArrayList<String> eTemp = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ct = this;
        pos = Integer.parseInt(getIntent().getStringExtra("pos"));
        setContentView(R.layout.activity_instance);
        b1 = (Button) findViewById(R.id.b1);
        lview = (ListView) findViewById(R.id.list);
        adapter = new InstanceAdapter(ct);
        lview.setAdapter(adapter);
        for (int i = 0; i < OnRemote.eInstances.size(); i++) {
            if (OnRemote.eInstances.get(i).get("key").equals("value" + pos)) {
                for (int j = 0; j < 1000; j++) {
                    try {
                        Log.d("dsfcwdsvewd", OnRemote.eInstances.get(i).get("value" + j) + "z");
                        String s1 = OnRemote.eInstances.get(i).get("value" + j);
                        if (s1.length() > 0) {
                            eTemp.add(j, OnRemote.eInstances.get(i).get("value" + j));
                            Log.d("sdfweavsg", OnRemote.eInstances.get(i).get("value" + j));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }

            }
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnRemote.showAlert(ct, "Hi !", "Integration not required.");

            }
        });
        adapter.notifyDataSetChanged();
    }

    class InstanceAdapter extends BaseAdapter {
        LayoutInflater li;
        Context c;
        EditText et1;

        InstanceAdapter(Context ct) {
            li = LayoutInflater.from(ct);
            c = ct;
        }

        @Override
        public int getCount() {
            return eTemp.size();
        }

        @Override
        public Object getItem(int position) {
            return eTemp.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = li.inflate(R.layout.instance_layout, null);
            et1 = (EditText) convertView.findViewById(R.id.et1);
            et1.setHint("Enter " + eTemp.get(position));
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }
}
