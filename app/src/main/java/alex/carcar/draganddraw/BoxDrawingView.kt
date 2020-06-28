package alex.carcar.draganddraw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

private const val TAG = "BoxDrawingView"
private const val TOUCH_SCALE_FACTOR: Float = 0.1550f

class BoxDrawingView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var currentBox: Box? = null
    private val boxen = mutableListOf<Box>()
    private val boxPaint = Paint().apply {
        color = 0x22ff0000.toInt()
    }
    private val backgroundPaint = Paint().apply {
        color = 0xfff8efe0.toInt()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val current = PointF(event.x, event.y)
        var action = ""
        if (event.pointerCount > 1) {
            Log.i(TAG, "multiple touches detected.")
            detectRotation(event)
            return true
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"
                angle = 0F
                // Reset drawing state
                currentBox = Box(current).also {
                    boxen.add(it)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"
                updateCurrentBox(current)
            }
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"
                if (angle != 0F) {
                    currentBox = null
                    this.pivotX = current.x
                    this.pivotY = current.y
                    this.setRotation(angle)
                    invalidate()
                } else {
                    updateCurrentBox(current)
                }
                currentBox = null
                angle = 0F
            }
            MotionEvent.ACTION_CANCEL -> {
                action = "ACTION_CANCEL"
                currentBox = null
                angle = 0F
            }
        }
        Log.i(TAG, "$action at x=${current.x} y=${current.y}")
        return true
    }

    private var previousX: Float = 0f
    private var previousY: Float = 0f
    private var angle: Float = 0f

    private fun detectRotation(e: MotionEvent) {
        val x: Float = e.x
        val y: Float = e.y

        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                var dx: Float = x - previousX
                var dy: Float = y - previousY
                // reverse direction of rotation above the mid-line
                if (y > height / 2) {
                    dx *= -1
                }
                // reverse direction of rotation to left of the mid-line
                if (x < width / 2) {
                    dy *= -1
                }
                angle += (dx + dy) * TOUCH_SCALE_FACTOR
            }
        }

        previousX = x
        previousY = y
    }

    private fun updateCurrentBox(current: PointF) {
        currentBox?.let {
            it.end = current
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        // Fill the background
        canvas.drawPaint(backgroundPaint)
        boxen.forEach { box ->
            canvas.drawRect(box.left, box.top, box.right, box.bottom, boxPaint)
        }
    }
}