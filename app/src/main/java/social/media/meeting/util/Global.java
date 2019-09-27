package social.media.meeting.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import social.media.meeting.member.Member;

public class Global {

    public static boolean is_task_area = false;
    public static String current_user_name = "";
    public static String current_user_email = "";
    public static String current_user_photo;
    public static int current_user_index;

    public static Integer mk_task_progress = 0;
    public static boolean validate_newtask = true;
    public static boolean is_admin = false;

    public static String task_id = "";
    public static String task_description = "";
    public static String task_start_date = "";
    public static String task_end_date = "";
    public static String task_deadline;
    public static String project_name;
    public static String[] task_hired_members;

    public static boolean is_hiring_member = false;
    public static ArrayList<Member> array_all_members = new ArrayList<Member>();
    public static ArrayList<Member> array_hired_members = new ArrayList<Member>();
    public static ArrayList<Boolean> hired_status = new ArrayList<Boolean>();
    public static List<String> list_project = new ArrayList<String>();



    public static String INDEX = "index";
    public static String PRO_INDEX = "pro_index";
    public static String ORIGIN = "orgin";
    public static String MYTASK = "my_task";
    public static String ALLTASK = "all_task";
    public static String FROMPROJECT = "from_pro";
    public static String I_CREATED = "I_created";
    public static String FROM_MAIN = "from_main";
    public static String FLAG = "flag";
    public static String today = "";
    public static String TYPE = "type";
    public static String TYPE_EDIT = "edit";
    public static String TYPE_NEW = "new";
    public static String  TYPE_DELETE = "delete";


    public static String getCountOfDays(String start_date, String end_date) {
        if((!(start_date.equals("")))&&(!end_date.equals(""))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
            try {
                createdConvertedDate = dateFormat.parse(start_date);
                expireCovertedDate = dateFormat.parse(end_date);

                Date today = new Date();

                todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int cYear = 0, cMonth = 0, cDay = 0;

            if (createdConvertedDate.after(todayWithZeroTime)) {
                Calendar cCal = Calendar.getInstance();
                cCal.setTime(createdConvertedDate);
                cYear = cCal.get(Calendar.YEAR);
                cMonth = cCal.get(Calendar.MONTH);
                cDay = cCal.get(Calendar.DAY_OF_MONTH);

            } else {
                Calendar cCal = Calendar.getInstance();
                cCal.setTime(todayWithZeroTime);
                cYear = cCal.get(Calendar.YEAR);
                cMonth = cCal.get(Calendar.MONTH);
                cDay = cCal.get(Calendar.DAY_OF_MONTH);
            }


    /*Calendar todayCal = Calendar.getInstance();
    int todayYear = todayCal.get(Calendar.YEAR);
    int today = todayCal.get(Calendar.MONTH);
    int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
    */

            Calendar eCal = Calendar.getInstance();
            eCal.setTime(expireCovertedDate);

            int eYear = eCal.get(Calendar.YEAR);
            int eMonth = eCal.get(Calendar.MONTH);
            int eDay = eCal.get(Calendar.DAY_OF_MONTH);

            Calendar date1 = Calendar.getInstance();
            Calendar date2 = Calendar.getInstance();

            date1.clear();
            date1.set(cYear, cMonth, cDay);
            date2.clear();
            date2.set(eYear, eMonth, eDay);

            long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

            float dayCount = (float) diff / (24 * 60 * 60 * 1000);
            if(dayCount > 0) {
                String duration = ("" + (int) dayCount);
                return duration;
            }
            else {

            }
        }
        return "invalid";
    }

    public static Integer getRandom(){
        final int min = 1000;
        final int max = 9999;
        Integer random  = new Random().nextInt((max - min) + 1) + min;
        return random;
    }
    public static String getToday(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String str = sdf.format(currentTime);
        String sub_id = String.valueOf(currentTime.getHours())+String.valueOf(currentTime.getMinutes());
        return currentTime.toString();
    }

}
