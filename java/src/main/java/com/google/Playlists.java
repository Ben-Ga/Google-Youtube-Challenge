package com.google;

import java.util.ArrayList;
import java.util.List;

public class Playlists {


    public List<VideoPlaylist> playlists;


    Playlists(){
        playlists = new ArrayList<>();
    }


    public void addPlaylist(String name){
        if(getPlaylistNames().contains(name.toLowerCase())){
            System.out.println("Cannot create playlist: A playlist with the same name already exists");
        }else{
            playlists.add(new VideoPlaylist(name));
            System.out.println("Successfully created new playlist: " + name);
        }
    }

    public void removePlaylist(VideoPlaylist vp){
        playlists.remove(vp);
    }


    public void showAllPlaylists(){
        if(playlists.size() > 0){
            List<String> playlistTitles = new ArrayList<>();
            StringBuilder sb = new StringBuilder();

            sb.append("Showing all playlists:\n");
            for(VideoPlaylist vp : playlists){
                playlistTitles.add(vp.getName());
            }
            java.util.Collections.sort(playlistTitles);

            for(String s : playlistTitles){
                sb.append("\t" + s + "\n");
            }
            System.out.println(sb.toString());
        }else{
            System.out.println("No playlists exist yet");
        }
    }

    private List<String> getPlaylistNames(){
        List<String> names = new ArrayList<>();

        for(VideoPlaylist vp : playlists){
            names.add(vp.getName().toLowerCase());
        }

        return names;
    }



    VideoPlaylist getPlaylist (String playlistName) {
        VideoPlaylist match = null;
        for(VideoPlaylist vp : playlists){
            if(vp.getName().toLowerCase().equals(playlistName.toLowerCase())){
                match = vp;
            }
        }
        return match;
    }

}
