package com.mionix.baseapp.ui.activity

import android.animation.ValueAnimator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.updateLayoutParams
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.model.InDateStyle
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.yearMonth
import com.mionix.baseapp.R
import com.mionix.baseapp.ui.base.BaseBackButtonActivity
import com.mionix.baseapp.ui.custom.setTextColorRes
import com.mionix.baseapp.ui.fragment.CurrentWorkingCalFragment
import com.mionix.baseapp.utils.onClickThrottled
import kotlinx.android.synthetic.main.activity_current_working_cal_moth_view.*
import kotlinx.android.synthetic.main.activity_current_working_cal_moth_view.calendarView
import kotlinx.android.synthetic.main.activity_current_working_cal_moth_view.spTypeOfTime
import kotlinx.android.synthetic.main.activity_register_time_working.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

class CurrentWorkingCalMothViewActivity : BaseBackButtonActivity() {
    companion object {
        const val KEY_MOTH = "key_moth"
        const val KEY_UID = "uid"
        const val KEY_POSITION = "position"
    }
    private var userRef = FirebaseDatabase.getInstance().reference.child("Users")
    private var fullTimeDateList = mutableSetOf<LocalDate>()
    private var morningTimeDateList = mutableSetOf<LocalDate>()
    private var afternoonTimeDateList = mutableSetOf<LocalDate>()
    private var selectedDates = mutableListOf<LocalDate>()
    private var listDateData = mutableListOf<DataWorkingDay>()

