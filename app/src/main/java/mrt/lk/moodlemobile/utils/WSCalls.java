package mrt.lk.moodlemobile.utils;

import android.content.Context;
import android.util.Log;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import mrt.lk.moodlemobile.data.ParticipantItem;
import mrt.lk.moodlemobile.data.ResObject;

/**
 * Created by janithamh on 8/12/18.
 */

public class WSCalls {

    Context context;
    DataManager dtManager;

    public WSCalls(Context context){
        this.context =context;

    }


    public ResObject checkLogin(String username,String password){
        ResObject res_object = new ResObject();
       String response;
        String request  =  "username="+username+"&password="+password+"&service=moodle_mobile_app";
        try {

            response = RequestHandler.getLogin(request,context);
            res_object.validity = Constants.VALIDITY_SUCCESS;
             res_object.msg = response;
        } catch (Exception e) {
            res_object.validity = Constants.VALIDITY_FAILED;
            res_object.msg = e.getMessage();
        }

        return res_object;
    }

    public ResObject confirm_group_students(String group_id){
        ResObject res_object = new ResObject();
        String response;
        String request  =  "group_id="+group_id;
        try {

            response = RequestHandler.sendPost(request,Constants.confirm_group_students,context);
            res_object.validity = Constants.VALIDITY_SUCCESS;
            res_object.msg = response;
        } catch (Exception e) {
            res_object.validity = Constants.VALIDITY_FAILED;
            res_object.msg = e.getMessage();
        }

        return res_object;
    }


    public ResObject remove_student_from_group(String group_id,String student_id){
        ResObject res_object = new ResObject();
        String response;
        String request  =  "group_id="+group_id+"&student_id="+student_id;
        try {

            response = RequestHandler.sendPost(request,Constants.remove_student_from_group,context);
            res_object.validity = Constants.VALIDITY_SUCCESS;
            res_object.msg = response;
        } catch (Exception e) {
            res_object.validity = Constants.VALIDITY_FAILED;
            res_object.msg = e.getMessage();
        }

        return res_object;
    }

    public ResObject course_group_students(String group_id){
        ResObject res_object = new ResObject();
        String response;
        String request  =  "group_id="+group_id;
        try {

            response = RequestHandler.sendPost(request,Constants.course_group_students,context);
            res_object.validity = Constants.VALIDITY_SUCCESS;
            res_object.msg = response;
        } catch (Exception e) {
            res_object.validity = Constants.VALIDITY_FAILED;
            res_object.msg = e.getMessage();
        }

        return res_object;
    }

    public ResObject add_group_project(String group_id,String project_name){
        ResObject res_object = new ResObject();
        String response;
        String request  =  "group_id="+group_id+"&project_name="+project_name;
        try {

            response = RequestHandler.sendPost(request,Constants.add_group_project,context);
            res_object.validity = Constants.VALIDITY_SUCCESS;
            res_object.msg = response;
        } catch (Exception e) {
            res_object.validity = Constants.VALIDITY_FAILED;
            res_object.msg = e.getMessage();
        }

        return res_object;
    }

    public ResObject course_group_projects(String group_id){
        ResObject res_object = new ResObject();
        String response;
        String request  =  "group_id="+group_id;
        try {

            response = RequestHandler.sendPost(request,Constants.course_group_projects,context);
            res_object.validity = Constants.VALIDITY_SUCCESS;
            res_object.msg = response;
        } catch (Exception e) {
            res_object.validity = Constants.VALIDITY_FAILED;
            res_object.msg = e.getMessage();
        }

        return res_object;
    }

