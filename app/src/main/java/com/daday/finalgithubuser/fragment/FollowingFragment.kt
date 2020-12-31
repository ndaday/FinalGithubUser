package com.daday.finalgithubuser.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.daday.finalgithubuser.adapter.UserAdapter
import com.daday.finalgithubuser.databinding.FragmentFollowingBinding
import com.daday.finalgithubuser.viewmodel.MainViewModel

class FollowingFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var binding: FragmentFollowingBinding

    companion object {
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String?): FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        showRecyclerview()
    }

    private fun showRecyclerview() {
        val username = arguments?.getString(ARG_USERNAME)
        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowing.adapter = adapter
        showLoading(true)
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java
        )
        username?.let { mainViewModel.setUserFollowing(it) }
        mainViewModel.getUsersFollowing().observe(this, { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBarFollowing.visibility = View.VISIBLE
        } else {
            binding.progressBarFollowing.visibility = View.GONE
        }
    }
}