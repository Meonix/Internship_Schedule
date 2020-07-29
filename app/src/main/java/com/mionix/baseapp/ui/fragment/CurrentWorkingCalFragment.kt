package com.mionix.baseapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mionix.baseapp.R
import com.mionix.baseapp.ui.activity.CurrentWorkingCalMothViewActivity
import com.mionix.baseapp.ui.activity.RegisterTimeWorkingActivity
import com.mionix.baseapp.utils.onClickThrottled
import kotlinx.android.synthetic.main.fragment_current_working_cal.*
import java.time.LocalDate

class CurrentWorkingCalFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_working_cal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleOnClick()
    }

    private fun handleOnClick() {
        btJanuary.onClickThrottled {
            openMothWorkingCalendar(1)
        }
        btFebruary.onClickThrottled {
            openMothWorkingCalendar(2)
        }
        btMarch.onClickThrottled {
            openMothWorkingCalendar(3)
        }
        btApril.onClickThrottled {
            openMothWorkingCalendar(4)
        }
        btMay.onClickThrottled {
            openMothWorkingCalendar(5)
        }
        btJune.onClickThrottled {
            openMothWorkingCalendar(6)
        }
        btJuly.onClickThrottled {
            openMothWorkingCalendar(7)
        }
        btAugust.onClickThrottled {
            openMothWorkingCalendar(8)
        }
        btSeptember.onClickThrottled {
            openMothWorkingCalendar(9)
        }
        btOctober.onClickThrottled {
            openMothWorkingCalendar(10)
        }
        btNovember.onClickThrottled {
            openMothWorkingCalendar(11)
        }
        btDecember.onClickThrottled {
            openMothWorkingCalendar(12)
        }
        btRegisterTimeWorking.onClickThrottled {
            val intent = Intent(context, RegisterTimeWorkingActivity::class.java)
            startActivity(intent)
        }
    }
    private fun openMothWorkingCalendar(intMoth: Int) {
        val intent = Intent(context,CurrentWorkingCalMothViewActivity::class.java)
        intent.putExtra(CurrentWorkingCalMothViewActivity.KEY_MOTH,intMoth)
        startActivity(intent)
    }
    companion object {
        fun newInstance(): CurrentWorkingCalFragment {
            val args = Bundle()
            val fragment = CurrentWorkingCalFragment()
            fragment.arguments = args
            return fragment
        }
    }


}