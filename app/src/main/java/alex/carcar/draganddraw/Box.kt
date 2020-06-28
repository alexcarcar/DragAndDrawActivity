package alex.carcar.draganddraw

import android.graphics.PointF

class Box(val start: PointF) {
    var end: PointF = start

    val left: Float
        get() = Math.min(start.x, end.x)

    val right: Float
        get() = Math.max(start.x, end.x)

    val top: Float
        get() = Math.min(start.y, end.y)

    val bottom: Float
        get() = Math.max(start.y, end.y)

    override fun toString(): String {
        return "{x1:${start.x}, y1:${start.y}, x2:${end.x}, y2:${end.y}}"
    }
}