    private val today = LocalDate.now()
    private var intMonth = 0
    private var uid = ""
    private  var position = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_working_cal_moth_view)
        setUpData()
        setUpView()
        handleOnclick()
    }

    private fun handleOnclick() {
        btUpdateTimeWorking.onClickThrottled {
            var fullTime = ""
            var morningTime =""
            var afternoonTime = ""
            val workingRef = userRef.child(uid).child("working_time")
            listDateData.forEach {
//                Log.d("DUY",it.date.dayOfMonth.toString())
//                Log.d("DUY",it.type.toString())
                when(it.type){
                    1 -> {
                        fullTime += "," + it.date.dayOfMonth.toString()
                    }
                    2-> {
                        morningTime += "," + it.date.dayOfMonth.toString()
                    }
                    3-> {
                        afternoonTime += "," + it.date.dayOfMonth.toString()
                    }
                }
            }
            workingRef.child(intMonth.toString()).child("full_time").setValue(fullTime)
            workingRef.child(intMonth.toString()).child("afternoon_time").setValue(afternoonTime)
            workingRef.child(intMonth.toString()).child("morning_time").setValue(morningTime).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this@CurrentWorkingCalMothViewActivity,"Update Successful..", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@CurrentWorkingCalMothViewActivity,it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
            onBackPressed()
        }
    }

    private fun setUpData() {


    }

    override fun setTitleToolbar(): String {
        return "Working Schedule in "
    }

    private fun setUpView() {
        intMonth = intent.getIntExtra(KEY_MOTH,0)
        intent.getStringExtra(KEY_UID)?.let {
            uid = it
        }
        intent.getStringExtra(KEY_POSITION)?.let{
            position = it
        }
        val currentUserRef = userRef.child(uid)
        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.textView.text = day.date.dayOfMonth.toString()
            }
        }
        //itnit date of Month
        val calendar = Calendar.getInstance()
        val currentMonth = YearMonth.of(calendar.get(Calendar.YEAR),intMonth)
        this.setTitleToolbar("Working Schedule in ${currentMonth.month}")
        val firstMonth = currentMonth.minusMonths(0)
        val lastMonth = currentMonth.plusMonths(0)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
        //create class DayViewContainer
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataFullTime = dataSnapshot.child("working_time").child(intMonth.toString()).child("full_time").getValue(String::class.java)
                val dataMorningTime = dataSnapshot.child("working_time").child(intMonth.toString()).child("morning_time").getValue(String::class.java)
                val dataAfternoonTime = dataSnapshot.child("working_time").child(intMonth.toString()).child("afternoon_time").getValue(String::class.java)
                if(dataFullTime!=null){
                    val arrayData = dataFullTime.split(",")
                    for (x in 1 until arrayData.size){
                        fullTimeDateList.add(LocalDate.of(calendar.get(Calendar.YEAR),intMonth, arrayData[x].toInt()))
                    }
                }
                if(dataMorningTime!=null){
                    val arrayData = dataMorningTime.split(",")
                    for (x in 1 until arrayData.size){
                        morningTimeDateList.add(LocalDate.of(calendar.get(Calendar.YEAR),intMonth, arrayData[x].toInt()))
                    }
                }
                if(dataAfternoonTime!=null){
                    val arrayData = dataAfternoonTime.split(",")
                    for (x in 1 until arrayData.size){
                        afternoonTimeDateList.add(LocalDate.of(calendar.get(Calendar.YEAR),intMonth, arrayData[x].toInt()))
                    }
                }
                class DayViewContainer(view: View) : ViewContainer(view) {
                    // Will be set when this container is bound. See the dayBinder.
                    lateinit var day: CalendarDay
                    val textView = view.findViewById<TextView>(R.id.exOneDayText)
                    init {
                        view.setOnClickListener {
                            if (day.owner == DayOwner.THIS_MONTH && (position == "admin" ||position =="system_admin" )) {
                                if (selectedDates.contains(day.date)||
                                    fullTimeDateList.contains(day.date)||
                                    morningTimeDateList.contains(day.date)||
                                    afternoonTimeDateList.contains(day.date)) {

                                    listDateData.removeIf { it.date == day.date}
                                    fullTimeDateList.remove(day.date)
                                    morningTimeDateList.remove(day.date)
                                    afternoonTimeDateList.remove(day.date)
                                    selectedDates.remove(day.date)
                                } else {
                                    selectedDates.add(day.date)
                                }
                                calendarView.notifyDayChanged(day)
                            }
                        }
                    }
                }
                //Using class DayViewContainer to handle event pick on calendar
                calendarView.dayBinder =object : DayBinder<DayViewContainer> {
                    override fun create(view: View) = DayViewContainer(view)
                    override fun bind(container: DayViewContainer, day: CalendarDay) {
                        container.day = day
                        val textView = container.textView
                        textView.text = day.date.dayOfMonth.toString()
//                        selectedDates.forEach{
//                            Log.d("DUY",selectedDates.size.toString()+selectedDates.contains(it).toString())
//                            if(fullTimeDateList.contains(it)){
//                                textView.setTextColorRes(R.color.example_1_bg)
//                                textView.setBackgroundResource(R.drawable.fullday_selected)
//                            }
//                        }

                        if (day.owner == DayOwner.THIS_MONTH) {
                            when {
                                selectedDates.contains(day.date) -> {
                                    when (spTypeOfTime.selectedItemPosition) {
                                        0 -> {
                                            listDateData.add(
                                                DataWorkingDay(1, day.date)
                                            )
                                            textView.setTextColorRes(R.color.example_1_bg)
                                            textView.setBackgroundResource(R.drawable.fullday_selected)
                                        }
                                        1 -> {
                                            listDateData.add(
                                                DataWorkingDay(2, day.date)
                                            )
                                            textView.setTextColorRes(R.color.example_1_bg)
                                            textView.setBackgroundResource(R.drawable.example_1_selected_bg)
                                        }
                                        else -> {
                                            listDateData.add(
                                                DataWorkingDay(3, day.date)
                                            )
                                            textView.setTextColorRes(R.color.example_1_bg)
                                            textView.setBackgroundResource(R.drawable.affternoon_selected)
                                        }
                                    }
                                }
                                fullTimeDateList.contains(day.date) -> {
                                    listDateData.add(
                                        DataWorkingDay(1, day.date)
                                    )
                                    textView.setTextColorRes(R.color.example_1_bg)
                                    textView.setBackgroundResource(R.drawable.fullday_selected)
                                }
                                morningTimeDateList.contains(day.date) ->{
                                    listDateData.add(
                                        DataWorkingDay(2, day.date)
                                    )
                                    textView.setTextColorRes(R.color.example_1_bg)
                                    textView.setBackgroundResource(R.drawable.example_1_selected_bg)
                                }
                                afternoonTimeDateList.contains(day.date) ->{
                                    listDateData.add(
                                        DataWorkingDay(1, day.date)
                                    )
                                    textView.setTextColorRes(R.color.example_1_bg)
                                    textView.setBackgroundResource(R.drawable.affternoon_selected)
                                }
                                today == day.date -> {
                                    textView.setTextColorRes(R.color.example_1_white)
                                    textView.setBackgroundResource(R.drawable.example_1_today_bg)
                                }
                                else -> {
                                    textView.setTextColorRes(R.color.example_1_white)
                                    textView.background = null
                                }
                            }

                        } else {
                            textView.setTextColorRes(R.color.example_1_white_light)
                            textView.background = null
                        }
                        val c = Calendar.getInstance()
                        c.set(c.get(Calendar.YEAR),intMonth-1,day.date.dayOfMonth)
                        if(day.owner == DayOwner.THIS_MONTH&&(c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)){
                            textView.setTextColorRes(R.color.colorPrimaryDark)
                            textView.background = null
                        }
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError?) {

            }
        }
        currentUserRef.addValueEventListener(eventListener)
        val spinnerLeft = arrayOf("Full Working Day","Morning","Afternoon")
        val arrayAdapterLeft = ArrayAdapter(this@CurrentWorkingCalMothViewActivity,R.layout.support_simple_spinner_dropdown_item,spinnerLeft)
        spTypeOfTime.adapter = arrayAdapterLeft
