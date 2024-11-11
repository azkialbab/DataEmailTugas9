package com.example.dataemail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dataemail.databinding.FragmentHomeBinding
import com.example.dataemail.model.DataNew
import com.example.dataemail.model.Users
import com.example.dataemail.network.ApiClient
import com.example.dataemail.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UserAdapter
    private val userList = mutableListOf<DataNew>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = UserAdapter(userList, requireActivity()) { user ->
            openUserDetail(user)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter


        loadUserData()
    }

    private fun loadUserData() {
        val client: ApiService = ApiClient.getInstance()
        val response = client.getAllUsers()

        response.enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful && response.body() != null) {
                    userList.clear()
                    userList.addAll(response.body()!!.data)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Data not found", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Toast.makeText(requireContext(), "Connection error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun openUserDetail(user: DataNew) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("USER_ID", user.id)
        intent.putExtra("USER_NAME", user.fullName)
        intent.putExtra("USER_EMAIL", user.email)
        intent.putExtra("USER_AVATAR", user.avatar)
        startActivity(intent)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
