package com.zen.alchan.data.repository

import android.net.Uri
import com.zen.alchan.data.converter.convert
import com.zen.alchan.data.datasource.UserDataSource
import com.zen.alchan.data.entity.AppSetting
import com.zen.alchan.data.entity.MediaFilter
import com.zen.alchan.data.manager.UserManager
import com.zen.alchan.data.response.anilist.MediaListTypeOptions
import com.zen.alchan.data.response.anilist.NotificationOption
import com.zen.alchan.data.response.anilist.User
import com.zen.alchan.helper.enums.AppTheme
import com.zen.alchan.helper.enums.MediaType
import com.zen.alchan.helper.enums.Source
import com.zen.alchan.data.entity.ListStyle
import com.zen.alchan.data.response.anilist.PageInfo
import com.zen.alchan.helper.pojo.NullableItem
import com.zen.alchan.helper.pojo.SaveItem
import com.zen.alchan.helper.utils.NotInStorageException
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import type.ScoreFormat
import type.UserStaffNameLanguage
import type.UserStatisticsSort
import type.UserTitleLanguage

class DefaultUserRepository(
    private val userDataSource: UserDataSource,
    private val userManager: UserManager
) : UserRepository {

    private val _refreshMainScreenTrigger = PublishSubject.create<Unit>()
    override val refreshMainScreenTrigger: Observable<Unit>
        get() = _refreshMainScreenTrigger

    override fun getIsLoggedInAsGuest(): Observable<Boolean> {
        return Observable.just(userManager.isLoggedInAsGuest)
    }

    override fun getIsAuthenticated(): Observable<Boolean> {
        return Observable.just(userManager.isAuthenticated)
    }

    override fun getViewer(source: Source?, sort: List<UserStatisticsSort>): Observable<User> {
        when (source) {
            Source.CACHE -> {
                return Observable.create { emitter ->
                    val savedViewer = userManager.viewerData?.data
                    if (savedViewer != null) {
                        emitter.onNext(savedViewer)
                        emitter.onComplete()
                    } else {
                        emitter.onError(NotInStorageException())
                    }
                }
            }
            else -> {
                return userDataSource.getViewerQuery(sort).map {
                    val newViewer = it.data?.convert()
                    if (newViewer != null) {
                        userManager.viewerData = SaveItem(newViewer)
                    }
                    newViewer ?: throw Throwable()
                }.onErrorReturn {
                    val savedViewer = userManager.viewerData?.data
                    savedViewer ?: throw NotInStorageException()
                }
            }
        }
    }

    override fun loginAsGuest() {
        userManager.isLoggedInAsGuest = true
    }

    override fun logoutAsGuest() {
        userManager.isLoggedInAsGuest = false
    }

    override fun logout() {
        userManager.bearerToken = null
        userManager.viewerData = null
        userManager.followingCount = null
        userManager.followersCount = null
    }

    override fun saveBearerToken(newBearerToken: String?) {
        userManager.bearerToken = newBearerToken
    }

    override fun getFollowingAndFollowersCount(userId: Int, source: Source?): Observable<Pair<Int, Int>> {
        when (source) {
            Source.CACHE -> {
                return Observable.create { emitter ->
                    val savedFollowingCount = userManager.followingCount ?: 0
                    val savedFollowersCount = userManager.followersCount ?: 0
                    emitter.onNext(savedFollowingCount to savedFollowersCount)
                    emitter.onComplete()
                }
            }
            else -> {
                return userDataSource.getFollowingAndFollowersCount(userId).map {
                    val newFollowingCount = it.data?.following?.pageInfo?.total ?: 0
                    val newFollowersCount = it.data?.followers?.pageInfo?.total ?: 0
                    userManager.followingCount = newFollowingCount
                    userManager.followersCount = newFollowersCount
                    newFollowingCount to newFollowersCount
                }.onErrorReturn {
                    val savedFollowingCount = userManager.followingCount ?: 0
                    val savedFollowersCount = userManager.followersCount ?: 0
                    savedFollowingCount to savedFollowersCount
                }
            }
        }
    }

    override fun getFollowing(userId: Int, page: Int): Observable<Pair<PageInfo, List<User>>> {
        return userDataSource.getFollowing(userId, page).map {
            it.data?.convert()
        }
    }

    override fun getFollowers(userId: Int, page: Int): Observable<Pair<PageInfo, List<User>>> {
        return userDataSource.getFollowers(userId, page).map {
            it.data?.convert()
        }
    }

    override fun getListStyle(mediaType: MediaType): Observable<ListStyle> {
        return Observable.just(
            when (mediaType) {
                MediaType.ANIME -> userManager.animeListStyle
                MediaType.MANGA -> userManager.mangaListStyle
            }
        )
    }

    override fun setListStyle(mediaType: MediaType, newListStyle: ListStyle) {
        when (mediaType) {
            MediaType.ANIME -> userManager.animeListStyle = newListStyle
            MediaType.MANGA -> userManager.mangaListStyle = newListStyle
        }
    }

    override fun getListBackground(mediaType: MediaType): Observable<NullableItem<Uri>> {
        return when (mediaType) {
            MediaType.ANIME -> {
                if (userManager.animeListStyle.useBackgroundImage) {
                    userManager.animeListBackground
                } else {
                    Observable.just(NullableItem(null))
                }
            }
            MediaType.MANGA -> {
                if (userManager.mangaListStyle.useBackgroundImage) {
                    userManager.mangaListBackground
                } else {
                    Observable.just(NullableItem(null))
                }
            }
        }
    }

    override fun setListBackground(mediaType: MediaType, newUri: Uri?): Observable<Unit> {
        return when (mediaType) {
            MediaType.ANIME -> userManager.saveAnimeListBackground(newUri)
            MediaType.MANGA -> userManager.saveMangaListBackground(newUri)
        }
    }

    override fun getMediaFilter(mediaType: MediaType): Observable<MediaFilter> {
        return Observable.just(
            when (mediaType) {
                MediaType.ANIME -> userManager.animeFilter
                MediaType.MANGA -> userManager.mangaFilter
            }
        )
    }

    override fun setMediaFilter(mediaType: MediaType, newMediaFilter: MediaFilter) {
        when (mediaType) {
            MediaType.ANIME -> userManager.animeFilter = newMediaFilter
            MediaType.MANGA -> userManager.mangaFilter = newMediaFilter
        }
    }

    override fun getAppSetting(): Observable<AppSetting> {
        return Observable.just(userManager.appSetting)
    }

    override fun setAppSetting(newAppSetting: AppSetting?): Observable<Unit> {
        return Observable.create {
            try {
                userManager.appSetting = newAppSetting ?: AppSetting()
                it.onNext(Unit)
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

    override fun getAppTheme(): AppTheme {
        return userManager.appSetting.appTheme
    }

    override fun updateAniListSettings(
        titleLanguage: UserTitleLanguage,
        staffNameLanguage: UserStaffNameLanguage,
        activityMergeTime: Int,
        displayAdultContent: Boolean,
        airingNotifications: Boolean
    ): Observable<User> {
        return userDataSource.updateAniListSettings(
            titleLanguage,
            staffNameLanguage,
            activityMergeTime,
            displayAdultContent,
            airingNotifications
        )
            .toObservable()
            .doFinally {
                _refreshMainScreenTrigger.onNext(Unit)
            }
            .map {
                val newViewer = it.data?.convert()
                if (newViewer != null) {
                    userManager.viewerData = SaveItem(newViewer)
                }
                newViewer
            }
    }

    override fun updateListSettings(
        scoreFormat: ScoreFormat,
        rowOrder: String,
        animeListOptions: MediaListTypeOptions,
        mangaListOptions: MediaListTypeOptions
    ): Observable<User> {
        return userDataSource.updateListSettings(
            scoreFormat, rowOrder, animeListOptions, mangaListOptions
        )
            .toObservable()
            .doFinally {
                _refreshMainScreenTrigger.onNext(Unit)
            }
            .map {
                val newViewer = it.data?.convert()
                if (newViewer != null) {
                    userManager.viewerData = SaveItem(newViewer)
                }
                newViewer
            }
    }

    override fun updateNotificationsSettings(notificationOptions: List<NotificationOption>): Observable<User> {
        return userDataSource.updateNotificationsSettings(notificationOptions)
            .toObservable()
            .map {
                val newViewer = it.data?.convert()
                if (newViewer != null) {
                    userManager.viewerData = SaveItem(newViewer)
                }
                newViewer
            }
    }
}