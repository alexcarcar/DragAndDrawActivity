package alex.carcar.draganddraw

import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat

private const val TAG = "DragAndDrawActivity"

class DragAndDrawActivity : AppCompatActivity(),
    GestureDetector.OnGestureListener {

    lateinit var mDetector: GestureDetectorCompat
    lateinit var boxDrawingView: BoxDrawingView

    // Called when the activity is first created.
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_and_draw)
        mDetector = GestureDetectorCompat(this, this)
        boxDrawingView = findViewById(R.id.box_view)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        boxDrawingView.boxOnTouchEvent(event)
        return if (mDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }

    override fun onDown(event: MotionEvent): Boolean {
        Log.d(TAG, "onDown: (${event.x},${event.y})")
        return true
    }

    override fun onFling(
        event1: MotionEvent,
        event2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        Log.d(TAG, "onFling: (${event1.x},${event1.y}) -> (${event2.x},${event2.y})")
        boxDrawingView.boxFling()
        return true
    }

    override fun onLongPress(event: MotionEvent) {
        Log.d(TAG, "onLongPress: (${event.x},${event.y})")
    }

    override fun onScroll(
        event1: MotionEvent,
        event2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        Log.d(TAG, "onScroll: (${event1.x},${event1.y})")
        return true
    }

    override fun onShowPress(event: MotionEvent) {
        Log.d(TAG, "onShowPress: (${event.x},${event.y})")
    }

    override fun onSingleTapUp(event: MotionEvent): Boolean {
        Log.d(TAG, "onSingleTapUp: (${event.x},${event.y})")
        return true
    }
}