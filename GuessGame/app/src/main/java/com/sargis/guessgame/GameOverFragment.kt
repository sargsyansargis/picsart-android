package com.sargis.guessgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.sargis.guessgame.databinding.FragmentGameOverBinding

class GameOverFragment : Fragment() {
    private val args: GameOverFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGameOverBinding.inflate(inflater, container, false)
        binding.name = args.name + " " + args.surname
        binding.startAgainButton.setOnClickListener {
            val action = GameOverFragmentDirections.actionGameOverFragmentToGameFragment(
                args.name,
                args.surname
            )
            it.findNavController().navigate(action)
        }
        return binding.root
    }
}