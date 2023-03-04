package com.example.clockCustomView.common.presentation

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.example.clockCustomView.common.R
import java.util.*


class Clock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val clockRect = RectF()

    private val primaryColor = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.blue_grey)
    }

    private val secondaryColor = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    private var h = 0
    private var w = 0
    private var padding = 0
    private var fontSize = 0
    private val numeralSpacing = 0
    private var handTruncation = 0
    private var hourHandTruncation: Int = 0
    private var radius = 0
    private var paint: Paint? = null
    private var isInit = false
    private val numbers = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private val rect = Rect()

    private fun initClock() {
        h = height
        w = width
        padding = numeralSpacing + 50
        fontSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 13f,
            resources.displayMetrics
        ).toInt()
        val min = Math.min(h, w)
        radius = min / 2 - padding
        handTruncation = min / 20
        hourHandTruncation = min / 7
        paint = Paint()
        isInit = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        /*val smallestDimension = if (measuredh < measuredw) {
            measuredh
        } else {
            measuredw
        }

        val radius = smallestDimension / 2f
        canvas.drawCircle(radius, radius, radius, primaryColor)

        Timber.d(measuredh.toString())
        Timber.d(measuredw.toString())
        Timber.d(smallestDimension.toString())
        Timber.d(radius.toString())

        secondaryColor.textSize = radius * 0.15f
        secondaryColor.textAlign = Paint.Align.CENTER
        for (i in 1..12) {
            val angle = i * Math.PI / 6
            canvas.rotate(angle.toFloat())
            canvas.translate(0f, -radius * 0.85f)
            canvas.rotate(-angle.toFloat())
            canvas.drawText(i.toString(), 0f, 0f, secondaryColor)
            canvas.rotate(angle.toFloat())
            canvas.translate(0f, radius * 0.85f)
            canvas.rotate(-angle.toFloat())
        }

        val time = LocalDateTime.now()
        val hour = time.hour % 12*/

        if (!isInit) {
            initClock();
        }

        canvas.drawColor(Color.BLACK);
        drawCircle(canvas);
        drawCenter(canvas);
        drawNumeral(canvas);
        drawHands(canvas);

        postInvalidateDelayed(500);
    }

    /*private fun drawHand(canvas: Canvas, position: Float, length: Float, w: Float) {
        val path = Path()
        path.moveTo(0f, 0f)
    }*/

    private fun drawHand(canvas: Canvas, loc: Double, isHour: Boolean) {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        val handRadius: Int =
            if (isHour) radius - handTruncation - hourHandTruncation else radius - handTruncation
        canvas.drawLine(
            (w / 2).toFloat(), (h / 2).toFloat(),
            (w / 2 + Math.cos(angle) * handRadius).toFloat(),
            (h / 2 + Math.sin(angle) * handRadius).toFloat(),
            paint!!
        )
    }

    private fun drawHands(canvas: Canvas) {
        val c: Calendar = Calendar.getInstance()
        var hour: Int = c.get(Calendar.HOUR_OF_DAY)
        hour = if (hour > 12) hour - 12 else hour
        drawHand(canvas, (hour + c.get(Calendar.MINUTE) / 60f) * 5.0, true)
        drawHand(canvas, c.get(Calendar.MINUTE).toDouble(), false)
        drawHand(canvas, c.get(Calendar.SECOND).toDouble(), false)
    }

    private fun drawNumeral(canvas: Canvas) {
        paint?.setTextSize(fontSize.toFloat())
        for (number in numbers) {
            val tmp = number.toString()
            paint!!.getTextBounds(tmp, 0, tmp.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (w / 2 + Math.cos(angle) * radius - rect.width() / 2).toInt()
            val y = (h / 2 + Math.sin(angle) * radius + rect.height() / 2).toInt()
            canvas.drawText(tmp, x.toFloat(), y.toFloat(), paint!!)
        }
    }

    private fun drawCenter(canvas: Canvas) {
        paint!!.setStyle(Paint.Style.FILL)
        canvas.drawCircle((w / 2).toFloat(), (h / 2).toFloat(), 12f, paint!!)
    }

    private fun drawCircle(canvas: Canvas) {
        paint!!.reset()
        paint!!.setColor(resources.getColor(android.R.color.white))
        paint!!.strokeWidth = 5f
        paint!!.setStyle(Paint.Style.STROKE)
        paint!!.setAntiAlias(true)
        canvas.drawCircle(
            (w / 2).toFloat(),
            (h / 2).toFloat(), (radius + padding - 10).toFloat(), paint!!
        )
    }
}