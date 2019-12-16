package com.sargis.guessgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.sargis.guessgame.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        binding.nextButton.setOnClickListener {
            val action = RegistrationFragmentDirections.actionRegistrationFragmentToGameFragment(
                binding.nameText.text.toString(),
                binding.surNameText.text.toString()
            )

            it.findNavController().navigate(action)
        }

        return binding.root
    }
}
