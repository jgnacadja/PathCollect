package org.odk.collect.android.button

import android.R
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.Checkable
import com.google.android.material.button.MaterialButton
import org.odk.collect.androidshared.ui.multiclicksafe.MultiClickGuard
import timber.log.Timber

class MaterialButtonCheckable : MaterialButton, Checkable {
    private var mChecked = false
    private var mOnCheckedChangeListener: OnCheckedChangeListener? = null
    private var mBroadcasting = false

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun isChecked(): Boolean {
        return mChecked
    }

    override fun performClick(): Boolean {
        toggle()
        return MultiClickGuard.allowClick() && super.performClick()
    }

    override fun toggle() {
        isChecked = !mChecked
    }

    public override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked) {
            mergeDrawableStates(drawableState, checkedStateSet)
        }
        return drawableState
    }

    override fun setChecked(checked: Boolean) {
        Timber.tag("MaterialButtonCheckable").e("Toogle Button clicked %s", checked)
        if (mChecked != checked) {
            Timber.tag("MaterialButtonCheckable").e("Checked state %s", checked)
            mChecked = checked
            refreshDrawableState()

            // Avoid infinite recursions if setChecked() is called from a listener
            if (mBroadcasting) {
                return
            }
            mBroadcasting = true
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener!!.onCheckedChanged(this, mChecked)
            }
            mBroadcasting = false
        }
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener the callback to call on checked state change
     */
    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener?) {
        mOnCheckedChangeListener = listener
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changed.
     */
    interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        fun onCheckedChanged(buttonView: MaterialButtonCheckable?, isChecked: Boolean)
    }

    internal class SavedState : BaseSavedState {
        var checked = false

        constructor(superState: Parcelable?) : super(superState) {}

        /**
         * Constructor called from [.CREATOR]
         */
        private constructor(`in`: Parcel) : super(`in`) {
            checked = (`in`.readValue(null) as Boolean?)!!
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeValue(checked)
        }

        override fun toString(): String {
            return "MaterialButtonCheckable.SavedState{" + Integer.toHexString(
                System.identityHashCode(
                    this
                )
            ) + " checked=" + checked + "}"
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState?> =
                object : Parcelable.Creator<SavedState?> {
                    override fun createFromParcel(`in`: Parcel): SavedState? {
                        return SavedState(`in`)
                    }

                    override fun newArray(size: Int): Array<SavedState?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.checked = isChecked
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val ss = state as SavedState?
        super.onRestoreInstanceState(ss!!.superState)
        isChecked = ss.checked
        requestLayout()
    }

    companion object {
        private val checkedStateSet = intArrayOf(R.attr.state_checked)
    }
}