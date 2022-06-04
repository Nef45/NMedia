package ru.netology.nmedia.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Insert
    fun insert(post: PostEntity)

    @Query("UPDATE posts SET content = :content, videoUrl = :videoUrl WHERE id = :postId")
    fun update(postId: Long, content: String, videoUrl: String)


    @Query("""
        UPDATE posts SET
        likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :postId
    """)
    fun like(postId: Long)

    @Query("""
        UPDATE posts SET
        shares = shares + 1,
        sharedByMe = 1
        WHERE id = :postId
    """)
    fun share(postId: Long)

    @Query("DELETE FROM posts WHERE id = :postId")
    fun delete(postId: Long)
}