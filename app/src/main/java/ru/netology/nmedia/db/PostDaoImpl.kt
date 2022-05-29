package ru.netology.nmedia.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dto.Post

class PostDaoImpl(
    private val dataBase: SQLiteDatabase
) : PostDao {

    override fun getAll(): List<Post> {
        return dataBase.query(
            PostsTable.NAME,
            PostsTable.ALL_COLUMNS_NAMES,
            null, null, null, null,
            "${PostsTable.Column.ID.columnName} DESC"
        ).use { cursor ->
            cursor.moveToNext()
            List(cursor.count) { index ->
                cursor.toPost().also {
                    cursor.moveToNext()
                }
            }
        }
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            put(PostsTable.Column.AUTHOR.columnName, post.author)
            put(PostsTable.Column.CONTENT.columnName, post.content)
            put(PostsTable.Column.PUBLISHED.columnName, post.published)
            put(PostsTable.Column.VIDEO_URL.columnName, post.videoUrl)
        }

        val id = if (post.id != 0L) {
            dataBase.update(
                PostsTable.NAME,
                values,
                "${PostsTable.Column.ID.columnName} = ?",
                arrayOf(post.id.toString())
            )
            post.id
        } else {
            dataBase.insert(
                PostsTable.NAME,
                null,
                values
            )
        }

        return dataBase.query(
            PostsTable.NAME,
            PostsTable.ALL_COLUMNS_NAMES,
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(id.toString()),
            null, null, null, null
        ).use { cursor ->
            cursor.moveToNext()
            cursor.toPost()
        }
    }

    override fun like(postId: Long) {
        dataBase.execSQL(
            """
                UPDATE ${PostsTable.NAME} SET
                ${PostsTable.Column.LIKES.columnName} = ${PostsTable.Column.LIKES.columnName} + CASE WHEN ${PostsTable.Column.LIKED_BY_ME.columnName} THEN -1 ELSE 1 END,
                ${PostsTable.Column.LIKED_BY_ME.columnName} = CASE WHEN ${PostsTable.Column.LIKED_BY_ME.columnName} THEN 0 ELSE 1 END
                WHERE ${PostsTable.Column.ID.columnName} = ?;
            """.trimIndent(),
            arrayOf(postId)
        )
    }

    override fun share(postId: Long) {
        dataBase.execSQL(
            """
                UPDATE ${PostsTable.NAME} SET
                ${PostsTable.Column.SHARES.columnName} = ${PostsTable.Column.SHARES.columnName} + 1,
                ${PostsTable.Column.SHARED_BY_ME.columnName} = 1
                WHERE ${PostsTable.Column.ID.columnName} = ?;
            """.trimIndent(),
            arrayOf(postId)
        )
    }

    override fun delete(postId: Long) {
        dataBase.delete(
            PostsTable.NAME,
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(postId.toString())
        )
    }
}