package net.roarsoftware.lastfm.scrobble;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Bean that contains track information.
 *
 * @author Janni Kovacs
 */
public class SubmissionData {

  private String artist;
  private String track;
  private String album;
  private long startTime;
  private Source source;
  private int length;
  private int tracknumber;
  private String musicBrainzID;

  public SubmissionData(String artist, String track, String album, int length,
                        int tracknumber, Source source, long startTime) {
    this.artist = artist;
    this.track = track;
    this.album = album;
    this.length = length;
    this.tracknumber = tracknumber;
    this.source = source;
    this.startTime = startTime;
  }

  public SubmissionData(String artist, String track, String album, int length,
    int tracknumber, Source source, long startTime,String musicBrainzID) {
    this.artist = artist;
    this.track = track;
    this.album = album;
    this.length = length;
    this.tracknumber = tracknumber;
    this.source = source;
    this.startTime = startTime;
    this.musicBrainzID = musicBrainzID;
  }

  String toString(String sessionId, int index) {
    String mbId = musicBrainzID != null ? musicBrainzID : "";
    String b = album != null ? album : "";
    String artist = this.artist;
    String track = this.track;
    try {
      artist = URLEncoder.encode(artist, "UTF-8");
      track = URLEncoder.encode(track, "UTF-8");
      b = URLEncoder.encode(b, "UTF-8");
      mbId = URLEncoder.encode(mbId, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      // UTF-8 always available
    }
    String l = length == -1 ? "" : String.valueOf(length);
    String n = tracknumber == -1 ? "" : String.valueOf(tracknumber);
    return String
        .format("s=%s&a[%9$d]=%s&t[%9$d]=%s&i[%9$d]=%s&o[%9$d]=%s&r[%9$d]=&l[%9$d]=%s&b[%9$d]=%s&n[%9$d]=%s&m[%9$d]=",
            sessionId, artist, track, startTime, source.getCode(), l, b, n, index);
  }

}
