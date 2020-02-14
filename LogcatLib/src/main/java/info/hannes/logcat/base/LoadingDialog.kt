package info.hannes.logcat.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import info.hannes.logcat.R

class LoadingDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.loading_dialog, container, false)

        arguments?.let {
            val tv = view.findViewById<TextView>(R.id.loadingText)
            val messageId = it.getInt(MESSAGE_ID, R.string.placeholder)
            tv.setText(messageId)
        }

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        /// set cancellation behavior
        var cancelable = false
        arguments?.let {
            cancelable = it.getBoolean(CANCELABLE, false)
        }
        dialog.setCancelable(cancelable)
        if (!cancelable) {
            // disable the back button
            val keyListener = DialogInterface.OnKeyListener { _, keyCode, _ -> keyCode == KeyEvent.KEYCODE_BACK }
            dialog.setOnKeyListener(keyListener)
        }
        return dialog
    }

    override fun onDestroyView() {
        if (this.dialog != null && retainInstance) {
            dialog!!.setDismissMessage(null)
        }
        super.onDestroyView()
    }

    companion object {

        private const val MESSAGE_ID = "MESSAGE_ID"
        private const val CANCELABLE = "CANCELABLE"

        internal fun newInstance(messageId: Int, cancelable: Boolean): LoadingDialog {
            val fragment = LoadingDialog()
            val args = Bundle()
            args.putInt(MESSAGE_ID, messageId)
            args.putBoolean(CANCELABLE, cancelable)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(cancelable: Boolean): LoadingDialog {
            val fragment = LoadingDialog()
            val args = Bundle()
            args.putBoolean(CANCELABLE, cancelable)
            fragment.arguments = args
            return fragment
        }
    }
}
