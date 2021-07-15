package com.giron.android.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout

class CircleFrameLayout : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr)

    private var path: Path = Path()

    /**
     * 丸める処理
     */
    private fun rebuildPath(w: Float, h: Float) {
        val rect = RectF(0F, 0F, w, h)

        // 角を幅の半分でまとめる
        path.reset()
        path.addRoundRect(rect, w / 2, h / 2, Path.Direction.CW)
        path.close()
    }

    /**
     * サイズ変更
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        rebuildPath(w.toFloat(), h.toFloat())
    }

    override fun dispatchDraw(canvas: Canvas?) {
        val save = canvas?.save()
        canvas?.clipPath(path)
        super.dispatchDraw(canvas)

        save?.let {
            canvas.restoreToCount(it)
        }
    }
}