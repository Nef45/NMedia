package ru.netology.nmedia.db

object PostsTable {

    const val NAME = "posts"

    enum class Column (val columnName: String) {
        ID ("id"),
        AUTHOR ("author"),
        CONTENT ("content"),
        PUBLISHED ("published"),
        LIKES ("likes"),
        LIKED_BY_ME ("likedByMe"),
        SHARES ("shares"),
        SHARED_BY_ME ("sharedByMe"),
        VIEWS ("views"),
        VIDEO_URL ("videoUrl")
    }

    val DDL = """
        CREATE TABLE $NAME (
        ${Column.ID.columnName} INTEGER PRIMARY KEY AUTOINCREMENT,
        ${Column.AUTHOR.columnName} TEXT NOT NULL,
        ${Column.CONTENT.columnName} TEXT NOT NULL,
        ${Column.PUBLISHED.columnName} TEXT NOT NULL,
        ${Column.LIKES.columnName} INTEGER NOT NULL DEFAULT 0,
        ${Column.LIKED_BY_ME.columnName} BOOLEAN NOT NULL DEFAULT 0,
        ${Column.SHARES.columnName} INTEGER NOT NULL DEFAULT 0,
        ${Column.SHARED_BY_ME.columnName} BOOLEAN NOT NULL DEFAULT 0,
        ${Column.VIEWS.columnName} INTEGER NOT NULL DEFAULT 0,
        ${Column.VIDEO_URL.columnName} TEXT NOT NULL
        );
    """.trimIndent()

    val ALL_COLUMNS_NAMES = Column.values()
        .map { columns -> columns.columnName }
        .toTypedArray()
}