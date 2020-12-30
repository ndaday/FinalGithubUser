package com.daday.finalgithubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.daday.finalgithubuser.BuildConfig
import com.daday.finalgithubuser.entity.UserItems
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<UserItems>>()
    private val detailUsers = MutableLiveData<UserItems>()
    val token = BuildConfig.GITHUB_TOKEN

    fun setUsers() {
        val listItems = ArrayList<UserItems>()

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users"

        client.addHeader("Authorization", token)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val user = jsonArray.getJSONObject(i)
                        val userItems = UserItems()
                        userItems.username = user.getString("login")
                        userItems.avatar = user.getString("avatar_url")
                        userItems.url = user.getString("url")
                        listItems.add(userItems)
                    }
                    listUsers.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                Log.d("Exception", error.message.toString())
            }
        })
    }

    fun setDetail(username: String?) {
        val users = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"

        users.addHeader("Authorization", token)
        users.addHeader("User-Agent", "request")
        users.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d("Detail", result)
                try {
                    val jsonObject = JSONObject(result)
                    val userItems = UserItems()
                    userItems.username = jsonObject.getString("login")
                    userItems.name = jsonObject.getString("name")
                    userItems.company = jsonObject.getString("company")
                    userItems.location = jsonObject.getString("location")
                    userItems.followers = jsonObject.getString("followers")
                    userItems.following = jsonObject.getString("following")
                    userItems.avatar = jsonObject.getString("avatar_url")
                    detailUsers.postValue(userItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message + " GIT"}"
                }
                Log.d("Exception", errorMessage)
            }
        })

    }

    fun setSearchUser(query: String) {
        val listUser = ArrayList<UserItems>()

        val users = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$query"

        users.addHeader("Authorization", token)
        users.addHeader("User-Agent", "request")
        users.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {

                val result = String(responseBody)
                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        val avatar = item.getString("avatar_url")
                        val giturl = item.getString("url")
                        val user = UserItems()
                        user.username = username
                        user.url = giturl
                        user.avatar = avatar
                        listUser.add(user)
                    }
                    listUsers.postValue(listUser)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                Log.d("Exception", error.message.toString())
            }
        })
    }

    fun setUserFollower(username: String?) {
        val listItems = ArrayList<UserItems>()
        val users = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/followers"

        users.addHeader("Authorization", token)
        users.addHeader("User-Agent", "request")
        users.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    Log.d("FollowerReq", result)
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val userFollower = jsonArray.getJSONObject(i)
                        val followers = UserItems()
                        followers.username = userFollower.getString("login")
                        followers.avatar = userFollower.getString("avatar_url")
                        listItems.add(followers)
                    }
                    listUsers.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message + " GIT"}"
                }
                Log.d("Exception", errorMessage)
            }
        })
    }

    fun setUserFollowing(username: String?) {
        val listItems = ArrayList<UserItems>()
        val users = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/following"

        users.addHeader("Authorization", token)
        users.addHeader("User-Agent", "request")
        users.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    Log.d("FollowingReq", result)
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val userFollowing = jsonArray.getJSONObject(i)
                        val following = UserItems()
                        following.username = userFollowing.getString("login")
                        following.avatar = userFollowing.getString("avatar_url")
                        listItems.add(following)
                    }
                    listUsers.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message + " GIT"}"
                }
                Log.d("Exception", errorMessage)
            }
        })
    }

    fun getUsers(): LiveData<ArrayList<UserItems>> = listUsers

    fun getDetail(): LiveData<UserItems> = detailUsers

    fun getUserFollower(): LiveData<ArrayList<UserItems>> = listUsers

    fun getUsersFollowing(): LiveData<ArrayList<UserItems>> = listUsers
}