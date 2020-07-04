package alex.carcar.draganddraw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import org.json.JSONArray
import org.json.JSONObject

private const val TAG = "LineDrawingView"

class LineDrawingView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    init {
        isSaveEnabled = true
    }

    private var currentLine: Line? = null
    private val lines = mutableListOf<Line>()
    private val linePaint = Paint().apply {
        color = 0x22ff0000.toInt()
    }
    private val backgroundPaint = Paint().apply {
        color = 0xfff8efe0.toInt()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val myState = SavedState(superState)
        myState.json = lines.toString()
        return myState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        val jsonArray = JSONArray(savedState.json)
        var i = 0
        while (i < jsonArray.length()) {
            val jsonObject = jsonArray.get(i) as JSONObject
            val x1 = jsonObject.getDouble("x1").toFloat()
            val y1 = jsonObject.getDouble("y1").toFloat()
            val x2 = jsonObject.getDouble("x2").toFloat()
            val y2 = jsonObject.getDouble("y2").toFloat()
            val start = PointF(x1, y1)
            val end = PointF(x2, y2)
            currentLine = Line(start).also {
                it.end = end
                lines.add(it)
            }
            i++
        }
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val current = PointF(event.x, event.y)
        var action = ""
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"
                // Reset drawing state
                currentLine = Line(current).also {
                    lines.add(it)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"
                updateCurrentLine(current)
            }
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"
                updateCurrentLine(current)
                currentLine = null
            }
            MotionEvent.ACTION_CANCEL -> {
                action = "ACTION_CANCEL"
                currentLine = null
            }
        }
        Log.i(TAG, "$action at x=${current.x} y=${current.y}")
        return true
    }

    private fun updateCurrentLine(current: PointF) {
        currentLine?.let {
            it.end = current
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        // Fill the background
        canvas.drawPaint(backgroundPaint)
        linePaint.strokeWidth = 10f
        lines.forEach { l ->
            // canvas.drawRect(l.left, l.bottom, l.right, l.top, linePaint)
            canvas.drawLine(l.x1, l.y1, l.x2, l.y2, linePaint)
            // canvas.drawCircle((l.x1+l.x2)/2, (l.y1+l.y2)/2, l.length/2, linePaint)
        }
    }
}

private class SavedState internal constructor(superState: Parcelable?) :
    View.BaseSavedState(superState) {
    var json: String? = null
    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeString(json)
    }
}