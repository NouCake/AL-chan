package com.zen.alchan.data.localstorage

import android.content.Context
import com.google.gson.Gson
import com.zen.alchan.data.response.HomeData
import com.zen.alchan.data.response.ProfileData
import com.zen.alchan.data.response.anilist.User
import com.zen.alchan.helper.pojo.SaveItem

class DefaultJsonStorageHandler(
    context: Context,
    private val gson: Gson
) : JsonStorageHandler, BaseJsonStorageHandler(context) {

    override var homeData: SaveItem<HomeData>?
        get() = gson.fromJson(getData(HOME_DATA), getType<SaveItem<HomeData>>())
        set(value) { setData(HOME_DATA, gson.toJson(value)) }

    override var viewerData: SaveItem<User>?
        get() = gson.fromJson(getData(VIEWER_DATA), getType<SaveItem<User>>())
        set(value) { setData(VIEWER_DATA, gson.toJson(value)) }

    override var profileData: SaveItem<ProfileData>?
        get() = gson.fromJson(getData(PROFILE_DATA), getType<SaveItem<ProfileData>>())
        set(value) { setData(PROFILE_DATA, gson.toJson(value)) }

    companion object {
        private const val HOME_DATA = "home_data.json"
        private const val VIEWER_DATA = "viewer_data.json"
        private const val PROFILE_DATA = "profile_data.json"
    }
}