package com.sargis.guardiannews

import android.util.Log
import com.sargis.guardiannews.guadriandsapi.ArticleModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ArticleListAdapter :
    PagedListAdapter<ArticleModel, ArticlesListAdapterViewHolder>(REPO_COMPARATOR) {

    lateinit var articleListListener: ArticleListListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticlesListAdapterViewHolder {
        if (viewType == 0) {
            return ArticlesListAdapterViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.article_list_row,
                    parent,
                    false
                )
            )
        } else {
            return ArticlesListAdapterViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.liveblog_list_row,
                    parent,
                    false
                )
            )}
        }

        override fun onBindViewHolder(holder: ArticlesListAdapterViewHolder, position: Int) {
            holder.bind(getItem(position)!!, articleListListener)
        }

    override fun getItemViewType(position: Int): Int {
        return if(getItem(position)!!.type == "article"){
            0
        }else{
            1
        }
    }
        companion object {
            private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<ArticleModel>() {
                override fun areItemsTheSame(
                    oldItem: ArticleModel,
                    newItem: ArticleModel
                ): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: ArticleModel,
                    newItem: ArticleModel
                ): Boolean =
                    oldItem.id == newItem.id
            }
        }
    }

    class ArticlesListAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleText = itemView.findViewById<TextView>(R.id.article_title)
        private val categoryText = itemView.findViewById<TextView>(R.id.article_category)
        private val image = itemView.findViewById<ImageView>(R.id.article_image)
        private val likeButton = itemView.findViewById<Button>(R.id.like_button)
        private val saveButton = itemView.findViewById<Button>(R.id.save_button)
        private val deleteButton = itemView.findViewById<Button>(R.id.delete_button)

        fun bind(articleModel: ArticleModel, listener: ArticleListListener) {

            itemView.visibility = if (articleModel.deleted) {
                itemView.layoutParams.width = 0
                itemView.layoutParams.height = 0
                View.GONE
            } else View.VISIBLE


            image.setOnClickListener{
                it.transitionName = articleModel.id
                val extras = FragmentNavigatorExtras(
                    image to articleModel.id
                )

                it?.findNavController()?.navigate(
                    ArticlesListFragmentDirections.actionArticlesListFragmentToArticleDetailFragment(articleModel),extras)

            }
            likeButton.text = if (articleModel.liked) "Liked" else "Like"
            likeButton.setOnClickListener {
                articleModel.liked = !articleModel.liked
                likeButton.text = if (articleModel.liked) "Liked" else "Like"
                listener.onItemChanged(articleModel)
            }

            saveButton.text = if (articleModel.saved) "Saved" else "Save"
            saveButton.setOnClickListener {
                articleModel.saved = !articleModel.saved
                saveButton.text = if (articleModel.saved) "Saved" else "Save"
                listener.onItemChanged(articleModel)
                listener.onItemSaved(articleModel)
            }

            deleteButton.setOnClickListener {
                itemView.layoutParams.width = 0
                itemView.layoutParams.height = 0
                View.GONE
                articleModel.deleted = true
                listener.onItemDeleted(articleModel)
            }

            Glide.with(itemView.context)
                .load(articleModel.fields?.thumbnail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(image)
            titleText.text = articleModel.webTitle
            categoryText.text = articleModel.sectionName
        }
    }


    interface ArticleListListener {
        fun onItemChanged(model: ArticleModel)

        fun onItemDeleted(model: ArticleModel)

        fun onItemSaved(model: ArticleModel)

    }

