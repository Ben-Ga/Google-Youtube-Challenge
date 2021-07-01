package com.google;

import java.util.ArrayList;
import java.util.List;

/** A class used to represent a Playlist */
class VideoPlaylist {

    private List<Video> videos = new ArrayList<>();

    private final String PLAYLIST_NAME;


    VideoPlaylist(String name){
        PLAYLIST_NAME = name;
    }


    public String getName(){
        return PLAYLIST_NAME;
    }

    public boolean addVideo(Video video){
        if(videos.contains(video)){
            return false;
        }else{
            videos.add(video);
            return true;
        }
    }

    public void removeVideo(Video video){
        videos.remove(video);
    }

    public void clearPlaylist(){
        videos.clear();
    }

    public List<Video> getVideos(){
        return videos;
    }

    public boolean contains(Video video){
        if(videos.contains(video)){
            return true;
        }else{
            return false;
        }
    }



    public int numberOfVideos(){
        return videos.size();
    }


}
