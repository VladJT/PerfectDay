package jt.projects.perfectday.core.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.allViews
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MySwipeRefreshLayout : SwipeRefreshLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        setOnChildScrollUpCallback { _, child ->
            if (child is MotionLayout) {
                val recycler = child.allViews.find { it is RecyclerView } as? RecyclerView
                    ?: return@setOnChildScrollUpCallback false
                child.progress == 1f || recycler.scrollState == RecyclerView.SCROLL_STATE_DRAGGING
            } else
                false
        }
    }
}