package com.myfirst;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ashu.myfirst.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public String checkError(){
        /* this function checks whether the fields entered are valid or not.
           e.g. team name, name 1 and name 2 can't be empty
           and entry no. must be in proper format
        */
        EditText Team = (EditText) findViewById(R.id.getTeamName);   //access team name
        String t = Team.getText().toString().trim();                //getting value of team name in a string
        EditText Name1 = (EditText) findViewById(R.id.getName1);
        String t1 = Name1.getText().toString().trim();
        EditText Name2 = (EditText) findViewById(R.id.getName2);
        String t2 = Name2.getText().toString().trim();
        EditText Name3 = (EditText) findViewById(R.id.getName3);
        String t3 = Name3.getText().toString().trim();

        EditText Entry1 = (EditText) findViewById(R.id.getEntry1);
        String s1 = Entry1.getText().toString().trim();
        EditText Entry2 = (EditText) findViewById(R.id.getEntry2);
        String s2 = Entry2.getText().toString().trim();
        EditText Entry3 = (EditText) findViewById(R.id.getEntry3);
        String s3 = Entry3.getText().toString().trim();

        if(t.equals("")) {                                           //team name can't be empty
            Team.setError("please enter username");                 // showing error field
            return "Team name is a compulsory field";
        }else
            Team.setError(null);                                    // clearing error message
        if(t1.equals("")) {
            Name1.setError("please enter Name 1");
            return "Name 1 is a compulsory field";
        }else
            Name1.setError(null);
        if(checkEntry(s1)) {                                 //checkEntry validates the entry no.
            Entry1.setError("please enter correct Entry No");
            return "Entry No. 1 is incorrect";
        }else
            Entry1.setError(null);
        if(t2.equals("")) {
            Name2.setError("please enter Name 2");
            return "Name 2 is a compulsory field";
        }else
            Name2.setError(null);
        if(checkEntry(s2)) {
            Entry2.setError("please enter correct Entry No");
            return "Entry No. 2 is incorrect";
        }else
            Entry2.setError(null);
        if (!t3.equals("") && checkEntry(s3)) {                      //  if name 3 is entered then
            Entry3.setError("please enter correct Entry No");
            return "Entry No. 3 is incorrect";            // entry 3 must be entered
        }else
            Entry3.setError(null);
        return "No Error";
    }
    public boolean checkEntry(String t){
        if (t.length() != 11)                       //Entry no. must be 11 char long
            return  true;
        int temp = Integer.parseInt(t.substring(0, 4));     //first 4 char represent year
        if (!(temp > 2007 && temp <= 2017)) {               // entry year in a range
            //System.out.print(temp);
            return  true;
        }
        String tempS =  t.substring(4,6).toUpperCase();         // next 2 char represent department
        if (!(tempS.compareTo("AA")>=0 && tempS.compareTo("ZZ")<=0)) {
            //System.out.print(tempS);
            return true;
        }
        int temp2 = Integer.parseInt(t.substring(7));        // last 4 char should be integers
        if (!(temp2 > 0 && temp2 < 2000)) {
            //System.out.print(temp2);
            return  true;
        }
        return false;
    }


    //check_Error check = new check_Error();                          // creating an instance of class check_Error
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    Button b = (Button) findViewById(R.id.submitButton);         // declaring button b to access submit button
    b.setOnClickListener(new View.OnClickListener() {            // OnClickListener to monitor pressing button b
        @Override
        public void onClick(View view) {
                String temp = checkError();               // calling fn checkError to validate data
                if (!temp.equals("No Error")) {                 //in case of error tossing the error
                    Toast.makeText(getBaseContext(), temp, Toast.LENGTH_LONG).show();
                    onResume();
                }else {
                    MainActivity.this.Post();                   // no error -> posting data to server
                }
        }
    });
    }

    public void Post()  {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");                   // setting a message to show on dialog box
        pDialog.show();
        String URL = "http://agni.iitd.ernet.in/cop290/assign0/register/";

        RequestQueue queue = Volley.newRequestQueue(this);          // creating a new request queue to send data to server
        StringRequest strObjRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.hide();
                        Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String s = error.toString();
                        pDialog.hide();
                        Toast.makeText(getBaseContext(), "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {                         // creating a map of the data to be sent
                Map<String, String> params = new HashMap<String, String>();
                //extracting string from data  and putting it as the value of key terms teamname, name, entry
                params.put("teamname", ((EditText) findViewById(R.id.getTeamName)).getText().toString().trim());
                params.put("entry1", ((EditText) findViewById(R.id.getEntry1)).getText().toString().trim());
                params.put("name1", ((EditText) findViewById(R.id.getName1)).getText().toString().trim());
                params.put("entry2", ((EditText) findViewById(R.id.getEntry2)).getText().toString().trim());
                params.put("name2", ((EditText) findViewById(R.id.getName2)).getText().toString().trim());
                params.put("entry3", ((EditText) findViewById(R.id.getEntry3)).getText().toString().trim());
                params.put("name3", ((EditText) findViewById(R.id.getName3)).getText().toString().trim());
                return params;
            }

            @Override
            public RetryPolicy getRetryPolicy() {
                setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                return super.getRetryPolicy();
            }
        };
        queue.add(strObjRequest);                            // adding the object to the queue
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
