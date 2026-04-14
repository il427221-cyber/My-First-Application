package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import ru.netology.nmedia.entity.PostEntity


@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Upsert
    fun save(post: PostEntity)

    @Query(
        """
           UPDATE posts SET
               likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = :id;
        """
    )

    fun likeById(id: Long)

    @Query("DELETE FROM posts WHERE id = :id")
    fun removeById(id: Long)



    @Query(            """
            UPDATE posts SET
            reposts = reposts + 1,
            repostedByMe = 1
            WHERE id = :id;
        """
    )
    fun repostById(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<PostEntity>)

}




