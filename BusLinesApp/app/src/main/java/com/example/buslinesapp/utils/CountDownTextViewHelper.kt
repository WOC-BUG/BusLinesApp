package com.example.buslinesapp.utils

import android.graphics.Color
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView

/**
 * 倒计时TextView帮助类
 */
class CountDownTextViewHelper(private val textView: TextView,
    defaultString: String?, max: Int, interval: Int) {

    private val countDownTimer: CountDownTimer  // 倒计时
    private var listener: OnFinishListener? = null  // 倒计时结束的回调接口

    //开始 倒计时
    fun start() {
        textView.isEnabled = false
        countDownTimer.start()
    }

    /**
     * 设置倒计时结束的监听器
     * @param listener
     */
    fun setOnFinishListener(listener: OnFinishListener?) {
        this.listener = listener
    }

   //倒计时结束的回调接口
    interface OnFinishListener {
        fun finish()
    }

    /**
     *
     * @param textView
     * 需要显示倒计时的TextView
     * @param defaultString
     * 默认显示的字符串
     * @param max
     * 需要进行倒计时的最大值,单位是秒
     * @param interval
     * 倒计时的间隔，单位是秒
     */
    init {
        // 由于CountDownTimer并不是准确计时，在onTick方法调用的时候，time会有1-10ms左右的误差，这会导致最后一秒不会调用onTick()
        // 因此，设置间隔的时候，默认减去了10ms，从而减去误差。
        // 经过以上的微调，最后一秒的显示时间会由于10ms延迟的积累，导致显示时间比1s长max*10ms的时间，其他时间的显示正常,总时间正常
        countDownTimer = object : CountDownTimer(
            (max * 1000).toLong(),
            (interval * 1000 - 10).toLong()
        ) {
            override fun onTick(time: Long) {
                // 第一次调用会有1-10ms的误差，因此需要+15ms，防止第一个数不显示，第二个数显示2s
                textView.text = ((time + 15) / 1000)
                    .toString() + ""
                textView.setTextColor(Color.BLACK)
                Log.d("CountDownTextViewHelper", "time = " + time + " text = "
                            + (time + 15) / 1000
                )
            }

            override fun onFinish() {
                textView.isEnabled = true
                textView.text = defaultString
                if (listener != null) {
                    listener!!.finish()
                }
            }
        }
    }
}
