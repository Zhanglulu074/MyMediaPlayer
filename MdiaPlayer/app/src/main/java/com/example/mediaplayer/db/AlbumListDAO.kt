package com.example.mediaplayer.db

import androidx.room.*

@Dao
public interface AlbumListDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlbumList(album: AlbumListEntity)

    @Delete
    fun deleteAlbumList(album: AlbumListEntity)

    @Update
    fun updateAlbumList(album: AlbumListEntity)

    @Query("SELECT * FROM albumList")
    fun getAllAlbumList(): MutableList<AlbumListEntity>

    @Query("SELECT * FROM albumList WHERE albumName = :name")
    fun getAlbumByName(name: String): AlbumListEntity
}