//
//        weekModeCheckBox.setOnCheckedChangeListener { _, monthToWeek ->
//            val firstDate = calendarView.findFirstVisibleDay()?.date ?: return@setOnCheckedChangeListener
//            val lastDate = calendarView.findLastVisibleDay()?.date ?: return@setOnCheckedChangeListener
//
//            val oneWeekHeight = calendarView.dayHeight
//            val oneMonthHeight = oneWeekHeight * 6
//
//            val oldHeight = if (monthToWeek) oneMonthHeight else oneWeekHeight
//            val newHeight = if (monthToWeek) oneWeekHeight else oneMonthHeight
//
//            // Animate calendar height changes.
//            val animator = ValueAnimator.ofInt(oldHeight, newHeight)
//            animator.addUpdateListener { animator ->
//                calendarView.updateLayoutParams {
//                    height = animator.animatedValue as Int
//                }
//            }
//
//            // When changing from month to week mode, we change the calendar's
//            // config at the end of the animation(doOnEnd) but when changing
//            // from week to month mode, we change the calendar's config at
//            // the start of the animation(doOnStart). This is so that the change
//            // in height is visible. You can do this whichever way you prefer.
//
//            animator.doOnStart {
//                if (!monthToWeek) {
//                    calendarView.apply {
//                        inDateStyle = InDateStyle.ALL_MONTHS
//                        maxRowCount = 6
//                        hasBoundaries = true
//                    }
//                }
//            }
//            animator.doOnEnd {
//                if (monthToWeek) {
//                    calendarView.apply {
//                        inDateStyle = InDateStyle.FIRST_MONTH
//                        maxRowCount = 1
//                        hasBoundaries = false
//                    }
//                }
//
//                if (monthToWeek) {
//                    // We want the first visible day to remain
//                    // visible when we change to week mode.
//                    if(today.monthValue==intMonth){
//                        calendarView.scrollToDate(today)
//                    }
//                } else {
//                    // When changing to month mode, we choose current
//                    // month if it is the only one in the current frame.
//                    // if we have multiple months in one frame, we prefer
//                    // the second one unless it's an outDate in the last index.
//                    if (firstDate.yearMonth == lastDate.yearMonth) {
//                        calendarView.scrollToMonth(firstDate.yearMonth)
//                    } else {
//                        // We compare the next with the last month on the calendar so we don't go over.
//                        calendarView.scrollToMonth(minOf(firstDate.yearMonth.next, lastMonth))
//                    }
//                }
//            }
//            animator.duration = 250
//            animator.start()
//        }
    }
    private class DataWorkingDay(var type:Int,var date: LocalDate)

    class DayViewContainer(view: View) : ViewContainer(view) {
        val textView = view.findViewById<TextView>(R.id.exOneDayText)

        // With ViewBinding
        // val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
    }
}