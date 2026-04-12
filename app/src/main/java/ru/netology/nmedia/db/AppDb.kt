package ru.netology.nmedia.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dao.PostDaoImpl
import ru.netology.nmedia.dto.Post

class AppDb private constructor(db: SQLiteDatabase) {
    val postDao: PostDao = PostDaoImpl(db)

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: AppDb(
                    buildDatabase(context, arrayOf(PostDaoImpl.DDL))
                ).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context, DDLs: Array<String>) = DbHelper(
            context, 1, "app.db", DDLs,
        ).writableDatabase
    }
}

class DbHelper(context: Context, dbVersion: Int, dbName: String, private val DDLs: Array<String>) :
    SQLiteOpenHelper(context, dbName, null, dbVersion) {
    override fun onCreate(db: SQLiteDatabase) {
        DDLs.forEach {
            db.execSQL(it)
        }

        val initialPosts = listOf(
            Post(
                id = 9,
                author = "Нетология. Университет интернет-профессий будущего",
                content = "Освоение новой профессии — это не только открывающиеся возможности и перспективы, но и настоящий вызов самому себе. Приходится выходить из зоны комфорта и перестраивать привычный образ жизни: менять распорядок дня, искать время для занятий, быть готовым к возможным неудачам в начале пути. В блоге рассказали, как избежать стресса на курсах профпереподготовки → http://netolo.gy/fPD",
                published = "23 сентября в 10:12",
                likes = 90,
                reposts = 5,
                video = "https://rutube.ru/video/6550a91e7e523f9503bed47e4c46d0cb",
                likedByMe = false,
                repostedByMe = false
            ),
            Post(
                id = 8,
                author = "Нетология. Университет интернет-профессий будущего",
                content = "Делиться впечатлениями о любимых фильмах легко, а что если рассказать так, чтобы все заскучали \uD83D\uDE34\n",
                published = "22 сентября в 10:14",
                likes = 80,
                reposts = 5,
                video = null,
                likedByMe = false,
                repostedByMe = false
            ),
            Post(
                id = 7,
                author = "Нетология. Университет интернет-профессий будущего",
                content = "Таймбоксинг — отличный способ навести порядок в своём календаре и разобраться с делами, которые долго откладывали на потом. Его главный принцип — на каждое дело заранее выделяется определённый отрезок времени. В это время вы работаете только над одной задачей, не переключаясь на другие. Собрали советы, которые помогут внедрить таймбоксинг \uD83D\uDC47\uD83C\uDFFB",
                published = "22 сентября в 10:12",
                likes = 70,
                reposts = 5,
                video = null,
                likedByMe = false,
                repostedByMe = false
            ),
            Post(
                id = 6,
                author = "Нетология. Университет интернет-профессий будущего",
                content = "24 сентября стартует новый поток бесплатного курса «Диджитал-старт: первый шаг к востребованной профессии» — за две недели вы попробуете себя в разных профессиях и определите, что подходит именно вам → http://netolo.gy/fQ",
                published = "21 сентября в 10:12",
                likes = 60,
                reposts = 5,
                video = null,
                likedByMe = false,
                repostedByMe = false
            ),
            Post(
                id = 5,
                author = "Нетология. Университет интернет-профессий будущего",
                content = "Диджитал давно стал частью нашей жизни: мы общаемся в социальных сетях и мессенджерах, заказываем еду, такси и оплачиваем счета через приложения.",
                published = "20 сентября в 10:14",
                likes = 50,
                reposts = 5,
                video = null,
                likedByMe = false,
                repostedByMe = false
            ),
            Post(
                id = 4,
                author = "Нетология. Университет интернет-профессий будущего",
                content = "Большая афиша мероприятий осени: конференции, выставки и хакатоны для жителей Москвы, Ульяновска и Новосибирска \uD83D\uDE09",
                published = "19 сентября в 14:12",
                likes = 40,
                reposts = 5,
                video = null,
                likedByMe = false,
                repostedByMe = false
            ),
            Post(
                id = 3,
                author = "Нетология. Университет интернет-профессий будущего",
                content = "Языков программирования много, и выбрать какой-то один бывает нелегко. Собрали подборку статей, которая поможет вам начать, если вы остановили свой выбор на JavaScript.",
                published = "19 сентября в 10:24",
                likes = 30,
                reposts = 5,
                video = null,
                likedByMe = false,
                repostedByMe = false
            ),
            Post(
                id = 2,
                author = "Нетология. Университет интернет-профессий будущего",
                content = "Знаний хватит на всех: на следующей неделе разбираемся с разработкой мобильных приложений, учимся рассказывать истории и составлять PR-стратегию прямо на бесплатных занятиях \uD83D\uDC47",
                published = "18 сентября в 10:12",
                likes = 20,
                reposts = 5,
                video = null,
                likedByMe = false,
                repostedByMe = false
            ),
            Post(
                id = 1,
                author = "Нетология. Университет интернет-профессий будущего",
                content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                published = "21 мая в 18:36",
                likes = 10,
                reposts = 5,
                video = null,
                likedByMe = false,
                repostedByMe = false
            )
        ).reversed()

        initialPosts.forEach { post ->
            val values = ContentValues().apply {
                put(PostDaoImpl.PostColumns.COLUMN_AUTHOR, post.author)
                put(PostDaoImpl.PostColumns.COLUMN_PUBLISHED, post.published)
                put(PostDaoImpl.PostColumns.COLUMN_CONTENT, post.content)
                put(PostDaoImpl.PostColumns.COLUMN_LIKES, post.likes)
                put(PostDaoImpl.PostColumns.COLUMN_REPOSTS, post.reposts)
                put(PostDaoImpl.PostColumns.COLUMN_VIDEO, post.video)
                put(PostDaoImpl.PostColumns.COLUMN_LIKED_BY_ME, post.likedByMe)
                put(PostDaoImpl.PostColumns.COLUMN_REPOSTED_BY_ME, post.repostedByMe)
            }
            db.insert(PostDaoImpl.PostColumns.TABLE, null, values)
        }
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        TODO("Not yet implemented")
    }
}



