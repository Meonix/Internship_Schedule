package com.mionix.baseapp.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    const val FORMAT_DATE_CASE_1 = "yyyy.MM.dd G 'at' HH:mm:ss z"
    //2001.07.04 AD at 12:08:56 PDT
    const val FORMAT_DATE_CASE_2 = "EEE, MMM d, ''yy"
    //Wed, Jul 4, '01
    const val FORMAT_DATE_CASE_3 = "h:mm a"
    //12:08 PM
    const val FORMAT_DATE_CASE_4 = "hh 'o''clock' a, zzzz"
    //12 o'clock PM, Pacific Daylight Time
    const val FORMAT_DATE_CASE_5 = "K:mm a, z"
    //0:08 PM, PDT
    const val FORMAT_DATE_CASE_6 = 	"yyyyy.MMMMM.dd GGG hh:mm aaa"
    //02001.July.04 AD 12:08 PM
    const val FORMAT_DATE_CASE_7 =  "EEE, d MMM yyyy HH:mm:ss Z"
    //Wed, 4 Jul 2001 12:08:56 -0700
    const val FORMAT_DATE_CASE_8 = "yyMMddHHmmssZ"
    //010704120856-0700
    const val FORMAT_DATE_CASE_9 = "yyyy-MM-dd'T'HH:mm:ss Z"
    //2001-07-04T12:08:56.235-0700

    const val FORMAT_DATE_CASE_10 = "yyyy-MM-dd'T'HH:mm:ssXXX"
    //2001-07-04T12:08:56.235-07:00
    //2020-03-01T00:00:00+09:00
    const val FORMAT_DATE_CASE_11 = "YYYY-'W'ww-u"
    //2001-W27-3
    const val FORMAT_DATE_CASE_12 = "YYYYMMDDHHMMSS"

    const val FORMAT_DATE_CASE_13 = "yyyy-MM-dd"

    const val FORMAT_DATE_CASE_14 = "yyyy年MM月dd日"

    const val FORMAT_DATE_CASE_15 = "yyyy/MM/dd"

    const val FORMAT_DATE_CASE_16 = "MM月dd日"

    const val FORMAT_DATE_CASE_17 = "MM月dd日(E)"

    const val FORMAT_DATE_CASE_18 = "MM月yyyy年"

    const val FORMAT_DATE_CASE_19 = "yyyy/MM/dd hh:ss"

    const val FORMAT_DATE_CASE_20 = "yyyy年MM月dd日生まれ"

    //20200514T102501+0900
    const val FORMAT_DATE_CASE_21 = "yyyyMMdd'T'HHmmssZ"

    const val FORMAT_DATE_CASE_22 = "yyyy/MM/dd HH:mm"
    const val FORMAT_DATE_CASE_23 = "yyyyMMdd"

    private val mCalendar = Calendar.getInstance(Locale.JAPAN)

    fun toString(calendar: Calendar, formatString: String): String
    {
        var str = ""
        try {
            val sdf = SimpleDateFormat(formatString)
            str = sdf.format(calendar.time)
        }catch ( e: Exception)
        {
            return ""
        }
        return str
    }

    fun toString(date: Date, formatString: String): String
    {
        var str = ""
        try {
            val sdf = SimpleDateFormat(FORMAT_DATE_CASE_19)
            str = sdf.format(date)
        }catch (e:Exception)
        {
            return ""
        }
        return str
    }

    fun changeDate(calendar: Calendar, day: Int): String
    {
        var str = ""
        try {
            val sdf = SimpleDateFormat(FORMAT_DATE_CASE_19)
            calendar.add(Calendar.DATE,day)
            str = sdf.format(calendar.time)
        }catch ( e: Exception)
        {

        }
        return str

    }
    fun changeMonth(calendar: Calendar, month: Int): String
    {
        var str = ""
        try {
            val sdf = SimpleDateFormat(FORMAT_DATE_CASE_19)
            calendar.add(Calendar.MONTH,month)
            str = sdf.format(calendar.time)
        }catch ( e: Exception)
        {

        }
        return str

    }

    fun changeYear(calendar: Calendar, year: Int): String
    {
        var str = ""
        try {
            val sdf = SimpleDateFormat(FORMAT_DATE_CASE_19)
            calendar.add(Calendar.YEAR,year)
            str = sdf.format(calendar.time)
        }catch ( e: Exception)
        {

        }
        return str

    }

    fun convertDateToCalendar(date: Date): Calendar{
        //val calendar = Calendar.getInstance()
        try {
            mCalendar.time = date
        }catch (e: java.lang.Exception){
            return mCalendar
        }
        return mCalendar
    }

    fun convertStringToCalendar(str: String, formatStr: String): Calendar{
       // val calendar = Calendar.getInstance()
        try {
            val sdf = SimpleDateFormat(formatStr)
            val date = sdf.parse(str)
            mCalendar.time = date

        }catch (e: Exception){
            return mCalendar
        }
        return mCalendar
    }

    fun convertFromStringToString(str: String, formatStrFrom: String, formatStrTo: String): String{
      //  var date : Date = Date()

        var mStr = ""
        try {
            val date1 = SimpleDateFormat(formatStrFrom,Locale.JAPAN).parse(str)
          //  date = calendar.time
            val sdf = SimpleDateFormat(formatStrFrom,Locale.JAPAN)
         //   date = sdf.parse(str)
            mCalendar.time = date1
            sdf.applyPattern(formatStrTo)
            mStr = sdf.format(date1)
        }catch (e:Exception){
            return mStr
        }
        return mStr
    }

    fun getMonth(str: String, strFormat: String): Int{
        var mMonth = 0
        try {
            val date1 = SimpleDateFormat(strFormat).parse(str)

            mCalendar.time = date1
            mMonth = mCalendar.get(Calendar.MONTH)

        }catch (e:Exception){
            return mMonth
        }
        return mMonth
    }

    fun getYear(str: String,strFormat: String):Int{
        var mYear = 0
        try {
            val date1 = SimpleDateFormat(strFormat).parse(str)

            mCalendar.time = date1
            mYear = mCalendar.get(Calendar.YEAR)

        }catch (e:java.lang.Exception){
            return mYear
        }
        return mYear
    }
    fun toDate(str: String, formatStr: String): Date{
        var date : Date? = null
        try {
            val sdf = SimpleDateFormat(formatStr)
             date = sdf.parse(str)
        }catch (e:java.lang.Exception){
            return Date()
        }
        return date
    }

    fun toDateCurrent(): Date
    {
        return  Date()
    }

}