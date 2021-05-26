package com.zen.alchan.ui.profile

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.textview.MaterialTextView
import com.stfalcon.imageviewer.loader.ImageLoader
import com.zen.alchan.R
import com.zen.alchan.helper.extensions.applyTopPaddingInsets
import com.zen.alchan.helper.extensions.getAttrValue
import com.zen.alchan.helper.extensions.show
import com.zen.alchan.helper.utils.ImageUtil
import com.zen.alchan.ui.activity.ActivityFragment
import com.zen.alchan.ui.base.BaseFragment
import com.zen.alchan.ui.bio.BioFragment
import com.zen.alchan.ui.favorite.FavoriteFragment
import com.zen.alchan.ui.review.ReviewFragment
import com.zen.alchan.ui.stats.StatsFragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.layout_not_logged_in.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs


class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val viewModel by viewModel<ProfileViewModel>()
    private val sharedViewModel by sharedViewModel<SharedProfileViewModel>()

    private var viewPagerAdapter: FragmentStateAdapter? = null

    private var fragments: List<Fragment?>? = null
    private var cardMenu: List<Pair<AppCompatImageView, MaterialTextView>>? = null

    private var bioFragment: BioFragment? = null
    private var favoriteFragment: FavoriteFragment? = null
    private var statsFragment: StatsFragment? = null
    private var reviewFragment: ReviewFragment? = null

    private var menuItemActivities: MenuItem? = null
    private var menuItemNotifications: MenuItem? = null
    private var menuItemAddAsBestFriend: MenuItem? = null
    private var menuItemSettings: MenuItem? = null
    private var menuItemViewOnAniList: MenuItem? = null
    private var menuItemShareProfile: MenuItem? = null
    private var menuItemCopyLink: MenuItem? = null

    private var scaleUpAnimation: Animation? = null
    private var scaleDownAnimation: Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sharedViewModel.userId = it.getInt(USER_ID)
        }
    }

    override fun setUpLayout() {
        scaleUpAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up)
        scaleDownAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_down)

        profileToolbar.menu.apply {
            menuItemActivities = findItem(R.id.itemActivities)
            menuItemNotifications = findItem(R.id.itemNotifications)
            menuItemAddAsBestFriend = findItem(R.id.itemAddAsBestFriend)
            menuItemSettings = findItem(R.id.itemSettings)
            menuItemViewOnAniList = findItem(R.id.itemViewOnAniList)
            menuItemShareProfile = findItem(R.id.itemShareProfile)
            menuItemCopyLink = findItem(R.id.itemCopyLink)
        }

        bioFragment = BioFragment.newInstance()
        favoriteFragment = FavoriteFragment.newInstance()
        statsFragment = StatsFragment.newInstance()
        reviewFragment = ReviewFragment.newInstance(true)

        fragments = listOf(
            bioFragment,
            favoriteFragment,
            statsFragment,
            reviewFragment
        )

        cardMenu = listOf(
            profileBioIcon to profileBioText,
            profileFavoritesIcon to profileFavoritesText,
            profileStatsIcon to profileStatsText,
            profileReviewsIcon to profileReviewsText
        )

        viewPagerAdapter = ProfileViewPagerAdapter(
            childFragmentManager,
            viewLifecycleOwner.lifecycle,
            fragments?.filterNotNull() ?: listOf()
        )
        profileViewPager.adapter = viewPagerAdapter

        profileViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val selectedColor = ColorStateList.valueOf(requireContext().getAttrValue(R.attr.themeSecondaryColor))
                val unselectedColor = ColorStateList.valueOf(requireContext().getAttrValue(R.attr.themeContentColor))

                cardMenu?.toList()?.forEachIndexed { index, cardItem ->
                    val cardIcon = cardItem.first
                    val cardText = cardItem.second

                    if (index == position) {
                        cardIcon.imageTintList = selectedColor
                        cardText.setTextColor(selectedColor)
                    } else {
                        cardIcon.imageTintList = unselectedColor
                        cardText.setTextColor(unselectedColor)
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    viewModel.setCurrentPage(SharedProfileViewModel.Page.values()[profileViewPager.currentItem])
                }
            }
        })

        profileBioLayout.setOnClickListener {
            viewModel.setCurrentPage(SharedProfileViewModel.Page.BIO)
        }

        profileFavoritesLayout.setOnClickListener {
            viewModel.setCurrentPage(SharedProfileViewModel.Page.FAVORITE)
        }

        profileStatsLayout.setOnClickListener {
            viewModel.setCurrentPage(SharedProfileViewModel.Page.STATS)
        }

        profileReviewsLayout.setOnClickListener {
            viewModel.setCurrentPage(SharedProfileViewModel.Page.REVIEW)
        }

        goToLoginButton.setOnClickListener {
            viewModel.logout()
            navigation.navigateToLanding()
        }

        menuItemActivities?.setOnMenuItemClickListener {
            navigation.navigateToActivities()
            true
        }

        menuItemNotifications?.setOnMenuItemClickListener {
            navigation.navigateToNotifications()
            true
        }

        menuItemSettings?.setOnMenuItemClickListener {
            navigation.navigateToSettings()
            true
        }

        profileAppBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            profileSwipeRefresh?.isEnabled = verticalOffset == 0

            if (abs(verticalOffset) - appBarLayout.totalScrollRange >= -50) {
                if (profileNumberLayout?.isVisible == true) {
                    profileNumberLayout?.startAnimation(scaleDownAnimation)
                    profileNumberLayout?.visibility = View.INVISIBLE
                }
            } else {
                if (profileNumberLayout?.isInvisible == true) {
                    profileNumberLayout?.startAnimation(scaleUpAnimation)
                    profileNumberLayout?.show(true)
                }
            }
        })

        profileSwipeRefresh.setOnRefreshListener {
            sharedViewModel.reloadData()
        }
    }

    override fun setUpInsets() {
        profileHeaderGap.applyTopPaddingInsets()
    }

    override fun setUpObserver() {
        disposables.add(
            viewModel.isAuthenticated.subscribe {
                notLoggedInLayout.show(!it)
            }
        )

        disposables.add(
            viewModel.currentPage.subscribe {
                profileViewPager.setCurrentItem(it.ordinal, true)
            }
        )

        sharedDisposables.add(
            sharedViewModel.loading.subscribe {
                profileSwipeRefresh.isRefreshing = it
            }
        )

        sharedDisposables.add(
            sharedViewModel.isViewerProfile.subscribe {
                showToolbarMenu(it)
            }
        )

        sharedDisposables.add(
            sharedViewModel.userAndAppSetting.subscribe { (user, appSetting) ->
                profileUsernameText.text = user.name

                if (appSetting.useCircularAvatarForProfile)
                    ImageUtil.loadCircleImage(requireContext(), user.avatar.large, profileAvatarCircleImage)
                else
                    ImageUtil.loadRectangleImage(requireContext(), user.avatar.large, profileAvatarRectangleImage)

                profileAvatarCircleImage.show(appSetting.useCircularAvatarForProfile)
                profileAvatarRectangleImage.show(appSetting.useCircularAvatarForProfile)

                ImageUtil.loadImage(requireContext(), user.bannerImage, profileBannerImage)
            }
        )

        sharedDisposables.add(
            sharedViewModel.animeCount.subscribe {
                profileAnimeCountText.text = it.toString()
            }
        )

        sharedDisposables.add(
            sharedViewModel.mangaCount.subscribe {
                profileMangaCountText.text = it.toString()
            }
        )

        sharedDisposables.add(
            sharedViewModel.followingCount.subscribe {
                profileFollowingCountText.text = it.toString()
            }
        )

        sharedDisposables.add(
            sharedViewModel.followersCount.subscribe {
                profileFollowersCountText.text = it.toString()
            }
        )

        sharedViewModel.loadData()
    }

    private fun showToolbarMenu(isViewerProfile: Boolean) {
        menuItemNotifications?.isVisible = isViewerProfile
        menuItemAddAsBestFriend?.isVisible = !isViewerProfile
        menuItemSettings?.isVisible = isViewerProfile
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewPagerAdapter = null
        fragments = null
        cardMenu = null
        bioFragment = null
        favoriteFragment = null
        statsFragment = null
        reviewFragment = null
        menuItemActivities = null
        menuItemNotifications = null
        menuItemAddAsBestFriend = null
        menuItemSettings = null
        menuItemViewOnAniList = null
        menuItemShareProfile = null
        menuItemCopyLink = null
    }

    companion object {
        private const val USER_ID = "userId"

        @JvmStatic
        fun newInstance(userId: Int = 0) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putInt(USER_ID, userId)
                }
            }
    }
}