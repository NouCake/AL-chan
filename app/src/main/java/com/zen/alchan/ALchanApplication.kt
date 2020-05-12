package com.zen.alchan

import android.app.Application
import com.google.gson.Gson
import com.zen.alchan.data.datasource.*
import com.zen.alchan.data.localstorage.*
import com.zen.alchan.data.network.ApolloHandler
import com.zen.alchan.data.network.HeaderInterceptor
import com.zen.alchan.data.network.HeaderInterceptorImpl
import com.zen.alchan.data.repository.*
import com.zen.alchan.helper.Constant
import com.zen.alchan.ui.main.MainViewModel
import com.zen.alchan.ui.animelist.AnimeListViewModel
import com.zen.alchan.ui.animelist.editor.AnimeListEditorViewModel
import com.zen.alchan.ui.auth.LoginViewModel
import com.zen.alchan.ui.base.BaseViewModel
import com.zen.alchan.ui.auth.SplashViewModel
import com.zen.alchan.ui.browse.character.CharacterViewModel
import com.zen.alchan.ui.common.customise.CustomiseListViewModel
import com.zen.alchan.ui.common.filter.MediaFilterViewModel
import com.zen.alchan.ui.home.HomeViewModel
import com.zen.alchan.ui.mangalist.MangaListViewModel
import com.zen.alchan.ui.mangalist.editor.MangaListEditorViewModel
import com.zen.alchan.ui.browse.media.MediaViewModel
import com.zen.alchan.ui.browse.media.characters.MediaCharactersViewModel
import com.zen.alchan.ui.browse.media.overview.MediaOverviewViewModel
import com.zen.alchan.ui.browse.media.staffs.MediaStaffsViewModel
import com.zen.alchan.ui.browse.staff.StaffViewModel
import com.zen.alchan.ui.browse.staff.anime.StaffAnimeViewModel
import com.zen.alchan.ui.browse.staff.bio.StaffBioViewModel
import com.zen.alchan.ui.browse.staff.manga.StaffMangaViewModel
import com.zen.alchan.ui.browse.staff.voice.StaffVoiceViewModel
import com.zen.alchan.ui.browse.studio.StudioViewModel
import com.zen.alchan.ui.explore.ExploreViewModel
import com.zen.alchan.ui.profile.ProfileViewModel
import com.zen.alchan.ui.profile.bio.BioViewModel
import com.zen.alchan.ui.search.SearchListViewModel
import com.zen.alchan.ui.search.SearchViewModel
import com.zen.alchan.ui.settings.anilist.AniListSettingsViewModel
import com.zen.alchan.ui.settings.app.AppSettingsViewModel
import com.zen.alchan.ui.settings.list.ListSettingsViewModel
import com.zen.alchan.ui.settings.notifications.NotificationsSettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ALchanApplication : Application() {

    private val appModules = module {
        val gson = Gson()

        single<LocalStorage> { LocalStorageImpl(this@ALchanApplication.applicationContext, Constant.SHARED_PREFERENCES_NAME, gson) }
        single<AppSettingsManager> { AppSettingsManagerImpl(get()) }
        single<UserManager> { UserManagerImpl(get()) }
        single<MediaManager> { MediaManagerImpl(get()) }
        single<ListStyleManager> { ListStyleManagerImpl(get()) }

        single<HeaderInterceptor> { HeaderInterceptorImpl(get()) }
        single { ApolloHandler(get()) }

        single<UserDataSource> { UserDataSourceImpl(get()) }
        single<MediaListDataSource> { MediaListDataSourceImpl(get()) }
        single<MediaDataSource> { MediaDataSourceImpl(get()) }
        single<BrowseDataSource> { BrowseDataSourceImpl(get()) }
        single<SearchDataSource> { SearchDataSourceImpl(get()) }

        single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
        single<UserRepository> { UserRepositoryImpl(get(), get()) }
        single<AppSettingsRepository> { AppSettingsRepositoryImpl(get())}
        single<MediaListRepository> { MediaListRepositoryImpl(get(), get(), gson) }
        single<MediaRepository> { MediaRepositoryImpl(get(), get(), get()) }
        single<ListStyleRepository> { ListStyleRepositoryImpl(get()) }
        single<BrowseRepository> { BrowseRepositoryImpl(get()) }
        single<SearchRepository> { SearchRepositoryImpl(get()) }

        viewModel { BaseViewModel(get()) }
        viewModel { MediaFilterViewModel(get(), get(), gson) }
        viewModel { CustomiseListViewModel(get()) }

        viewModel { SplashViewModel(get()) }
        viewModel { LoginViewModel(get()) }

        viewModel { MainViewModel(get(), get(), get()) }

        viewModel { HomeViewModel(get(), get(), get()) }
        viewModel { SearchViewModel() }
        viewModel { SearchListViewModel(get()) }
        viewModel { ExploreViewModel(get(), gson) }

        viewModel { AnimeListViewModel(get(), get(), get(), gson) }
        viewModel { AnimeListEditorViewModel(get(), get(), gson) }

        viewModel { MangaListViewModel(get(), get(), get(), gson) }
        viewModel { MangaListEditorViewModel(get(), get(), gson) }

        viewModel { MediaViewModel(get()) }
        viewModel { MediaOverviewViewModel(get()) }
        viewModel { MediaCharactersViewModel(get(), get()) }
        viewModel { MediaStaffsViewModel(get()) }

        viewModel { CharacterViewModel(get(), get()) }
        viewModel { StaffViewModel(get(), get()) }
        viewModel { StaffBioViewModel(get()) }
        viewModel { StaffVoiceViewModel(get()) }
        viewModel { StaffAnimeViewModel(get()) }
        viewModel { StaffMangaViewModel(get()) }
        viewModel { StudioViewModel(get(), get()) }

        viewModel { ProfileViewModel(get()) }
        viewModel { BioViewModel(get()) }
        viewModel { AppSettingsViewModel(get()) }
        viewModel { AniListSettingsViewModel(get()) }
        viewModel { ListSettingsViewModel(get()) }
        viewModel { NotificationsSettingsViewModel(get()) }

    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ALchanApplication)
            modules(appModules)
        }
    }
}