package com.example.nikhin.anroidtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Context ct;
    JSONArray eArr[];
    ListView list;
    ClassAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ct = this;
        setContentView(R.layout.activity_class);
        list = (ListView) findViewById(R.id.list);
        adapter = new ClassAdapter(ct);
        list.setAdapter(adapter);
        if (OnRemote.iO())
            new getServer().execute(OnRemote.SERVER_CHOOSER);
        else
            OnRemote.showAlertAndGoBack(ct, "Sorry", "Internet not found. App will be closed.");
    }

    class getServer extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        public void onPreExecute() {
            pd = OnRemote.createProgressDialog(ct);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> values = new HashMap<>();
            String res = Server.postParams(params[0], values);
            return res;
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            if (result.equals("")) {
                Toast.makeText(ct, "Server down may be!! Offline mode activated.", Toast.LENGTH_LONG).show();
                new getData().execute(OnRemote.loadPreferences(ct, "SERVER_URL"));
            } else {
                try {
                    result = result.substring(result.indexOf("tempuri.org") + 14, result.indexOf("</string"));
                } catch (Exception e) {
                }
                try {
                    JSONObject resObj = new JSONObject(result);
                    OnRemote.savePreferences(ct, "SERVER_URL", resObj.getString("server"));
                    Toast.makeText(ct, "Server " + OnRemote.loadPreferences(ct, "SERVER_URL") + " chosen", Toast.LENGTH_LONG).show();
                    new getData().execute(OnRemote.loadPreferences(ct, "SERVER_URL"));
                } catch (JSONException jse) {
                    jse.printStackTrace();
                    Toast.makeText(ct, "Please re-try.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    class getData extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        public void onPreExecute() {
            pd = OnRemote.createProgressDialog(ct);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> values = new HashMap<>();
            String res = Server.postParams(OnRemote.loadPreferences(ct, "SERVER_URL") + OnRemote.DATA_URL, values);
            return res;
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            try {
                result = result.substring(result.indexOf("tempuri.org") + 14, result.indexOf("</string"));
            } catch (Exception e) {
            }
            try {
                JSONObject resObj = new JSONObject(result);
                OnRemote.eClasses.clear();
                JSONArray eClassJson = resObj.getJSONArray("clsArray");
                for (int i = 0; i < eClassJson.length(); i++) {
                    OnRemote.eClasses.add(i, eClassJson.getString(i));
                }

                OnRemote.eInstances.clear();
                JSONArray eInstanceJson = resObj.getJSONArray("insArray");
                JSONObject eWin = eInstanceJson.getJSONObject(0);
                eArr = new JSONArray[eWin.length()];
                for (int i = 0; i < eWin.length(); i++) {
                    HashMap<String, String> eInsMap = new HashMap<>();
                    eInsMap.put("key", "value" + i);
                    int j = 0;
                    for (j = 0; j < eWin.getJSONArray("value" + i).length(); j++) {
                        eInsMap.put("value" + j, eWin.getJSONArray("value" + i).getString(j));
                    }
                    eInsMap.put("count", j + "");
                    OnRemote.eInstances.add(eInsMap);
                }
                Toast.makeText(ct, "All Classes and instances, are loaded and saved.", Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();
            } catch (JSONException jse) {
                jse.printStackTrace();
                Toast.makeText(ct, "Please re-try.", Toast.LENGTH_LONG).show();
            }
        }
    }

    class ClassAdapter extends BaseAdapter {
        LayoutInflater li;
        Context c;
        Button b1;

        ClassAdapter(Context ct) {
            li = LayoutInflater.from(ct);
            c = ct;
        }

        @Override
        public int getCount() {
            return OnRemote.eClasses.size();
        }

        @Override
        public Object getItem(int position) {
            return OnRemote.eClasses.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = li.inflate(R.layout.class_layout, null);
            b1 = (Button) convertView.findViewById(R.id.b1);
            b1.setText(OnRemote.eClasses.get(position) + ".");
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, Instance.class);
                    i.putExtra("pos", String.valueOf(position));
                    startActivity(i);
                }
            });
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }
}
