package ru.netology.nmedia.db

import android.database.Cursor
import ru.netology.nmedia.dto.Post

internal fun PostEntity.toPost(): Post {
    val post = Post (
    id = id,
    author = author,
    content = content,
    published = published,
    likes = likes,
    likedByMe = likedByMe,
    shares = shares,
    sharedByMe = sharedByMe,
    views = views
    )
    post.videoUrl = videoUrl

    return post
}

internal fun Post.toPostEntity(): PostEntity {
    val postEntity = PostEntity (
        id = id,
        author = author,
        content = content,
        published = published,
        likes = likes,
        likedByMe = likedByMe,
        shares = shares,
        sharedByMe = sharedByMe,
        views = views
    )
    postEntity.videoUrl = videoUrl

    return postEntity
}