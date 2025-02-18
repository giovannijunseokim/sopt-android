package org.sopt.official.designsystem

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import org.sopt.official.R
import org.sopt.official.databinding.LayoutDialogNegativePositiveBinding
import org.sopt.official.feature.attendance.util.dpToPx
import org.sopt.official.stamp.util.toPx

class AlertDialogPositiveNegative(context: Context) : ConstraintLayout(context) {
    private val builder: AlertDialog.Builder = AlertDialog.Builder(context)

    val binding = LayoutDialogNegativePositiveBinding.inflate(LayoutInflater.from(context))

    private var dialog: AlertDialog? = null

    init {
        dialog = builder.setView(binding.root).create()
    }

    fun setTitle(@StringRes titleId: Int): AlertDialogPositiveNegative {
        binding.title.text = context.getText(titleId)
        return this
    }

    fun setTitle(title: CharSequence): AlertDialogPositiveNegative {
        binding.title.text = title
        return this
    }

    fun setSubtitle(@StringRes subtitleId: Int): AlertDialogPositiveNegative {
        binding.subtitle.text = context.getText(subtitleId)
        return this
    }

    fun setSubtitle(subtitle: CharSequence): AlertDialogPositiveNegative {
        binding.subtitle.text = subtitle
        return this
    }

    fun setPositiveButton(@StringRes textId: Int, listener: (view: View) -> (Unit) = { dismiss() }): AlertDialogPositiveNegative {
        binding.positiveButton.apply {
            text = context.getText(textId)
            setOnClickListener(listener)
        }
        return this
    }

    fun setPositiveButton(text: CharSequence, listener: (view: View) -> (Unit) = { dismiss() }): AlertDialogPositiveNegative {
        binding.positiveButton.apply {
            this.text = text
            setOnClickListener(listener)
        }
        return this
    }

    fun setNegativeButton(@StringRes textId: Int, listener: (view: View) -> (Unit) = { dismiss() }): AlertDialogPositiveNegative {
        binding.negativeButton.apply {
            text = context.getText(textId)
            this.text = text
            setOnClickListener(listener)
        }
        return this
    }

    fun setNegativeButton(text: CharSequence, listener: (view: View) -> (Unit) = { dismiss() }): AlertDialogPositiveNegative {
        binding.negativeButton.apply {
            this.text = text
            setOnClickListener(listener)
        }
        return this
    }

    fun show() {
        dialog?.window?.setBackgroundDrawable(context.getDrawable(R.drawable.rectangle_radius_10).apply {
            this?.setTint(context.getColor(R.color.black_60))
        })

        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}