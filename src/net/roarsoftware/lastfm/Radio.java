package net.roarsoftware.lastfm;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import static net.roarsoftware.util.StringUtilities.encode;
import static net.roarsoftware.util.StringUtilities.md5;
import net.roarsoftware.xml.DomElement;

/**
 * Provides method to tune in to last.fm radio stations.<br/>
 * This class uses the <b>old</b> radio APIs since the 2.0 API radio methods are not documented yet.<br/>
 * Note that this class does neither play nor scrobble songs. It only changes stations and fetches the playlist
 * which contains the location to the actual sound file. Playing and scrobbling has to be done by the application.<br/>
 * Also be aware that the last.fm radio service will be closed down so that only special API keys will have access
 * to the radio.<br/>
 * Therefore this API is experimental and may be removed in a later release.
 *
 * @author Janni Kovacs
 */
public class Radio {

	private static final String HANDSHAKE_URL = "http://ws.audioscrobbler.com/radio/handshake.php?username=%s&passwordmd5=%s&language=%s&player=%s&platform=%s&version=%s&platformversion=%s";
	private static final String ADJUST_URL = "http://ws.audioscrobbler.com/radio/adjust.php?session=%s&url=%s&lang=%s";
	private static final String PLAYLIST_URL = "http://ws.audioscrobbler.com/radio/xspf.php?sk=%s&discovery=0&desktop=1.5";
	private String lang = "en";

	private String playerName;
	private String playerVersion;

	private String session;
	private boolean subscriber;
	private String stationName;

	private Radio() {
	}

	public static Radio newRadio(String playerName, String playerVersion) {
		Radio r = new Radio();
		r.playerName = playerName;
		r.playerVersion = playerVersion;
		return r;
	}

	public boolean isSubscriber() {
		return subscriber;
	}

	public String getStationName() {
		return stationName;
	}

	public Result handshake(String username, String password) {
		String platform = System.getProperty("os.name");
		String platformVersion = System.getProperty("os.version");
		String url = String.format(HANDSHAKE_URL, encode(username), md5(password), lang, encode(playerName),
				encode(platform), encode(playerVersion), encode(platformVersion));
		try {
			HttpURLConnection connection = Caller.getInstance().openConnection(url);
			Properties props = new Properties();
			props.load(connection.getInputStream());
			if ("FAILED".equals(props.getProperty("session"))) {
				return new Result(props.getProperty("msg"));
			}
			this.session = props.getProperty("session");
			this.subscriber = "1".equals(props.getProperty("subscriber"));
			return Result.createOkResult(null);
		} catch (IOException e) {
			return new Result(e.getMessage());
		}
	}

	public Result changeStation(RadioStation station) {
		return changeStation(station.getUrl());
	}

	public Result changeStation(String stationUrl) {
		String url = String.format(ADJUST_URL, session, encode(stationUrl), lang);
		try {
			HttpURLConnection connection = Caller.getInstance().openConnection(url);
			Properties props = new Properties();
			props.load(connection.getInputStream());
			if ("FAILED".equals(props.getProperty("response"))) {
				this.stationName = null;
				return Result.createRestErrorResult(Integer.parseInt(props.getProperty("error")), null);
			}
			this.stationName = props.getProperty("stationname");
			return Result.createOkResult(null);
		} catch (IOException e) {
			return new Result(e.getMessage());
		}
	}

	public Playlist fetchPlaylist() {
		String url = String.format(PLAYLIST_URL, session);
		try {
			HttpURLConnection connection = Caller.getInstance().openConnection(url);
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(connection.getInputStream());
			DomElement elem = new DomElement(doc.getDocumentElement());
			return Playlist.playlistFromElement(elem);
		} catch (Exception e) {
			return null;
		}
	}

	public static class RadioStation {
		private String url;

		private RadioStation(String s) {
			this.url = s;
		}

		public String getUrl() {
			return url;
		}

		public static RadioStation similarArtists(String artist) {
			return new RadioStation("lastfm://artist/" + artist + "/similarartists");
		}

		public static RadioStation artistFans(String artist) {
			return new RadioStation("lastfm://artist/" + artist + "/fans");
		}

		public static RadioStation tagged(String tag) {
			return new RadioStation("lastfm://tag/" + tag);
		}

		public static RadioStation personal(String user) {
			return new RadioStation("lastfm://user/" + user + "/personal");
		}

		public static RadioStation lovedTracks(String user) {
			return new RadioStation("lastfm://user/" + user + "/loved");
		}

		public static RadioStation neighbours(String user) {
			return new RadioStation("lastfm://user/" + user + "/neighbours");
		}

		public static RadioStation recommended(String user) {
			return new RadioStation("lastfm://user/" + user + "/recommended");
		}
	}
}
