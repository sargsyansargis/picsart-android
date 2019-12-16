package com.sargis.guessgame

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.sargis.guessgame.databinding.FragmentGameWinBinding

class GameWinFragment : Fragment() {

    val args: GameWinFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGameWinBinding.inflate(inflater, container, false)
        binding.name = args.name + " " + args.surname
        binding.winNumber = args.winNumber.toString()
        binding.startAgainButton.setOnClickListener {
            val action = GameWinFragmentDirections.actionGameWinFragmentToGameFragment(
                args.name,
                args.surname
            )
            it.findNavController().navigate(action)
        }

        binding.shareButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, args.name + " " + args.surname + " " + args.winNumber)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        return binding.root
    }
}