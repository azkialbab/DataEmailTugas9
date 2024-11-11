package com.example.dataemail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.dataemail.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)

        prefManager = PrefManager(requireContext())

        val username = prefManager.getLoggedInUsername()

        if (username != null) {
            lifecycleScope.launch {
                val user = prefManager.getUser(username)

                if (user != null) {
                    binding.usernameTextView.text = "Username: ${user.username}"
                    binding.emailTextView.text = "Email: ${user.email}"
                    binding.phoneTextView.text = "Phone: ${user.phone}"
                    Picasso.get()
                        .load(user.profilePhotoUrl)
                        .placeholder(R.drawable.sample)
                        .into(binding.profilePhoto)
                } else {
                    binding.usernameTextView.text = "No user data found"
                    binding.emailTextView.text = ""
                    binding.profilePhoto.setImageResource(R.drawable.sample)
                }
            }
        } else {
            binding.usernameTextView.text = "No user logged in"
            binding.emailTextView.text = ""
            binding.profilePhoto.setImageResource(R.drawable.sample)
        }

        binding.logoutButton.setOnClickListener {
            prefManager.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return binding.root
    }
}
