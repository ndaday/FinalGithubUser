package com.daday.finalgithubuser.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.daday.finalgithubuser.DetailUserActivity
import com.daday.finalgithubuser.R
import com.daday.finalgithubuser.databinding.ItemUsersBinding
import com.daday.finalgithubuser.entity.UserItems
import com.daday.finalgithubuser.utils.CustomOnItemClickListener

class FavoriteAdapter(private val activity: Activity) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    var listFavorite = ArrayList<UserItems>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_users, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int = this.listFavorite.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUsersBinding.bind(itemView)
        fun bind(favorite: UserItems) {
            with(itemView) {
                binding.tvItemUsername.text = favorite.username
                binding.tvUrlUser.text = favorite.name
                Glide.with(context)
                    .load(favorite.avatar)
                    .into(binding.imgAvatar)
                itemView.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(activity, DetailUserActivity::class.java)
                                intent.putExtra(DetailUserActivity.ARG_USERNAME, favorite)
                                activity.startActivity(intent)
                            }
                        })
                )
            }
        }
    }
}