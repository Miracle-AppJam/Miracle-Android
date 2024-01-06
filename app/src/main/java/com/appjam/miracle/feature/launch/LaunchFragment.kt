package com.appjam.miracle.feature.launch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.appjam.miracle.R
import com.appjam.miracle.databinding.FragmentLaunchBinding

class LaunchFragment: Fragment() {

    private lateinit var binding: FragmentLaunchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaunchBinding.inflate(inflater, container, false)

        binding.textStart.setOnClickListener {
            findNavController().navigate(R.id.action_launchFragment_to_drawFragment)
        }

        return binding.root
    }
}