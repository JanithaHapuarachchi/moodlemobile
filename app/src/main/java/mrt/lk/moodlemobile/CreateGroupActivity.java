package mrt.lk.moodlemobile;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrt.lk.moodlemobile.adapters.GroupAddStudentsAdapter;
import mrt.lk.moodlemobile.data.GroupProjectItem;
import mrt.lk.moodlemobile.data.LoggedUser;
import mrt.lk.moodlemobile.data.ParticipantItem;
import mrt.lk.moodlemobile.data.ResObject;
import mrt.lk.moodlemobile.utils.Constants;
import mrt.lk.moodlemobile.utils.ProgressBarController;
import mrt.lk.moodlemobile.utils.Utility;
import mrt.lk.moodlemobile.utils.WSCalls;

public class CreateGroupActivity extends AppCompatActivity {

    WSCalls wscalls;
    ProgressBarController prgController;
    EditText group_name,search_name;
    Button btn_done;
    ListView list_students;
    ArrayList<ParticipantItem> clone_original_participants,clone_participants,participants,original_participants;
    static boolean IS_CREATE_GROUP;
    static String GIVEN_GROUP_NAME;
    static String GIVEN_GROUP_ID;
    GroupAddStudentsAdapter addStudentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        prgController = new ProgressBarController(this);
        wscalls = new WSCalls(getApplicationContext());
        group_name = (EditText)findViewById(R.id.group_name);
        search_name = (EditText)findViewById(R.id.search_name);
        btn_done =  (Button) findViewById(R.id.btn_done);
        list_students = (ListView)findViewById(R.id.list_students);
        participants = new ArrayList<ParticipantItem>();
        //if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                IS_CREATE_GROUP= false;
                GIVEN_GROUP_NAME = "";
                GIVEN_GROUP_ID = "";
            } else {
                IS_CREATE_GROUP= extras.getBoolean("IS_CREATE_GROUP");
                GIVEN_GROUP_NAME = extras.getString("GIVEN_GROUP_NAME");
                GIVEN_GROUP_ID = extras.getString("GIVEN_GROUP_ID");
            }
