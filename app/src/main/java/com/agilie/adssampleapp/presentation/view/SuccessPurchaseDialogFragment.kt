package com.agilie.adssampleapp.presentation.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.agilie.adssampleapp.R
import java.util.concurrent.atomic.AtomicBoolean

class SuccessPurchaseDialogFragment : DialogFragment() {

    private var isActionBlocked = AtomicBoolean(false)
    private val userActionHandler = Handler(Looper.getMainLooper())
    private val userActionDelay = 150L

    private val btnClickListener = View.OnClickListener {
        if (!isActionBlocked.get()) {
            isActionBlocked.set(true)
            userActionHandler.postDelayed(getRunnable { hide() }, userActionDelay)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fragment_success_purchase, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<AppCompatButton>(R.id.btnOk).setOnClickListener(btnClickListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userActionHandler.removeCallbacksAndMessages(null)
    }

    private fun getRunnable(action: () -> Unit): Runnable {
        return Runnable {
            action()
            isActionBlocked.set(false)
        }
    }

    private fun hide() {
        dismiss()
    }
}