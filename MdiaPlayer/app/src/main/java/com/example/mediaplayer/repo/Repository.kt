package com.example.mediaplayer.repo

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity
import com.example.mediaplayer.db.AlbumListDataBase
import com.example.mediaplayer.db.AlbumListEntity
import com.example.mediaplayer.viewmodel.MusicModel
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.ArrayList

object Repository {

    private val albumArtUri: Uri = Uri.parse("content://media/external/audio/albumart")


    suspend fun getAllMusicInDevice(context: Context): MutableList<MusicModel> = withContext(Dispatchers.IO) {
        val contentResolver = context.contentResolver
        val audioColumns = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST
        )
        val cursor: Cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            audioColumns,
            null,
            null,
            null
        )!!
        val musicList: MutableList<MusicModel> = ArrayList<MusicModel>()
        while (cursor.moveToNext()) {
            val _id: String =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
            val filePath: String =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
            val title: String =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
            val mime_type: String =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE))
            val artist: String =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
            val albumId: Long =
                cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
            val uri = ContentUris.withAppendedId(albumArtUri, albumId)
            musicList.add(MusicModel(uri, title, artist, filePath))
        }
        musicList
    }

    suspend fun writeIntoAlbumDb(context: Context, musicList: MutableList<MusicModel>) =
        withContext(Dispatchers.IO) {
            val albumDao = AlbumListDataBase.getInstance(context).albumListDao()
            for (music in musicList) {
                albumDao.insertAlbumList(music.covertToAlbumEntity())
            }
        }

    suspend fun getAllAlbum(context: Context) = withContext(Dispatchers.IO) {
        val albumDao = AlbumListDataBase.getInstance(context).albumListDao()
        albumDao.getAllAlbumList()
    }


    private fun MusicModel.covertToAlbumEntity (): AlbumListEntity {
        return AlbumListEntity(this.musicName.get() ?:"",
            this.musicArtist.get()?:"", this.musicPath.get()?:"")
    }
}

