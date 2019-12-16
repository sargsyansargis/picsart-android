package com.sargis.guessgame

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.sargis.guessgame.databinding.FragmentGameBinding
import java.util.*

class GameFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args: GameFragmentArgs by navArgs()
        val binding: FragmentGameBinding = FragmentGameBinding.inflate(inflater, container, false)
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.isNotEmpty() == true) {
                    binding.button.isEnabled = true
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.button.setOnClickListener {
            val number = binding.editText.text.toString().toInt()
            val genNumber = Random().nextInt()
            val action = if (number == genNumber) {
                GameFragmentDirections.actionGameFragmentToGameWinFragment(
                    args.name,
                    args.surname,
                    number
                )
            } else {
                GameFragmentDirections.actionGameFragmentToGameOverFragment(
                    args.name,
                    args.surname
                )

            }
            it.findNavController().navigate(action)
        }


        return binding.root
    }
}
