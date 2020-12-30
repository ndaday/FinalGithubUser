package com.daday.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.daday.consumerapp.R
import com.daday.consumerapp.databinding.ItemUsersBinding
import com.daday.consumerapp.entity.UserItems

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val mData = ArrayList<UserItems>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<UserItems>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): UserViewHolder {
        val mView =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_users, viewGroup, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(userViewHolder: UserViewHolder, position: Int) {
        userViewHolder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUsersBinding.bind(itemView)
        fun bind(userItems: UserItems) {
            with(itemView) {
                Glide.with(context)
                    .load(userItems.avatar)
                    .apply(RequestOptions().override(55, 55))
                    .into(binding.imgAvatar)
                binding.tvItemUsername.text = userItems.username
                binding.tvUrlUser.text = userItems.url

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(userItems) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserItems) {
        }
    }
}