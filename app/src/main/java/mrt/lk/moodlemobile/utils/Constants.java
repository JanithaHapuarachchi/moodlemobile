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
    public static final String DATE_FORMAT ="yyyy-MM-dd HH.mm.ss";
    public static final String AUTHENTICATION_KEY ="AUTHENTICATION_KEY";
    public static final String USER_ID ="USER_ID";
    public static final String STAFF_ID ="STAFF_ID";
    public static final String SHOULD_SYNC_AGAIN = "SHOULD_SYNC_AGAIN";
    public static final String PROJECT_LAST_COMMENT = "PROJECT_LAST_COMMENT";
    public static final String SUBPROJECT_LAST_COMMENT = "SUBPROJECT_LAST_COMMENT";

    public static String SERVER_URL_DEFAULT = "https://13.251.49.181";
    public static String SERVER_PORT_DEFAULT = "443";
    public static String SERVER_TENANT_DEFAULT = "default";
    public static String SERVER_URL = "SERVER_URL";
    public static String SERVER_PORT = "SERVER_PORT";
    public static String SERVER_TENANT = "SERVER_TENANT";
    public static String MAIN_URL = "https://mpbl.projects.uom.lk/mobileapp/";
    public static String LOGIN_URL = "https://mpbl.projects.uom.lk/login/token.php?";

    public static String unallocated_group_students = "unallocated_group_students.php";
    public static String add_group_students = "add_group_students.php";
    public static String course_group_projects = "course_group_projects.php";
    public static String add_group_project = "add_group_project.php";
    public static String confirm_group_students = "confirm_group_students.php";
    public static String course_group_students = "course_group_students.php";
    public static String remove_student_from_group = "remove_student_from_group.php";
    public static String participant_evaluation_results = "participant_evaluation_results.php";
    public static String add_group_project_evaluation_group = "add_group_project_evaluation_group.php";
    public static String remove_group_project_evaluation_group = "remove_group_project_evaluation_group.php";
    public static String add_group_project_participant_marks = "add_group_project_participant_marks.php";
    public static String add_group_subproject = "add_group_subproject.php";
    public static String allocated_evaluation_groups = "allocated_evaluation_groups.php";
    public static String course_groups = "course_groups.php";
    public static String groupproject_subprojects = "groupproject_subprojects.php";
    public static String add_comment_report = "add_comment_report.php";
    public static String add_like_unlike_report = "add_like_unlike_report.php";
    public static String group_project_student_diary = "group_project_student_diary.php";
    public static String groupproject_subproject_final_report_details = "groupproject_subproject_final_report_details.php";
    public static String group_project_final_report_details = "group_project_final_report_details.php";
    public static String participant_details = "participant_details.php";
    public static String group_project_contribution_reports = "group_project_contribution_reports.php";
    public static String groupproject_subproject_contribution_reports = "groupproject_subproject_contribution_reports.php";
    public static String upload_group_project_contribution_report = "upload_group_project_contribution_report.php";
    public static String upload_groupproject_subproject_contribution_report = "upload_groupproject_subproject_contribution_report.php";
    public static String add_group_project_work = "add_group_project_work.php";
    public static String add_groupproject_subproject_work = "add_groupproject_subproject_work.php";
    public static String group_project_work_list = "group_project_work_list.php";
    public static String groupproject_subproject_work_list = "groupproject_subproject_work_list.php";
    public static String add_gcm_participant = "add_gcm_participant.php";




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

    public static final String[] ALLOWED_VIDEO_FORMATS =  new String[]{"mp4"};
    public static final String[] ALLOWED_AUDIO_FORMATS =  new String[]{"mp3"};
    public static final String[] ALLOWED_IMAGE_FORMATS =  new String[]{"jpeg","png"};
    public static final String[] ALLOWED_FILE_FORMATS =  new String[]{"doc","pdf","txt","xlsx"};

    public static final String UPLOAD_TEXT = "text";
    public static final String UPLOAD_LINK = "link";
    public static final String UPLOAD_VIDEO = "video";
    public static final String UPLOAD_IMAGE = "image";
    public static final String UPLOAD_AUDIO = "audio";
    public static final String UPLOAD_FILE = "file";


}
