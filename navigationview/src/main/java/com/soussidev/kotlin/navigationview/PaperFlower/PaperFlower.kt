package com.soussidev.kotlin.navigationview.PaperFlower

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.OvershootInterpolator

import java.util.ArrayList
import java.util.Arrays
import java.util.Random

/**
 * Created by Soussi on 06/09/2017.
 */

class PaperFlower : View {

    internal var colors = intArrayOf(0xFFDF4288.toInt(), 0xFFCD8BF8.toInt(), 0XFF2B9DF2.toInt(), 0XFFA4EEB4.toInt(), 0XFFE097CA.toInt(), 0XFFCAACC6.toInt(), 0XFFC5A5FC.toInt(), 0XFFF5BC16.toInt(), 0XFFF2DFC8.toInt(), 0XFFE1BE8E.toInt(), 0XFFC8C79D.toInt(), 0XFFC0C11D.toInt())
    internal var dotList: MutableList<Dot> = ArrayList()
    private val ANIMATE_DURATION: Long = 1000
    private var MAX_RADIUS = 150f
    private var MAX_CIRCLE_RADIUS = 100f
    private var progress: Float = 0.toFloat()
    private var circlePaint: Paint? = null
    private val RING_WIDTH = 15f
    private val P1 = 0.20f
    private val P2 = 0.28f
    private val P3 = 0.30f
    private var DOT_NUMBER = 20
    private var DOT_BIG_RADIUS = 10f
    private var DOT_SMALL_RADIUS = 7f
    private val mExpandInset = IntArray(2)
    private var mListener: PaperFlowerListener? = null
    private var centerY: Int = 0
    private var centerX: Int = 0


    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        circlePaint!!.style = Paint.Style.FILL
        circlePaint!!.color = Color.BLACK
    }

    /**
     * listener for animation change time
     *
     * @param listener
     */
    fun setmListener(listener: PaperFlowerListener) {
        this.mListener = listener
    }

    /**
     * set different colors for last dots
     *
     * @param newColors
     */
    fun setColors(newColors: IntArray) {
        this.colors = Arrays.copyOf(newColors, newColors.size)
    }

    /**
     * set small dot number
     * @param dotNumber
     */
    fun setDotNumber(dotNumber: Int) {
        DOT_NUMBER = dotNumber
    }

    @JvmOverloads
    fun paperflower(view: View, listener: PaperFlowerListener? = null) {
        paperflower(view, -1f, listener)
    }

    fun paperflower(view: View, radius: Float, listener: PaperFlowerListener?) {

        // set listener
        if (listener != null) {
            setmListener(listener)
            this.mListener!!.onAnimationStart()
        }

        val r = Rect()
        view.getGlobalVisibleRect(r)
        val location = IntArray(2)
        getLocationOnScreen(location)
        r.offset(-location[0], -location[1])
        r.inset(-mExpandInset[0], -mExpandInset[1])

        centerX = r.left + r.width() / 2
        centerY = r.top + r.height() / 2

        if (radius != -1f) {
            initRadius(radius)
        } else {
            initRadius(Math.max(r.width(), r.height()).toFloat())
        }

        view.scaleX = 0.1f
        view.scaleY = 0.1f
        val animator = ValueAnimator.ofFloat(0f, 1f).setDuration((ANIMATE_DURATION * 0.5f).toLong())
        animator.addUpdateListener { animation ->
            val animatedFraction = animation.animatedFraction
            view.scaleX = 0.1f + animatedFraction * 0.9f
            view.scaleY = 0.1f + animatedFraction * 0.9f
        }
        animator.interpolator = OvershootInterpolator(2f)
        animator.startDelay = (ANIMATE_DURATION * P3).toLong()

        animator.start()
        paperflower()

    }

    private fun initRadius(max_circle_radius: Float) {
        MAX_CIRCLE_RADIUS = max_circle_radius
        MAX_RADIUS = MAX_CIRCLE_RADIUS * 1.4f
        DOT_BIG_RADIUS = MAX_CIRCLE_RADIUS * 0.1f
        DOT_SMALL_RADIUS = DOT_BIG_RADIUS * 0.1f
    }

    private fun paperflower() {
        val valueAnimator = ValueAnimator.ofFloat(0F, 1F).setDuration(ANIMATE_DURATION)
        valueAnimator.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            progress = animation.animatedValue as Float
            invalidate()
        })
        valueAnimator.start()
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (mListener != null) {
                    mListener!!.onAnimationEnd()
                }
            }
        })
        initDots()
    }

    private fun initDots() {

        val random = Random(System.currentTimeMillis())
        for (i in 0..DOT_NUMBER * 2 - 1) {
            val dot = Dot()
            dot.startColor = colors[random.nextInt(99999) % colors.size]
            dot.endColor = colors[random.nextInt(99999) % colors.size]
            dotList.add(dot)
        }
    }

    override fun onDraw(canvas: Canvas) {


        if (progress >= 0 && progress <= P1) {
            var progress1 = 1f / P1 * progress
            if (progress1 > 1) progress1 = 1f
            val startColor = colors[0]
            val endColor = colors[1]
            circlePaint!!.style = Paint.Style.FILL
            circlePaint!!.color = evaluateColor(startColor, endColor, progress1)
            canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), MAX_CIRCLE_RADIUS * progress1, circlePaint!!)
        } else if (progress > P1) {

            if (progress > P1 && progress <= P3) {
                var progress2 = (progress - P1) / (P3 - P1)
                if (progress2 < 0) progress2 = 0f
                if (progress2 > 1) progress2 = 1f


                circlePaint!!.style = Paint.Style.STROKE
                val strokeWidth = MAX_CIRCLE_RADIUS * (1 - progress2)
                circlePaint!!.strokeWidth = strokeWidth

                canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), MAX_CIRCLE_RADIUS * progress2 + strokeWidth / 2, circlePaint!!)
            }
            if (progress >= P2) {
                circlePaint!!.style = Paint.Style.FILL
                val progress3 = (progress - P2) / (1 - P2)
                val r = MAX_CIRCLE_RADIUS + progress3 * (MAX_RADIUS - MAX_CIRCLE_RADIUS)


                var i = 0
                while (i < dotList.size) {
                    val dot = dotList[i]
                    circlePaint!!.color = evaluateColor(dot.startColor, dot.endColor, progress3)

                    val x = (r * Math.cos(i.toDouble() * 2.0 * Math.PI / DOT_NUMBER)).toFloat() + centerX
                    val y = (r * Math.sin(i.toDouble() * 2.0 * Math.PI / DOT_NUMBER)).toFloat() + centerY
                    canvas.drawCircle(x, y, DOT_BIG_RADIUS * (1 - progress3), circlePaint!!)

                    val dot2 = dotList[i + 1]

                    circlePaint!!.color = evaluateColor(dot2.startColor, dot2.endColor, progress3)
                    val x2 = (r * Math.cos(i.toDouble() * 2.0 * Math.PI / DOT_NUMBER + 0.2)).toFloat() + centerX
                    val y2 = (r * Math.sin(i.toDouble() * 2.0 * Math.PI / DOT_NUMBER + 0.2)).toFloat() + centerY
                    canvas.drawCircle(x2, y2, DOT_SMALL_RADIUS * (1 - progress3), circlePaint!!)
                    i += 2

                }
            }
        }
    }

    private fun evaluateColor(startValue: Int, endValue: Int, fraction: Float): Int {
        if (fraction <= 0) {
            return startValue
        }
        if (fraction >= 1) {
            return endValue
        }
        val startA = startValue shr 24 and 0xff
        val startR = startValue shr 16 and 0xff
        val startG = startValue shr 8 and 0xff
        val startB = startValue and 0xff

        val endA = endValue shr 24 and 0xff
        val endR = endValue shr 16 and 0xff
        val endG = endValue shr 8 and 0xff
        val endB = endValue and 0xff

        return startA + (fraction * (endA - startA)).toInt() shl 24 or (startR + (fraction * (endR - startR)).toInt() shl 16) or (startG + (fraction * (endG - startG)).toInt() shl 8) or startB + (fraction * (endB - startB)).toInt()
    }

    internal inner class Dot {
        var startColor: Int = 0
        var endColor: Int = 0
    }
    /**
     * @auteur Soussi Mohamed .
     */
    companion object {

        fun attach2Window(activity: Activity): PaperFlower {
            val rootView = activity.findViewById<View>(Window.ID_ANDROID_CONTENT) as ViewGroup
            val smallBang = PaperFlower(activity)
            rootView.addView(smallBang, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            return smallBang
        }
    }
}
