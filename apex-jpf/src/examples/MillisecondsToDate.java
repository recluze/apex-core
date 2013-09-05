
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
 
public class MillisecondsToDate {
    public static void main(String[] args) {
        //
        // Create a DateFormatter object for displaying date information.
        //
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
         
        //
        // Get date and time information in milliseconds
        //
        long now = System.currentTimeMillis();
 
        //
        // Create a calendar object that will convert the date and time value
        // in milliseconds to date. We use the setTimeInMillis() method of the
        // Calendar object.
        //
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
         
        System.out.println(now + " = " + formatter.format(calendar.getTime()));
    }
}