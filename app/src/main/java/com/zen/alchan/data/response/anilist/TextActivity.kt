package com.zen.alchan.data.response.anilist

import com.zen.alchan.data.entity.AppSetting
import type.ActivityType

data class TextActivity(
    override val id: Int = 0,
    val userId: Int = 0,
    override val type: ActivityType = ActivityType.TEXT,
    override val replyCount: Int = 0,
    val text: String = "",
    override val isLocked: Boolean = false,
    override var isSubscribed: Boolean = false,
    override var likeCount: Int = 0,
    override var isLiked: Boolean = false,
    val isPinned: Boolean = false,
    override val siteUrl: String = "",
    override val createdAt: Int = 0,
    val user: User = User(),
    override val replies: List<ActivityReply> = listOf(),
    override val likes: List<User> = listOf()
) : Activity {

    override fun user(): User {
        return user
    }

    override fun hasRecipient(): Boolean {
        return false
    }

    override fun recipient(): User {
        return User()
    }

    override fun message(appSetting: AppSetting): String {
        return text
    }

    override fun hasMedia(): Boolean {
        return false
    }

    override fun media(): Media {
        return Media()
    }
}