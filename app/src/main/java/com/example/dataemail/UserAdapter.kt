package com.example.dataemail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dataemail.databinding.ItemUserBinding
import com.example.dataemail.model.DataNew
import com.squareup.picasso.Picasso

class UserAdapter(
    private val userList: List<DataNew>,
    private val context: Context,
    private val itemClick: (DataNew) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: DataNew) {

            binding.nameTextView.text = user.fullName
            binding.emailTextView.text = user.email

            Picasso.get()
                .load(user.avatar)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(binding.avatarImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)


        holder.itemView.setOnClickListener {
            itemClick(user)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
