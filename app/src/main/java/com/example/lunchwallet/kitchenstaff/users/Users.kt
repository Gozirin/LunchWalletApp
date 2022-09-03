package com.example.lunchwallet.kitchenstaff.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lunchwallet.R
import com.example.lunchwallet.databinding.FragmentUsersBinding


class Users : Fragment() {
    private lateinit var binding: FragmentUsersBinding
    private lateinit var userAdapter: UsersAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUsersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userAdapter = UsersAdapter(ExistingUsers.userList)
        binding.usersRv.adapter = userAdapter
        binding.usersRv.layoutManager = LinearLayoutManager(requireContext())
    }


}