package com.oan.management.utility;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Oan on 19/03/2018.
 */
public class StringToDate {
    public Date convert(String date) {
        Date sqlDate = new Date(Calendar.getInstance().getTime().getTime());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            sqlDate = new Date(format.parse(date).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return sqlDate;
        }
    }
}