//        } else {
//            IS_CREATE_GROUP= (Boolean) savedInstanceState.getSerializable("IS_CREATE_GROUP");
//            GIVEN_GROUP_NAME = (String) savedInstanceState.getSerializable("GIVEN_GROUP_NAME");
//            GIVEN_GROUP_ID = (String) savedInstanceState.getSerializable("GIVEN_GROUP_ID");
//        }
        group_name.setText(GIVEN_GROUP_NAME);

        if(IS_CREATE_GROUP){
            getSupportActionBar().setTitle("Create Group");
            group_name.setEnabled(true);
        }
        else{
            getSupportActionBar().setTitle("Add Members");
            group_name.setEnabled(true);
        }

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!group_name.getText().toString().equals("")){
                    Utility.showMessage("Please add a Name",CreateGroupActivity.this);
                }
                else {
                    rearrange_original_participants();
                    //new AddGroupStudets().execute(group_name.getText().toString());
                }

            }
        });

        list_students.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParticipantItem item = participants.get(position);
                Log.e("Moodlemobile",""+position+":"+item.isSelected);
                LinearLayout selectedView = (LinearLayout)list_students.getChildAt(position);
                    if (item.isSelected) {
                        item.isSelected = false;
                        if (selectedView != null) {
                            selectedView.setBackgroundResource(R.color.white);
                        }
                        else{
                            Log.e("Moodlemobile","NULL");
                        }
                    } else {
                        item.isSelected = true;
                        if (selectedView != null) {
                            selectedView.setBackgroundResource(R.color.app_text_color_warning);
                        }
                        else{
                            Log.e("Moodlemobile","NULL");
                        }
                    }
                participants.set(position,item);
            }
        });
        setSampleData();
       // new LoadUnAllocatedAtudents().execute(LoggedUser.course_id);
        addStudentsAdapter = new GroupAddStudentsAdapter(getApplicationContext(),participants);
        list_students.setAdapter(addStudentsAdapter);

        search_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                populate_search_results(s.toString());
            }
        });
    }

    private boolean isSelectedParticipant(String id){
        boolean isSelected = false;
        ParticipantItem p;
        for(int i=0;i<clone_participants.size();i++){
            p = clone_participants.get(i);
            if(id.equals(p.id) && p.isSelected ){
                isSelected = true;
                clone_participants.remove(i);
                break;
            }
        }
        return isSelected;
    }

    private void rearrange_original_participants(){
        clone_original_participants = (ArrayList<ParticipantItem>) original_participants.clone();
        clone_participants =  (ArrayList<ParticipantItem>) participants.clone();
        for(int j=0;j<clone_original_participants.size();j++){
            Log.e("Moodlemobile rm",""+j);
            if(isSelectedParticipant(clone_original_participants.get(j).id)){
                clone_original_participants.remove(j);
                j--;
            }
        }
        original_participants = clone_original_participants;
        participants =original_participants;
        addStudentsAdapter = new GroupAddStudentsAdapter(getApplicationContext(),participants);
        list_students.setAdapter(addStudentsAdapter);
    }

    private void populate_search_results(String name){
        if(name.equals("")){
            participants =original_participants;
            addStudentsAdapter = new GroupAddStudentsAdapter(getApplicationContext(),participants);
        }
        else{
            ParticipantItem itm;
            participants = new ArrayList<ParticipantItem>();
            for(int i=0;i<original_participants.size();i++ ){
                itm = original_participants.get(i);
                if(itm.name.toLowerCase().contains(name.toLowerCase())){
                    participants.add(itm);
                }
            }
            addStudentsAdapter = new GroupAddStudentsAdapter(getApplicationContext(),participants);
        }
        list_students.setAdapter(addStudentsAdapter);
    }

    private void setSampleData(){
        participants = new ArrayList<ParticipantItem>();
        ParticipantItem item;
        for(int i=0;i<5;i++){
            item = new ParticipantItem();
            item.id= ""+i;
            item.name = "Name : "+(i+1);
            //item.isSelected = true;
            participants.add(item);
        }
        original_participants = participants;
    }

    public void populate_unallocated_students(String msg){
        try {
            JSONObject j  = new JSONObject(msg);
            if(j.getString("msg").equals("Success")) {

                //JSONArray j_array = new JSONArray(msg);
                JSONArray j_array = j.getJSONArray("data");
                participants = new ArrayList<ParticipantItem>();
                ParticipantItem p;
                JSONObject jo;
                for (int i = 0; i < j_array.length(); i++) {
                    jo = j_array.getJSONObject(i);
                    p = new ParticipantItem();
                    p.id = jo.getString("participantid");
                    p.name = jo.getString("studentname");
                    participants.add(p);
                }
                original_participants = participants;
                addStudentsAdapter = new GroupAddStudentsAdapter(getApplicationContext(),participants);
                list_students.setAdapter(addStudentsAdapter);
            }
            else{
                Utility.showMessage(j.getString("msg"),getApplicationContext());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populate_addgroup_data(String msg){
        if(IS_CREATE_GROUP){
            finish();
        }
        rearrange_original_participants();
    }

    class AddGroupStudets extends AsyncTask<String,Void,ResObject>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Sending Data");
        }

        @Override
        protected void onPostExecute(ResObject response) {
            super.onPostExecute(response);
            prgController.hideProgressBar();
            if(response.validity.equals(Constants.VALIDITY_SUCCESS)){
                populate_addgroup_data(response.msg);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {
            if(IS_CREATE_GROUP){
               return wscalls.add_group_students(null,LoggedUser.course_id,params[0],participants);
            }
            else{
                return wscalls.add_group_students(GIVEN_GROUP_ID,LoggedUser.course_id,params[0],participants);
            }


        }
    }

    class LoadUnAllocatedAtudents extends AsyncTask <String,Void,ResObject>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgController.showProgressBar("Loading Students");
        }

        @Override
        protected void onPostExecute(ResObject response) {
            super.onPostExecute(response);
            prgController.hideProgressBar();

            if(response.validity.equals(Constants.VALIDITY_SUCCESS)){
                populate_unallocated_students(response.msg);
            }
            else{
                Utility.showMessage(response.msg,getApplicationContext());
            }
        }

        @Override
        protected ResObject doInBackground(String... params) {
            return wscalls.get_unallocated_students(params[0]);
        }
    }

}
