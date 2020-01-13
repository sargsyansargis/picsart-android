package com.sargis.guardiannews


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sargis.guardiannews.databinding.FragmentArticleDetailBinding

/**
 * A simple [Fragment] subclass.
 */
class ArticleDetailFragment : Fragment() {
    private val args: ArticleDetailFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentArticleDetailBinding.inflate(inflater, container, false)

        val model = args.model
        if (model != null) {
            Glide.with(this)
                .load(model.fields?.thumbnail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(binding.articleImage)
            binding.articleCategory.text = model.sectionName.toString()
            binding.articleTitle.text = model.webTitle.toString()
        }




        return binding.root
    }


}
