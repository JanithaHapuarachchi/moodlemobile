package mrt.lk.moodlemobile.utils;

import java.text.DecimalFormat;

/**
 * Created by janithah on 8/9/2018.
 */

public class Constants {

    public static final String[] ATTENDANCE_TYPES = {"Present","Absent","Leave","Late"};
    public static final String LAST_LOGGED_DATE = "LAST_LOGGED_DATE";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_PASSWORD = "USER_PASSWORD";
    public static final String FLI_SHARED_PREFS = "FLI_SHARED_PREFS";
    public static final String DATE_FORMAT ="dd-MM-yyyy";
    public static final String AUTHENTICATION_KEY ="AUTHENTICATION_KEY";
    public static final String USER_ID ="USER_ID";
    public static final String STAFF_ID ="STAFF_ID";
    public static final String SHOULD_SYNC_AGAIN = "SHOULD_SYNC_AGAIN";

    public static String SERVER_URL_DEFAULT = "https://13.251.49.181";
    public static String SERVER_PORT_DEFAULT = "443";
    public static String SERVER_TENANT_DEFAULT = "default";
    public static String SERVER_URL = "SERVER_URL";
    public static String SERVER_PORT = "SERVER_PORT";
    public static String SERVER_TENANT = "SERVER_TENANT";
    public static String MAIN_URL = "https://52.74.229.37:443/fineract-provider/api/v1/";
    public static String LOGIN_URL = "https://mpbl.projects.uom.lk/login/token.php?";

    public static String unallocated_group_students = "unallocated_group_students.php";
    public static String add_group_students = "add_group_students.php";
    public static String course_group_projects = "course_group_projects.php";
    public static String add_group_project = "add_group_project.php";
    public static String confirm_group_students = "confirm_group_students.php";
    public static String course_group_students = "course_group_students.php";
    public static String remove_student_from_group = "remove_student_from_group.php";



    public static final String AUTHENTICATION_URL = "authentication";
    public static final String LOANS_URL = "loans";
    public static final String ACTIVE_LOANS_URL = " and (l.loan_status_id=300 or l.loan_status_id=602)";
    public static final String ACTIVE_USER_URL = "l.loan_officer_id=";
    public static final String LOAN_TRANSACTION_URL = "/transactions?command=repayment";
    public static final String GROUP_URL = "groups/";
    public static final String TRANSACTIONS_URL = "transactions?command=recoverypayment";
    public static final String ATTENDANCE_URL = "meetings";
    public static final String SQL_SEARCH = "sqlSearch=";

    public static DecimalFormat decimal_places = new DecimalFormat(".##");

    public static final String ALL_ASSOCIATIONS = "?associations=all";
   // public static final String ACTIVE_LOANS_URL = "&sqlSearch=cbu.username  is null";

    public static final String FILTER_FOR_PAYMENT = "FILTER_FOR_PAYMENT";
    public static final String FILTER_FOR_ATTENDANCE = "FILTER_FOR_ATTENDANCE";
    public static final String SYCED_NOT = "n";
    public static final String SYCED_YES = "y";
    public static final String LOAN__TYPE_GROUP = "JLG";
    public static final String VALIDITY_SUCCESS = "Success";
    public static final String VALIDITY_FAILED = "Failed";
    public static final String VALIDITY_STRING = "validity";
    public static final String VALIDITY_MSG = "msg";

    public static final String PAYMENT_TYPE_ID_CASH = "1";
    public static final String PAYMENT_TYPE_ID_CHEQUE = "2";

    public static final String NETWORK_NOT_FOUND = "Network Not Found";

}
