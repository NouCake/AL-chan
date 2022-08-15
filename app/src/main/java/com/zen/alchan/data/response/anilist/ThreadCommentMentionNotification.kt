package com.zen.alchan.data.response.anilist

import type.NotificationType

data class ThreadCommentMentionNotification(
    override val id: Int = 0,
    val userId: Int = 0,
    override val type: NotificationType = NotificationType.THREAD_COMMENT_MENTION,
    val commentId: Int = 0,
    val context: String = "",
    override val createdAt: Int = 0,
    val thread: Thread = Thread(),
    val comment: ThreadComment = ThreadComment(),
    val user: User = User()
) : Notification