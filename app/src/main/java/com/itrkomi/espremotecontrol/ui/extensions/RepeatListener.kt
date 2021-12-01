import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener

class RepeatListener(
    initialInterval: Int,
    normalInterval: Int,
    clickListener: View.OnClickListener?,
    cancelClickListener: View.OnClickListener?
) :
    OnTouchListener {
    private val handler = Handler()
    private val initialInterval: Int
    private val normalInterval: Int
    private val clickListener: View.OnClickListener
    private val cancelClickListener: View.OnClickListener?
    private var touchedView: View? = null
    private val handlerRunnable: Runnable = object : Runnable {
        override fun run() {
            if (touchedView!!.isEnabled) {
                handler.postDelayed(this, normalInterval.toLong())
                clickListener!!.onClick(touchedView)
            } else {
                // if the view was disabled by the clickListener, remove the callback
                handler.removeCallbacks(this)
                touchedView!!.isPressed = false
                touchedView = null
            }
        }
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                handler.removeCallbacks(handlerRunnable)
                handler.postDelayed(handlerRunnable, initialInterval.toLong())
                touchedView = view
                touchedView!!.isPressed = true
                clickListener.onClick(view)
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                handler.removeCallbacks(handlerRunnable)
                touchedView!!.isPressed = false
                touchedView = null
                cancelClickListener!!.onClick(view)
                return true
            }
        }
        return false
    }


    init {
        requireNotNull(clickListener) { "null runnable" }
        require(!(initialInterval < 0 || normalInterval < 0)) { "negative interval" }
        this.initialInterval = initialInterval
        this.normalInterval = normalInterval
        this.clickListener = clickListener
        this.cancelClickListener = cancelClickListener
    }
}