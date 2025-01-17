package com.waytruck.cargo.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.waytruck.cargo.R

class PurchaseConfirmationDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setMessage(getString(R.string.order_confirmation))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                goToUpdate()
            }
            .create()

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }

    fun goToUpdate() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("market://details?id=com.waytruck.cargo")
        //  intent.data = Uri.parse(preferencesController.getLinkUpdateApp())
        startActivity(intent)

    }
}