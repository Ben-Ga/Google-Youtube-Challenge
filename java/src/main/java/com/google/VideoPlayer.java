package com.google;

import java.util.*;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private Playlists playlists = new Playlists();
  private enum  PlayState  { PLAYING, NOT_PLAYING, PAUSED }
  private  PlayState videoState = PlayState.NOT_PLAYING;
  private Video playingVideo;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    StringBuilder sb = new StringBuilder();
    List<Video> videoArrayList;
    videoArrayList = videoLibrary.getVideos();
    List<String> titles = new ArrayList<>();

    for(Video v : videoArrayList){
      titles.add(v.getTitle() + " (" + v.getVideoId() + ") " + parseTags(v.getTags()));
    }
    java.util.Collections.sort(titles);

    sb.append("Here's a list of all available videos: \n");

    for(String s : titles){
      sb.append("\t" + s + "\n");
    }
    System.out.print(sb.toString());
  }

  public void playVideo(String videoId) {
    //Does video exist
    if(videoLibrary.getVideo(videoId) != null){
      Video tempVideo = videoLibrary.getVideo(videoId);
      //Are we already playing a video?
      if(videoState == PlayState.NOT_PLAYING){
        playingVideo = tempVideo;
        System.out.println(playMessage());
        videoState = PlayState.PLAYING;
      }else{
        //If a video was paused, set state back to play, otherwise ignore
        videoState = videoState == PlayState.PAUSED ? PlayState.PLAYING : videoState;
        //If we are, check if it is different to currently playing
        if(tempVideo.getVideoId() != playingVideo.getVideoId()){
          //Change video instance if different
          System.out.println(stopMessage());
          playingVideo = tempVideo;
        }else{
          System.out.println(stopMessage());
        }
        System.out.println(playMessage());
      }
    }else{
      System.out.println("Cannot play video: Video does not exist");
    }

  }

  public void stopVideo() {
    if(videoState != PlayState.NOT_PLAYING){
      videoState = PlayState.NOT_PLAYING;
      System.out.println(stopMessage());
      playingVideo = null;
    }else{
      System.out.println("Cannot stop video: No video is currently playing");
    }
  }

  public void playRandomVideo() {
    if(videoState == PlayState.PLAYING || videoState == PlayState.PAUSED){
      System.out.println(stopMessage());
    }
    Random rand = new Random();
    int index = rand.nextInt(videoLibrary.getVideos().size());
    playingVideo = videoLibrary.getVideos().get(index);
    videoState = PlayState.PLAYING;
    System.out.println(playMessage());
  }

  public void pauseVideo() {
    if(videoState != PlayState.PAUSED){
      if(videoState != PlayState.NOT_PLAYING){
        //Must be playing, so pause
        System.out.println(pauseMessage());
        videoState = PlayState.PAUSED;

      }else{
        System.out.println("Cannot pause video: No video is currently playing");
      }
    }else{
      System.out.println("Video already paused: " + playingVideo.getTitle());
    }
  }

  public void continueVideo() {
    if(videoState == PlayState.PAUSED){
      videoState = PlayState.PLAYING;
      System.out.println("Continuing video: " + playingVideo.getTitle());
    }else{
      if(videoState == PlayState.NOT_PLAYING){
        System.out.println("Cannot continue video: No video is currently playing");
      }else{
        System.out.println("Cannot continue video: Video is not paused");
      }
    }
  }

  public void showPlaying() {
    if(videoState != PlayState.NOT_PLAYING){
      System.out.println("Currently playing: " + playingVideo.getTitle() + " (" + playingVideo.getVideoId() + ") " + parseTags(playingVideo.getTags()) + pausedStatus());
    }else{
      System.out.println("No video is currently playing");
    }
  }

  public void createPlaylist(String playlistName) {
    playlists.addPlaylist(playlistName);
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    Video tempVideo = videoLibrary.getVideo(videoId);
    VideoPlaylist tempPlaylist = playlists.getPlaylist(playlistName);
    if(tempPlaylist != null){
      if(tempVideo != null){
        if(tempPlaylist.addVideo(tempVideo)){
          System.out.println("Added video to " + playlistName + ": " + tempVideo.getTitle());
        }else{
          System.out.println("Cannot add video to " + playlistName + ": Video already added");
        }
      }else{
        System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
      }
    }else{
      System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
    }

  }

  public void showAllPlaylists() {
    playlists.showAllPlaylists();
  }

  public void showPlaylist(String playlistName) {
    VideoPlaylist tempPlaylist = playlists.getPlaylist(playlistName);
    if(tempPlaylist != null){
      StringBuilder sb = new StringBuilder();
      sb.append("Showing playlist: " + playlistName + "\n");
      if(tempPlaylist.numberOfVideos() == 0){
        sb.append("\tNo videos here yet");
      }else {
        for (Video v: tempPlaylist.getVideos()
             ) {
          sb.append("\t" + v.getTitle() + " (" + v.getVideoId() + ") " + parseTags(v.getTags()) + "\n");
        }
        sb.deleteCharAt(sb.length()-1);
      }
      System.out.println(sb.toString());
    }else{
      System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    Video tempVideo = videoLibrary.getVideo(videoId);
    VideoPlaylist tempPlaylist = playlists.getPlaylist(playlistName);
    if(tempPlaylist != null) {
      if (tempVideo != null) {
        if(tempPlaylist.contains(tempVideo)){
          tempPlaylist.removeVideo(tempVideo);
          System.out.println("Removed video from " + playlistName + ": " + tempVideo.getTitle());
        }else{
          System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
        }

      }else{
        System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
      }

    }else{
      System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");

    }
  }

  public void clearPlaylist(String playlistName) {
    VideoPlaylist tempPlaylist = playlists.getPlaylist(playlistName);
    if(tempPlaylist != null){
      tempPlaylist.clearPlaylist();
      System.out.println("Successfully removed all videos from " + playlistName);
    }else{
      System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
    }
  }

  public void deletePlaylist(String playlistName) {
    VideoPlaylist tempPlaylist = playlists.getPlaylist(playlistName);
    if(tempPlaylist != null){
      playlists.removePlaylist(tempPlaylist);
      System.out.println("Deleted playlist: " + playlistName);
    }else{
      System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
    }
  }

  public void searchVideos(String searchTerm) {
    List<Video> matches = videoLibrary.getVideoMatches(searchTerm);
    StringBuilder sb = new StringBuilder();
    HashMap<Integer, Video> options = new HashMap<>();
    HashMap<String, Video> sortedToVideo = new HashMap<>();

    if(matches.size() > 0){
      sb.append("Here are the results for " + searchTerm + ":\n");
      int count = 1;
      List<String> sortedSearch = new ArrayList<>();
      for(Video v: matches){
        sortedSearch.add(v.getTitle() + " (" + v.getVideoId() + ") " + parseTags(v.getTags()) + "\n");
        sortedToVideo.put(v.getTitle() + " (" + v.getVideoId() + ") " + parseTags(v.getTags()) + "\n", v);
      }
      //sort by name
      java.util.Collections.sort(sortedSearch);

      for(String s : sortedSearch){
        sb.append("\t"+ count + ") " + s);
        Video option = sortedToVideo.get(s);
        options.put(count, option);
        count++;
      }
      sb.append("Would you like to play any of the above? If yes, specify the number of the video.\n");
      sb.append("If your answer is not a valid number, we will assume it's a no.");

      System.out.println(sb.toString());

      Scanner input = new Scanner(System.in);
      String index = input.nextLine();
      int indexNum;
      try {
        indexNum = Integer.parseInt(index);
        Video selectedVideo = options.get(indexNum);
        playingVideo = selectedVideo;
        videoState = PlayState.PLAYING;
        System.out.println(playMessage());
      }catch (NumberFormatException e){
        System.out.println("Nope!");
      }
    }else{
      System.out.println("No search results for blah");
    }

  }

  public void searchVideosWithTag(String videoTag) {

  }

  public void flagVideo(String videoId) {
    System.out.println("flagVideo needs implementation");
  }

  public void flagVideo(String videoId, String reason) {
    System.out.println("flagVideo needs implementation");
  }

  public void allowVideo(String videoId) {
    System.out.println("allowVideo needs implementation");
  }

  private String playMessage(){
    return "Playing video: " + playingVideo.getTitle();
  }

  private String stopMessage(){
    return "Stopping video: " + playingVideo.getTitle();
  }

  private String pauseMessage(){
    return "Pausing video: " + playingVideo.getTitle();
  }

  private String pausedStatus(){
    if(videoState == PlayState.PAUSED){
      return " - PAUSED";
    }else{
      return "";
    }
  }

  private String parseTags(List<String> tags){

    if(tags.size() > 0) {
      StringBuilder sb = new StringBuilder();
      sb.append("[");
      for (String tag : tags) {
        sb.append(tag + " ");
      }
      sb.deleteCharAt(sb.length() - 1);
      sb.append("]");
      return sb.toString();
    }else{
      return "[]";
    }
  }

}