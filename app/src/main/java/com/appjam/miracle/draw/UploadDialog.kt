package com.appjam.miracle.draw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.appjam.miracle.databinding.DialogUploadBinding

class UploadDialog(
    private val uploadDialogInterface: UploadDialogInterface
): DialogFragment() {

    private lateinit var binding: DialogUploadBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogUploadBinding.inflate(layoutInflater, container, false)


        binding.textNo.setOnClickListener {
            dialog?.cancel()
        }
        binding.textYes.setOnClickListener {
            uploadDialogInterface.onYesButtonClick()
        }
        return binding.root
    }
}

interface UploadDialogInterface {
    fun onYesButtonClick()
}