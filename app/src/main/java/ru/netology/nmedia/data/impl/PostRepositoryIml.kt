package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.db.toPost
import ru.netology.nmedia.db.toPostEntity
import ru.netology.nmedia.dto.Post

class PostRepositoryIml(
    private val dao: PostDao
) : PostRepository {

    override val data = dao.getAll().map{listOfEntities ->
        listOfEntities.map { postEntity ->
            postEntity.toPost()
        }
    }

    override fun like(postId: Long) {
        dao.like(postId)
    }

    override fun share(postId: Long) {
        dao.share(postId)
    }

    override fun delete(postId: Long) {
        dao.delete(postId)
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) {
            dao.insert(post.toPostEntity())
        } else {
            dao.update(post.id, post.content, post.videoUrl)
        }
    }
}