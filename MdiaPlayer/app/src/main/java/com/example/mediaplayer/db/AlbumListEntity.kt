package com.example.mediaplayer.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albumList")
public class AlbumListEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "albumName", typeAffinity = ColumnInfo.TEXT)
    public var albumName: String = "",
    @ColumnInfo(name = "artist", typeAffinity = ColumnInfo.TEXT)
    public var artist: String = "",
    @ColumnInfo(name = "path", typeAffinity = ColumnInfo.TEXT)
    public var path: String = ""
)