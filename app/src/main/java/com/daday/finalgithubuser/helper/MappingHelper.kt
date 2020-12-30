package com.daday.finalgithubuser.helper

import android.database.Cursor
import com.daday.finalgithubuser.db.DatabaseContract
import com.daday.finalgithubuser.entity.UserItems

object MappingHelper {
    fun mapCursorToArrayList(favCursor: Cursor?): ArrayList<UserItems> {
        val favList = ArrayList<UserItems>()

        favCursor?.apply {
            while (moveToNext()) {
                val username =
                    getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COL_USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COL_NAME))
                val avatar =
                    getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COL_AVATAR))
                favList.add(UserItems(username, name, avatar))
            }
        }
        return favList
    }
}