    public ResObject add_group_students(String group_id, String course_id, String group_name, ArrayList<ParticipantItem> students){
        ResObject res_object = new ResObject();
        String response;
        String request  =  "course_id="+course_id+"&group_name="+group_name;
        if(group_id != null){
            request += "&group_id="+group_id;
        }
        JSONArray ja = new JSONArray();
        JSONObject jo;
        for(int i=0;i<students.size();i++){
            jo = new JSONObject();
            try {
                if(students.get(i).isSelected) {
                    jo.put("participantid", students.get(i).id);
                    ja.put(jo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        request += "&students="+ja.toString();
        try {
            response = RequestHandler.sendPost(request,Constants.add_group_students,context);
            res_object.validity = Constants.VALIDITY_SUCCESS;
            res_object.msg = response;
        } catch (Exception e) {
            res_object.validity = Constants.VALIDITY_FAILED;
            res_object.msg = e.getMessage();
        }

        return res_object;
    }


    public ResObject get_unallocated_students(String course_id){
        ResObject res_object = new ResObject();
        String response;
        String request  =  "course_id="+course_id;
        try {

            response = RequestHandler.sendPost(request,Constants.unallocated_group_students,context);
            res_object.validity = Constants.VALIDITY_SUCCESS;
            res_object.msg = response;
        } catch (Exception e) {
            res_object.validity = Constants.VALIDITY_FAILED;
            res_object.msg = e.getMessage();
        }

        return res_object;
    }

//
//    public ResObject autherise_user(String username, String password) {
//
//        ResObject res_object = new ResObject();
//        String response;
//       String request  =  Constants.AUTHENTICATION_URL+"?username="+username+"&password="+password;
//      //  String request  =  Constants.AUTHENTICATION_URL;
//        JSONObject req = new JSONObject();
//        try {
//            req.put("username",username);
//            req.put("password",password);
//            if(Utility.isConnected(context)) {
//                Utility.clearAuthKey(context);
//                response = RequestHandler.sendPost(new JSONObject(), request, context);
//                res_object.validity = Constants.VALIDITY_SUCCESS;
//                res_object.msg = response;
//            }
//            else{
//                res_object.validity = Constants.VALIDITY_FAILED;
//                res_object.msg = Constants.NETWORK_NOT_FOUND;
//            }
//
//        } catch (Exception e) {
//            Log.e("FLI E AUTH",e.getMessage());
//            res_object.validity = Constants.VALIDITY_FAILED;
//            res_object.msg = "LoginActivity Failed";
//          // res_object.msg = e.getMessage();
//        }
//        return res_object;
//    }
//
//    public RecievedLoan get_loan_associations(RecievedLoan loan) throws Exception{
//
//        Calendar calendar = Calendar.getInstance();
//
//        int currentYear =  calendar.get(Calendar.YEAR);
//        int currentMonth = calendar.get(Calendar.MONTH)+1;
//        int currentDate = calendar.get(Calendar.DATE);
//
//        int periodCheckNo =1;
//        double rental = 0.00;
//        String request,response ;
//        request = Constants.LOANS_URL + "/" + loan.loan_id+Constants.ALL_ASSOCIATIONS ;
//        response = RequestHandler.sendGet(request, context);
//        JSONObject resJSON,scheduleJSON;
//        JSONArray periodsJSON,transactionJSON;
//        resJSON = new JSONObject(response);
//        scheduleJSON = resJSON.getJSONObject("repaymentSchedule");
//        periodsJSON = scheduleJSON.getJSONArray("periods");
//        transactionJSON = resJSON.getJSONArray("transactions");
//        rental= periodsJSON.getJSONObject(periodCheckNo).getDouble("principalDue")+periodsJSON.getJSONObject(periodCheckNo).getDouble("interestDue");
//        loan.rental = String.valueOf(rental);
//
//        for(int j = 0 ; j < periodsJSON.length() ; j++) {
//            JSONObject period = periodsJSON.getJSONObject(j);
//
//            JSONArray periodDueDate = period.getJSONArray("dueDate");
//
//            if (currentYear == periodDueDate.getInt(0) && currentMonth == periodDueDate.getInt(1) && currentDate == periodDueDate.getInt(2)) {
//                loan.arrearsFlag = true;
//                break;
//            } else {
//                loan.arrearsFlag = false;
//            }
//        }
//
//        if(transactionJSON ==null || transactionJSON.length()==0){
//            loan.def =  String.valueOf(rental);
//        }
//        else{
//            Double repayment = getRepaymentFromTransaction(transactionJSON);
//            if(repayment != null){
//                loan.def = String.valueOf(repayment);
//            }
//            else{
//                loan.def =  String.valueOf(rental);
//            }
//            //loan.def =  String.valueOf(transactionJSON.getJSONObject(transactionJSON.length()-1).getDouble("amount"));
//        }
//        return loan;
//    }
//
//    public Double getRepaymentFromTransaction(JSONArray transactionJSON) throws JSONException {
//        Double repayment = null;
//        JSONObject resJSON,typeJSON;
//        for(int i = transactionJSON.length()-1;i>=0;i--){
//            resJSON = transactionJSON.getJSONObject(i);
//            if(!resJSON.getBoolean("manuallyReversed")){
//                typeJSON = resJSON.getJSONObject("type");
//                if(typeJSON.getInt("id")==2){
//                    repayment = resJSON.getDouble("amount");
//                    break;
//                }
//            }
//
//        }
//        return  repayment;
//    }
//
//    public ResObject sync_loans(){
//        ResObject res_object = new ResObject();
//        ArrayList <JSONObject> loans = new ArrayList<JSONObject>();
//        ArrayList <RecievedLoan> res_loans_set;
//        ArrayList <RecievedLoan> res_loans = new ArrayList<RecievedLoan>();
//        RecievedLoan resLoan;
//        long loopcount = 1;
//        String request,response ;
//        JSONObject resJSON,arrayObject;
//        JSONArray resArray;
//        long limit =800;
//        long offset = 0;
//        long totalresults;
//        String urlencodestrring;
//        long i;
//        try {
//            if(Utility.isConnected(context)) {
//                urlencodestrring = URLEncoder.encode(Constants.ACTIVE_USER_URL + Utility.getStaffID(context) + Constants.ACTIVE_LOANS_URL, "UTF-8");
//                for ( i = 0; i < loopcount; i++) {
//                    Log.e("FLI loopcount i", "" + loopcount + " : " + i);
//                    request = Constants.LOANS_URL + "?" + "offset=" + offset + "&limit=" + limit + "&" + Constants.SQL_SEARCH + urlencodestrring;
//                    try {
//                        response = RequestHandler.sendGet(request, context);
//                        resJSON = new JSONObject(response);
//                        // resLoan = new RecievedLoan();
//                        resArray = resJSON.getJSONArray("pageItems");
//                        res_loans_set = getLoansFromArray(resArray);
//                        res_loans.addAll(res_loans_set);
//                        //loans.addAll(Utility.getArrayListformJSONARRAY( resJSON.getJSONArray("pageItems")));
//                        if (i == 0) {
//                            totalresults = resJSON.getLong("totalFilteredRecords");
//                            Log.e("FLI TOT COUNT", "" + totalresults);
//                            if (totalresults > limit) {
//                                loopcount = totalresults / limit;
//                                Log.e("FLI loopcount COUNT", "" + loopcount);
//                                Log.e("FLI loopcount COUNT 2", "" + totalresults % limit);
//                                if ((totalresults % limit) != 0) {
//                                    loopcount++;
//                                }
//                                //loopcount--; // for default loop
//                            }
//                        }
//                    } catch (Exception e) {
//                        Utility.clearCurrentUserLoginDate(context);
//                        Log.e("FLI E LOANS", e.getMessage());
//                        res_object.validity = Constants.VALIDITY_FAILED;
//                        res_object.msg = "Sync Loans Failed";
//                        return res_object;
//                        // res_object.msg = e.getMessage();
//                    }
//
//                    offset = (i + 1) * limit;
//                }
//                res_object.validity = Constants.VALIDITY_SUCCESS;
//
//
//            }
//            else{
//                Utility.clearCurrentUserLoginDate(context);
//                res_object = new ResObject();
//                res_object.validity = Constants.VALIDITY_FAILED;
//                res_object.msg = Constants.NETWORK_NOT_FOUND;
//                return res_object;
//            }
//        }
//        catch (Exception e) {
//            Utility.clearCurrentUserLoginDate(context);
//        Log.e("FLI E2 LOANS", e.getMessage());
//        res_object.validity = Constants.VALIDITY_FAILED;
//        res_object.msg = e.getMessage();
//            return res_object;
//        // res_object.msg = e.getMessage();
//    }
//
//        new DBOperations(context).saveRecievedLoans(res_loans);
////        Log.e("FLI LOANS",loans.toString());
//        return res_object;
//    }
//
//    public ArrayList<RecievedLoan> getLoansFromArray(JSONArray recieved) throws Exception {
//        JSONObject res,group,summary,loanType;
//        ArrayList<RecievedLoan> resloans = new ArrayList<RecievedLoan>();
//        RecievedLoan loan;
//
//        for(int i=0; i< recieved.length();i++){
//           // try {
//            res= recieved.getJSONObject(i);
//            loan = new RecievedLoan();
//            Log.e("FLI Res ",res.toString());
//            loan.loan_id = String.valueOf(res.getLong("id"));
//            loan.loan_name = res.getString("loanProductName");
//            loan.loan_accountno =  res.getString("accountNo");
//
//            if(Utility.isJSONKeyAvailable(res,"externalId")) {
//                loan.loan_externalid = res.getString("externalId");
//            }else{
//                loan.loan_externalid ="";
//            }
//
//            if(Utility.isJSONKeyAvailable(res,"group")) {
//                group = res.getJSONObject("group");
//            }else{
//                group = new JSONObject();
//            }
//
//            summary = res.getJSONObject("summary");
//            loanType =  res.getJSONObject("loanType");
//            loan.type = loanType.getString("value");
//                if(loan.type.equals(Constants.LOAN__TYPE_GROUP)) {
//                    loan.group_id = String.valueOf(group.getLong("id"));
//                    loan.group_name = group.getString("name");
//                    loan.center_id = String.valueOf(group.getLong("centerId"));
//                    loan.center_name = group.getString("centerName");
//                }
//                else{
//                    loan.group_id = "";
//                    loan.group_name = "";
//                    loan.center_id = "";
//                    loan.center_name ="";
//                }
//
//            loan.client_id = String.valueOf(res.getLong("clientId"));
//            loan.client_name = res.getString("clientName");
//            loan.total_balance = String.valueOf(summary.getDouble("principalDisbursed"));
//            loan.outstanding_balance = String.valueOf(summary.getDouble("totalOutstanding"));
//            loan.arrears =  String.valueOf(summary.getDouble("totalOverdue"));
//
//            loan = get_loan_associations(loan);
//
//            if(loan.arrearsFlag){
//                Double tmpArrears = Double.valueOf(loan.arrears);
//                tmpArrears += Double.valueOf(loan.rental);
//                loan.arrears =  String.valueOf(tmpArrears);
//            }
//
//            resloans.add(loan);
//
//
//         //   } catch (Exception e) {
//         //       Log.e("FLI LOAN RES",e.getMessage());
//         //       e.printStackTrace();
//         //   }
//        }
//
//        return  resloans;
//    }
//
//    public void sync_MarkedAttendance(){
//        ResObject res_object;
//        String response,request;
//        JSONObject jRequest,jResponse;
//        JSONArray jClients;
//        ArrayList<MarkedAttendace> markedAttendaces = dtManager.getMarkedAttendance();
//        MarkedAttendace attendace;
//        ClientItem clientItem;
//        for(int i=0; i<markedAttendaces.size();i++){
//            attendace =markedAttendaces.get(i);
//             res_object = new ResObject();
//             request  =  Constants.GROUP_URL+attendace.group_id+"/"+Constants.ATTENDANCE_URL+"?calenderId="+attendace.center_id;
//            try {
//                jRequest = new JSONObject();
//                jClients = new JSONArray();
//                Log.e("FLI SYNC GROUP",attendace.group_id);
//                jRequest.put("dateFormat",Constants.DATE_FORMAT);
//                jRequest.put("meetingDate",attendace.meeting_date);
//                jRequest.put("calendarId",attendace.center_id);
//                jRequest.put("locale","en");
//
//                for(int j=0; j< attendace.clients.size();j++){
//                    clientItem = attendace.clients.get(j);
//                    jClients.put(new JSONObject().put("clientId",clientItem.id).put("attendanceType",clientItem.attendancetype));
//                }
//                jRequest.put("clientsAttendance",jClients);
//                if(Utility.isConnected(context)) {
//                    response = RequestHandler.sendServicePost(jRequest, request, context);
//                    Log.e("FLI SYNC RES",response);
//                    jResponse = new JSONObject(response);
//                    if(Utility.isJSONKeyAvailable(jResponse,"resourceId")){
//                        dtManager.deleteAttendanceForGroupId(attendace.group_id);
//                    }
//                   // res_object.validity = Constants.VALIDITY_SUCCESS;
//                   // res_object.msg = response;
//                }
//                else{
//                    //res_object.validity = Constants.VALIDITY_FAILED;
//                   // res_object.msg = Constants.NETWORK_NOT_FOUND;
//                }
//
//            } catch (Exception e) {
//                Log.e("FLI E AUTH",e.getMessage());
//                //res_object.validity = Constants.VALIDITY_FAILED;
//                //res_object.msg = "LoginActivity Failed";
//                // res_object.msg = e.getMessage();
//            }
//
//        }
//    }
//
//    public void sync_PayedLoans(){
//
//        String response,request;
//        JSONObject jRequest,jResponse;
//        ArrayList<PayeeItem> payedLoans = dtManager.getPayedLoans();
//        PayeeItem loan;
//        for(int i=0; i<payedLoans.size();i++){
//            loan =payedLoans.get(i);
//
//            request  =  Constants.LOANS_URL+"/"+loan.loan_id+Constants.LOAN_TRANSACTION_URL;
//            try {
//                jRequest = new JSONObject();
//                Log.e("FLI SYNC GROUP",loan.loan_id);
//                jRequest.put("dateFormat",Constants.DATE_FORMAT);
//                jRequest.put("transactionDate",loan.transaction_date);
//                jRequest.put("transactionAmount",loan.amount);
//                jRequest.put("paymentTypeId",loan.payment_type_id);
//                jRequest.put("note",loan.note);
//                if(loan.payment_type_id.equals(Constants.PAYMENT_TYPE_ID_CHEQUE)){
//                    jRequest.put("transactionAmount",loan.bank_no);
//                    jRequest.put("paymentTypeId",loan.checque_no);
//                }
//                jRequest.put("locale","en");
//                if(Utility.isConnected(context)) {
//                    response = RequestHandler.sendServicePost(jRequest, request, context);
//                    Log.e("FLI SYNC RES",response);
//                    jResponse = new JSONObject(response);
//                    if(Utility.isJSONKeyAvailable(jResponse,"resourceId")){
//                        dtManager.updateSyncedPayment(loan.loan_id);
//                    }
//
//                }
//
//            } catch (Exception e) {
//                Log.e("FLI E AUTH",e.getMessage());
//                //res_object.validity = Constants.VALIDITY_FAILED;
//                //res_object.msg = "LoginActivity Failed";
//                // res_object.msg = e.getMessage();
//            }
//
//        }
//    }

}
