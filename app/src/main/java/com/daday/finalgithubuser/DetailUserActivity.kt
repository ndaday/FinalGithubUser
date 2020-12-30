package com.daday.finalgithubuser

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.daday.finalgithubuser.adapter.SectionsPagerAdapter
import com.daday.finalgithubuser.databinding.ActivityDetailUserBinding
import com.daday.finalgithubuser.db.DatabaseContract.UserColumns.Companion.COL_AVATAR
import com.daday.finalgithubuser.db.DatabaseContract.UserColumns.Companion.COL_NAME
import com.daday.finalgithubuser.db.DatabaseContract.UserColumns.Companion.COL_USERNAME
import com.daday.finalgithubuser.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.daday.finalgithubuser.db.FavoriteHelper
import com.daday.finalgithubuser.entity.UserItems
import com.daday.finalgithubuser.helper.MappingHelper
import com.daday.finalgithubuser.viewmodel.MainViewModel

class DetailUserActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var favoriteHelper: FavoriteHelper
    private lateinit var imgAvatar: String
    private lateinit var uriWithId: Uri
    private var fav: UserItems? = null
    private var isFavorite = false

    companion object {
        const val ARG_USERNAME = "username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        favoriteCheck()

        binding.fabFavorite.setOnClickListener(this)
        showDetail()
        setViewPager()
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + fav?.username.toString())
        supportActionBar?.elevation = 0f
    }

    private fun setViewPager() {
        val user = intent.getParcelableExtra<UserItems>(ARG_USERNAME) as UserItems
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = user.username
        binding.vPager.adapter = sectionsPagerAdapter
        binding.tabs.setupWithViewPager(binding.vPager)
    }

    private fun showDetail() {
        val user = intent.getParcelableExtra<UserItems>(ARG_USERNAME) as UserItems
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)
        user.username?.let { mainViewModel.setDetail(it) }
        mainViewModel.getDetail().observe(this, { userDetail ->
            if (userDetail != null) {
                binding.tvUname.text = userDetail.username
                binding.tvName.text = userDetail.name
                binding.tvCompany.text = userDetail.company
                binding.tvLocation.text = userDetail.location
                binding.tvFollowers.text = userDetail.followers
                binding.tvFollowings.text = userDetail.following
                Glide.with(this).load(userDetail.avatar).into(binding.avatars)
                imgAvatar = userDetail.avatar.toString()
            }
        })
    }

    private fun setDataFavorite() {
        val favoriteUser = intent.getParcelableExtra<UserItems>(ARG_USERNAME) as UserItems
        binding.tvUname.text = favoriteUser.username
        binding.tvName.text = favoriteUser.name
        binding.tvCompany.text = favoriteUser.company
        binding.tvLocation.text = favoriteUser.location
        binding.tvFollowers.text = favoriteUser.followers
        binding.tvFollowings.text = favoriteUser.following
        Glide.with(this).load(favoriteUser.avatar).into(binding.avatars)
        imgAvatar = favoriteUser.avatar.toString()
    }

    override fun onClick(view: View) {
        setStatusFavorite(isFavorite)
        if (view.id == R.id.fab_favorite) {
            if (isFavorite) {
                favoriteHelper.deleteById(fav?.username.toString())
                Toast.makeText(this, getString(R.string.deletefavorite), Toast.LENGTH_SHORT).show()
                isFavorite = false
                setStatusFavorite(isFavorite)
            } else {
                val username = binding.tvUname.text.toString()
                val name = binding.tvName.text.toString()
                val avatar = imgAvatar
                val values = ContentValues()
                values.put(COL_USERNAME, username)
                values.put(COL_NAME, name)
                values.put(COL_AVATAR, avatar)
                contentResolver.insert(CONTENT_URI, values)
                Toast.makeText(this, getString(R.string.addfavorite), Toast.LENGTH_SHORT).show()
                isFavorite = true
                setStatusFavorite(isFavorite)
            }
        }
    }

    private fun favoriteCheck() {
        fav = intent.getParcelableExtra(ARG_USERNAME)
        val cursor = favoriteHelper.queryById(fav?.username.toString())
        val favourite = MappingHelper.mapCursorToArrayList(cursor)
        for (data in favourite) {
            if (fav?.username == data.username) {
                isFavorite = true
                setStatusFavorite(isFavorite)
                setDataFavorite()
            } else {
                showDetail()
            }
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
        }
    }
}