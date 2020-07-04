package alex.carcar.draganddraw

import android.graphics.PointF

class Line(val start: PointF) {
    var end: PointF = start

    val left: Float get() = Math.min(start.x, end.x)
    val right: Float get() = Math.max(start.x, end.x)
    val top: Float get() = Math.min(start.y, end.y)
    val bottom: Float get() = Math.max(start.y, end.y)

    val x1: Float get() = start.x
    val y1: Float get() = start.y
    val x2: Float get() = end.x
    val y2: Float get() = end.y

    val length: Float get() {
        val dx = (start.x - end.x).toDouble()
        val dy = (start.y - end.y).toDouble()
        return Math.sqrt(dx*dx + dy*dy).toFloat()
    }

    override fun toString(): String {
        return "{x1:${start.x}, y1:${start.y}, x2:${end.x}, y2:${end.y}}"
    }
}