package com.zen.alchan.ui.base

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.zen.alchan.data.entity.ListStyle
import com.zen.alchan.data.entity.MediaFilter
import com.zen.alchan.data.response.anilist.Activity
import com.zen.alchan.helper.enums.ActivityListPage
import com.zen.alchan.helper.enums.Favorite
import com.zen.alchan.helper.enums.MediaType
import com.zen.alchan.helper.enums.SearchCategory
import com.zen.alchan.helper.utils.DeepLink
import type.ScoreFormat


interface NavigationManager {

    fun navigateToSplash(deepLink: DeepLink? = null, bypassSplash: Boolean = false)
    fun navigateToLanding()
    fun navigateToLogin(bearerToken: String? = null, disableAnimation: Boolean = false)
    fun navigateToMain(deepLink: DeepLink? = null)

    fun navigateToSearch()
    fun navigateToExplore(searchCategory: SearchCategory)
    fun navigateToSocial()
    fun navigateToActivityDetail(id: Int, action: (activity: Activity, isDeleted: Boolean) -> Unit)
    fun navigateToActivityList(activityListPage: ActivityListPage, id: Int? = null)

    fun navigateToSettings()
    fun navigateToAppSettings()
    fun navigateToAniListSettings()
    fun navigateToListSettings()
    fun navigateToNotificationsSettings()
    fun navigateToAccountSettings()
    fun navigateToAbout()

    fun navigateToReorder(itemList: List<String>, action: (reorderResult: List<String>) -> Unit)
    fun navigateToFilter(
        mediaFilter: MediaFilter?,
        mediaType: MediaType,
        scoreFormat: ScoreFormat,
        isUserList: Boolean,
        isCurrentUser: Boolean,
        action: (filterResult: MediaFilter) -> Unit
    )
    fun navigateToCustomise(mediaType: MediaType, action: (customiseResult: ListStyle) -> Unit)

    fun navigateToEditor(mediaId: Int, fromMediaList: Boolean, action: (() -> Unit)? = null)

    fun navigateToMedia(id: Int)
    fun navigateToMediaCharacters(id: Int)
    fun navigateToMediaStaff(id: Int)
    fun navigateToCharacter(id: Int)
    fun navigateToCharacterMedia(id: Int)
    fun navigateToStaff(id: Int)
    fun navigateToStaffCharacter(id: Int)
    fun navigateToStaffMedia(id: Int)
    fun navigateToUser(id: Int? = null, username: String? = null)
    fun navigateToStudio(id: Int)
    fun navigateToStudioMedia(id: Int)

    fun navigateToAnimeMediaList(id: Int)
    fun navigateToMangaMediaList(id: Int)
    fun navigateToFollowing(id: Int)
    fun navigateToFollowers(id: Int)
    fun navigateToUserStats(id: Int)
    fun navigateToFavorite(id: Int, favorite: Favorite)

    fun openWebView(url: String)
    fun openWebView(url: Url, id: Int? = null)
    fun openEmailClient()
    fun openGallery(launcher: ActivityResultLauncher<Intent>)

    fun isAtPreLoginScreen(): Boolean
    fun isAtBrowseScreen(): Boolean
    fun popBrowseScreenPage()
    fun shouldPopFromBrowseScreen(): Boolean
    fun closeBrowseScreen()

    enum class Url {
        ANILIST_WEBSITE,
        ANILIST_LOGIN,
        ANILIST_REGISTER,
        ANILIST_PROFILE_SETTINGS,
        ANILIST_ACCOUNT_SETTINGS,
        ANILIST_LISTS_SETTINGS,
        ANILIST_IMPORT_LISTS,
        ANILIST_CONNECT_WITH_TWITTER,
        ANLIST_ACTIVITY,
        ALCHAN_FORUM_THREAD,
        ALCHAN_GITHUB,
        ALCHAN_PLAY_STORE,
        ALCHAN_TWITTER,
        ALCHAN_PRIVACY_POLICY
    }
}