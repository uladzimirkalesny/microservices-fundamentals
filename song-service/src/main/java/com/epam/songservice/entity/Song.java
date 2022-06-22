package com.epam.songservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "SONGS")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "artist", nullable = false)
    private String artist;
    @Column(name = "album", nullable = false)
    private String album;
    @Column(name = "song_length", nullable = false)
    private String length;
    @Column(name = "resource_id", nullable = false)
    private Integer resourceId;
    @Column(name = "song_year", nullable = false)
    private Integer year;

    public Song(String name, String artist, String album, String length, Integer resourceId, Integer year) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.length = length;
        this.resourceId = resourceId;
        this.year = year;
    }
}
