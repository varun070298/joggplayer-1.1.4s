/*
 *  $Source: /cvsroot2/open/projects/jOggPlayer/ca/bc/webarts/widgets/PlayList.java,v $
 *  $Name:  $
 *  $Revision: 1.8 $
 *  $Date: 2008-10-13 18:01:39 -0700 (Mon, 13 Oct 2008) $
 *  $Locker:  $
 */
/*
 *  PlayList -- A helper widget class used with the jOggPlayer.
 *
 *  Written by Tom Gutwin - WebARTS Design.
 *  Copyright (C) 2001 WebARTS Design, North Vancouver Canada
 *  http://www..webarts.bc.ca/jOggPlayer
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package ca.bc.webarts.widgets;

import ca.bc.webarts.widgets.Util;
import ca.bc.webarts.widgets.VorbisInfo;
import ca.bc.webarts.tools.JOrbisComment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;
import javax.swing.JCheckBox;

import javax.swing.JPanel;


/**
 *  A simple class to encapsulate the storage of references to Ogg files in a
 *  Playlist style.
 *
 * @author     tgutwin
 * @created    April 4, 2002
 */
public class PlayList
{

  /**  Description of the Field */
  private static final String SYSTEM_FILE_SEPERATOR = File.separator;
  /**  This is the meat of the class; it holds OggFileRef objects.* */
  private Vector songs = null;


  /**  basic constructor to initialize things. * */
  public PlayList()
  {
    songs = new Vector();
  }


  /**
   *  Constructor to get things init and load up an initial list of URLs *
   *
   * @param  urls  Description of the Parameter
   */
  public PlayList( URL[] urls )
  {
    songs = new Vector();
//      System.out.println("*** Creating a NEW PlayList "+urls.length);
    if ( urls != null )
    {
      for ( int i = 0; i < urls.length; i++ )
      {
        songs.add( new OggFileRef( urls[i] ) );
      }
    }
  }


  /**
   *  Parses the passed filenames looking for OGG files. It will recurse
   *  directories if a dir is in the passed array.
   *
   * @param  fileNames  an array containg files and/or dir names to parse for
   *      OGG files.
   * @return            an array of OGG files that have been found within the
   *      passed in array of filenames.
   */
  public static String[] getPlaylistArray( String[] fileNames )
  {
    String[] retVal = null;
    if ( fileNames != null )
    {
      Vector v = getPlaylistVector( fileNames );
      int size = v.size();
      retVal = new String[size];
//      System.out.println("Loading PlayList... "+size);
      if ( size > 0 )
      {
        for ( int i = 0; i < size; i++ )
        {
          retVal[i] = v.get( i ).toString();
        }
      }
    }
    return retVal;
  }


  /**
   *  Parses the passed filenames looking for OGG files. It will recurse
   *  directories if a dir is in the passed array.
   *
   * @param  fileNames  an array containg files and/or dir names to parse for
   *      OGG files.
   * @return            a vector of OGG files that have been found within the
   *      passed in array of filenames.
   */
  public static Vector getPlaylistVector( String[] fileNames )
  {
    Vector retVal = new Vector();
    File f;
    if ( fileNames != null )
    {
//      System.out.println("Loading Vector... "+fileNames.length);
      for ( int i = 0; i < fileNames.length; i++ )
      {
        f = new File( fileNames[i].trim() );
        //System.out.print(" "+i+"> " +fileNames[i]);
        if ( f.isDirectory() )
        {
          // && checkBoxRecursePlaylist.isSelected() )

          // fill from dir
          File oggDir = new File( fileNames[i] );
//          playListDir_ = fileNames[i];
          String[] oggDirFiles = oggDir.list();
          for ( int j = 0; j < oggDirFiles.length; j++ )
          {
            oggDirFiles[j] = fileNames[i].trim() +
                ( fileNames[i].endsWith( SYSTEM_FILE_SEPERATOR ) ?
                "" : SYSTEM_FILE_SEPERATOR ) +
                oggDirFiles[j];
          }
          // recurse on the dir
          //System.out.println(" is a Directory");
          retVal.addAll( getPlaylistVector( oggDirFiles ) );
        }
        else if ( fileNames[i].trim().toUpperCase().endsWith( ".OGG" ) )
        {
          //System.out.println(" is a File");
          retVal.addElement( fileNames[i] );
        }
        else if ( fileNames[i].trim().toUpperCase().endsWith( ".M3U" ) ||
            fileNames[i].trim().toUpperCase().endsWith( ".PLS" ) )
        {
          //System.out.println(" is a Playlist");
          retVal.addAll( getPlaylistVector( Util.getFileBaseURL( fileNames[i] ) ) );
        }
      }
    }
    return retVal;
  }


  /**
   *  Parses the passed URL as a plylist looking for OGG files.
   *
   * @param  playlistURL  a URL pointing at a playlist file.
   * @return              a vector of OGG files that have been found within the
   *      passed in URL.
   */
  public static Vector getPlaylistVector( URL playlistURL )
  {
    Vector retVal = new Vector();

    InputStream is = null;
    if ( playlistURL != null )
    {
      /*
       *  get the file ready to read
       */
      try
      {
        // load it as a URL

        URLConnection urlc = playlistURL.openConnection();
        // throws IOException
        is = urlc.getInputStream();

        /*
         *  Now load the songs listed
         */
        String line = null;
        int indx = -1;
        // check each time if the is has not disappeared
        while ( is != null &&
            ( line = readline( is ) ) != null )
        {
          if ( line.indexOf( "#EXTM3U" ) == -1 )
          {
            try
            {
              indx = line.indexOf( "=" );
              if ( indx != -1 )
              {
                retVal.add( new URL( line.substring( indx + 1 ) ) );
              }
              else
              {// just assume its a URL all on its own
                retVal.add( new URL( line ) );
              }
            }
            catch ( MalformedURLException ee )
            {
              // NOTHING JUST GO TO NEXT to save load time
            }
          }
        }
      }
      catch ( MalformedURLException ee )
      {
        System.out.println( "The Playlist Filename has some errors. " +
            "Please check the filename." );
      }
      catch ( IOException ee )
      {
        System.out.println( "The Playlist Filename cannot be read." );
      }
    }
    if (is != null)
      try{is.close();}catch(IOException ioEx){}

    return retVal;
  }


  /**
   *  Reads a single line from the InputStream.
   *
   * @param  is  Description of the Parameter
   * @return     the line read as a String
   */
  private static String readline( InputStream is )
  {
    StringBuffer rtn = new StringBuffer();
    int temp;
    do
    {
      try
      {
        temp = is.read();
      }
      catch ( Exception e )
      {
        return ( null );
      }
      if ( temp == -1 )
      {
        return ( null );
      }
      if ( temp != 0 && temp != '\n' )
      {
        rtn.append( (char)temp );
      }
    } while ( temp != '\n' );
    return ( rtn.toString() );
  }


  /**
   *  Gets the songNamesVector attribute of the PlayList object
   *
   * @return    The songNamesVector value
   */
  public Vector getSongNamesVector()
  {
    int size = songs.size();
    Vector retVal = new Vector();
//      System.out.println("SongNamesVectorSize="+size);
    for ( int i = 0; i < size; i++ )
    {
      retVal.add( ( (OggFileRef)( songs.get( i ) ) ).name_ );
    }
    return retVal;
  }


  /**
   *  Gets the songNames attribute of the PlayList object
   *
   * @return    The songNames value
   */
  public String[] getSongFileNames()
  {
    int size = songs.size();
    String[] retVal = new String[size];
    for ( int i = 0; i < size; i++ )
    {
      retVal[i] = ( (OggFileRef)( songs.get( i ) ) ).name_;
    }
    return retVal;
  }


  /**
   *  Gets an array of the song titles.
   *
   * @return    The song title values in an array
   */
  public String[] getSongTitles()
  {
    int size = songs.size();
    String[] retVal = new String[size];
    for ( int i = 0; i < size; i++ )
    {
      retVal[i] = ( (OggFileRef)( songs.get( i ) ) ).getTitle();
    }
    return retVal;
  }


  /**
   *  Gets the song Name Comment attribute of the selected Song object or the filename
   *
   * @param  songIndex  The index for the songname to retrieve
   * @return            The title comment value or filename if null
   */
  public String getSongName( int songIndex )
  {
    String retVal = ( (OggFileRef)( songs.get( songIndex ) ) ).getTitle();
    if (retVal.equals("")) retVal = ( (OggFileRef)( songs.get( songIndex ) ) ).name_;
    return retVal;
  }


  /**
   *  Gets the song file Name attribute of the selected Song object
   *
   * @param  songIndex  The index for the songname to retrieve
   * @return            The songName value
   */
  public String getSongFileName( int songIndex )
  {
    String retVal = "";
    retVal = ( (OggFileRef)( songs.get( songIndex ) ) ).name_;
    return retVal;
  }


  /**
   *  Sets the song Name comment of the selected Song object
   *
   * @param  songIndex  The index for the songname to retrieve
   * @param  name    The name to set
   * @return            The songName value
   */
  public void setSongName( int songIndex, String name )
  {
    setTitle(songIndex, name );
  }


  /**
   *  Sets the song Name comment of the selected Song object
   *
   * @param  songIndex  The index for the songname to retrieve
   * @param  name    The name to set
   * @return            The songName value
   */
  public void setTitle( int songIndex, String name )
  {
    ( (OggFileRef)( songs.get( songIndex ) ) ).setTitle(name);
  }


  /**
   *  Sets the song Artist comment of the selected Song object
   *
   * @param  songIndex  The index for the songname to retrieve
   * @param  name    The name to set
   * @return            The songName value
   */
  public void setArtist( int songIndex, String name )
  {
    ( (OggFileRef)( songs.get( songIndex ) ) ).setArtist(name);
  }


  /**
   *  Gets the URL attribute of the selected Song object
   *
   * @param  songIndex  The index for the song to retrieve
   * @return            The URL for the spec'd song value
   */
  public URL getSongUrl( int songIndex )
  {
    URL retVal = null;
    retVal = ( (OggFileRef)( songs.get( songIndex ) ) ).url_;
    return retVal;
  }


  /**
   *  Gets the songUrls attribute of the PlayList object
   *
   * @return    The songUrls value
   */
  protected URL[] getSongUrls()
  {
    int size = songs.size();

    URL[] retVal = new URL[size];
    for ( int i = 0; i < size; i++ )
    {
      retVal[i] = getSongUrl(i);
    }
    return retVal;
  }


  /**
   *  Gets the uRL attribute of the PlayList object
   *
   * @param  songName  Description of the Parameter
   * @return           The uRL value
   */
  public URL getURL( String songName )
  {
    URL retVal = null;
    if ( songName != null )
    {
      for ( int i = 0; i < songs.size(); i++ )
      {
        if ( ( (OggFileRef)( songs.get( i ) ) ).name_.equals( songName ) )
        {
          retVal = ( (OggFileRef)songs.get( i ) ).url_;
          break;
        }
      }
    }
    return retVal;
  }


  /**  Sorts the Playlist Alphabetically. */
  public void sortSongs()
  {
    sortSongsByFilename();
  }


  /**  Sorts the Playlist Alphabetically by its filenames. */
  public void sortSongsByFilename()
  {
    Object [] songsArray = getSongFileNames();
    // sorting alphabetically
    Arrays.sort(songsArray, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                return (((String) o1).toLowerCase().
                        compareTo(((String) o2).toLowerCase()));
            }
        });

    Vector sortedSongs = new Vector(songsArray.length);
    for (int i = 0; i < songsArray.length; i++)
    {
      sortedSongs.add(i, songs.get(findSong((String)songsArray[i])));
    }
    songs = null;
    songs = sortedSongs;
  }


  /**  Sorts the Playlist Alphabetically by its song titles. */
  public void sortSongsBySongTitle()
  {
    Object [] songsTitlesArray = getSongTitles();
    // sorting alphabetically
    Arrays.sort(songsTitlesArray, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                return (((String) o1).toLowerCase().
                        compareTo(((String) o2).toLowerCase()));
            }
        });

    Vector sortedSongs = new Vector(songsTitlesArray.length);
    for (int i = 0; i < songsTitlesArray.length; i++)
    {
      sortedSongs.add(i, songs.get(findSongByTitle((String)songsTitlesArray[i])));
    }
    songs = null;
    songs = sortedSongs;
  }


  /**  Sorts the Playlist Alphabetically by its artist (NOT IMPLEMENTED YET). */
  public void sortSongsByArtist()
  {
  }


  /**
   *  Finds a song in the playlist and returns the index to it.
   *
   * @param  songName  The Songname to look for
   * @return           the index postion of the found song in the playlist (-1
   *      if not found)
   */
  public int findSong( String songName )
  {
    int retVal = -1;
    for ( int i = 0; i < songs.size(); i++ )
    {
      if ( ( (OggFileRef)songs.get( i ) ).name_.equals( songName ) )
      {
        return i;
      }
    }
    return retVal;
  }


  /**
   *  Finds a song in the playlist by its song title and returns the index to it.
   *
   * @param  songName  The Songname to look for
   * @return           the index postion of the found song in the playlist (-1
   *      if not found)
   */
  public int findSongByTitle( String songTitle )
  {
    int retVal = -1;
    for ( int i = 0; i < songs.size(); i++ )
    {
      if ( ( (OggFileRef)songs.get( i ) ).getTitle().equals( songTitle ) )
      {
        return i;
      }
    }
    return retVal;
  }


  /**
   *  Finds a song in the playlist and returns the index to it.
   *
   * @param  songUrl  Description of the Parameter
   * @return          the index postion of the found song in the playlist (-1 if
   *      not found)
   */
  public int findSong( URL songUrl )
  {
    int retVal = -1;
    for ( int i = 0; i < songs.size(); i++ )
    {
      if ( ( (OggFileRef)songs.get( i ) ).url_.equals( songUrl ) )
      {
        return i;
      }
    }
    return retVal;
  }


  /**
   *  Tells you if the Playlist contains a certain song.
   *
   * @param  songName  The Songname to look for
   * @return           True if the song was found
   */
  public boolean contains( String songName )
  {
    boolean retVal = false;
    int indx = findSong( songName );
    if ( indx >= 0 )
    {
      retVal = true;
    }
    return retVal;
  }


  /**
   *  Checks to see if the list of songs in this playlist is empty.
   *
   * @return    true if the list of songs in this playlist is empty, false if
   *      not.
   */
  public boolean isEmpty()
  {
    return songs.isEmpty();
  }


  /**
   *  Adds the specified song to the playlist.
   *
   * @param  url  the specification for the song to add.
   * @return      the OggFileRef name of the added song.
   */
  public String addSong( URL url )
  {
    OggFileRef oggFileRef = new OggFileRef( url );
    songs.add( oggFileRef );
    return oggFileRef.name_;
  }


  /**
   *  Creates and returns a small JCheckBox with the selected song name and a
   *  small checkbox indicating if the song is selected.
   *
   * @param  songName  Description of the Parameter
   * @return           a JCheckBox containing a checkbox and Name of the song.
   */
  public JCheckBox createSongCheckBox( String songName )
  {
    return createSongCheckBox( findSong( songName ) );
  }


  /**
   *  Creates and returns a small JCheckBox with the selected song name and a
   *  small checkbox indicating if the song is selected.
   *
   * @param  index  the song num to look up
   * @return        a JCheckBox containing a checkbox and Name of the song.
   */
  public JCheckBox createSongCheckBox( int index )
  {
    OggFileRef oggRef = ( (OggFileRef)songs.get( index ) );
    JCheckBox jc = new JCheckBox( "uninit", false );
    if ( oggRef != null )
    {
      jc.setText( oggRef.name_ );
      jc.setSelected( oggRef.isSongSelected() );
    }
    return jc;
  }


  /**
   *  Creates and returns a small JPanel with the selected song name and a small
   *  checkbox indicating if the song is selected. This Panel is suitable to use
   *  a as a JComboBox Item.
   *
   * @param  index  the song num to look up
   * @return        a JPanel containing a checkbox and Name of the song.
   */
  public JPanel createSongPanel( int index )
  {
    OggFileRef oggRef = ( (OggFileRef)songs.get( index ) );
    JPanel retVal = new JPanel();
    retVal.add( new JCheckBox( oggRef.name_, oggRef.isSongSelected() ) );
    return retVal;
  }


  /**
   *  Creates and returns a small JPanel with the selected song name and a small
   *  checkbox indicating if the song is selected. This Panel is suitable to use
   *  a as a JComboBox Item.
   *
   * @param  song  the song name to look up
   * @return       a JPanel containing a checkbox and Name of the song.
   */
  public JPanel createSongPanel( String song )
  {
    OggFileRef oggRef = ( (OggFileRef)songs.get( findSong( song ) ) );
    JPanel retVal = new JPanel();
    retVal.add( new JCheckBox( oggRef.name_, oggRef.isSongSelected() ) );
    return retVal;
  }


  /**
   *  Parses the passed filename looking for OGG files. It will recurse
   *  directories if a dir is in the passed String.
   *
   * @param  fileName  an String (a file or dir name) to parse for OGG files.
   * @return           a vector of OGG files that have been found within the
   *      passed in String.
   */
  public Vector getPlaylistVector( String fileName )
  {
    Vector retVal = new Vector();
    String[] temp = {fileName};
    if ( fileName != null )
    {
      retVal = getPlaylistVector( temp );
    }
    return retVal;
  }


  /**
   *  The number of entries in the PlayList.
   *
   * @return    The number of entries in the PlayList.
   */
  public int size()
  {
    int retVal = 0;
    if ( songs != null )
    {
      retVal = songs.size();
    }
    return retVal;
  }


    /**
   *  Selects ALL OggFileRef
   *
   */
  public void selectAllSongs( )
  {
    int i;
    for ( i = 0; i < size(); i++ )
    {
      ( (OggFileRef)songs.get( i ) ).setSelected( true );
    }
  }


    /**
   *  De-Selects ALL OggFileRef
   *
   */
  public void deSelectAllSongs( )
  {
    int i;
    for ( i = 0; i < size(); i++ )
    {
      ( (OggFileRef)songs.get( i ) ).setSelected( false );
    }
  }


  /**
   *  De-Selects the spec'd OggFileRef
   *
   * @param  songIndex  The song to select
   * @param  select     for select or de-select
   */
  public void deSelectSong( int songIndex, boolean select )
  {
    ( (OggFileRef)songs.get( songIndex ) ).setSelected( select );
  }


  /**
   *  Marks the OggFileRef as selected
   *
   * @param  songIndex  The song to select
   * @param  select     for select or de-select
   */
  public void selectSong( int songIndex, boolean select )
  {
    ( (OggFileRef)songs.get( songIndex ) ).setSelected( select );
  }


  /**
   *  Gets the information from an Ogg Vorbis reference in a nice, humanly-readable format.
   *
   * @param  songIndex  The song to select
   * @return the information from an Ogg Vorbis stream in a nice, humanly-readable format.
   */
  public String getSongInfo( int songIndex )
  {
    return ( (OggFileRef)songs.get( songIndex ) ).toString();
  }


  /**
   *  Gets the Channels information from an Ogg Vorbis reference.
   *
   * @param  songIndex  The song to select
   * @return the information from an Ogg Vorbis stream in a nice, humanly-readable format.
   */
  public int getSongChannels( int songIndex )
  {
    return ( (OggFileRef)songs.get( songIndex ) ).getChannels();
  }


  /**
   *  Gets the Average Bitrate information from an Ogg Vorbis reference.
   *
   * @param  songIndex  The song to select
   * @return the information from an Ogg Vorbis stream in a nice, humanly-readable format.
   */
  public long getSongBitrate( int songIndex )
  {
    return ( (OggFileRef)songs.get( songIndex ) ).getBitrate();
  }


  /**
   *  Gets the ALBUM information from an Ogg Vorbis reference.
   *
   * @param  songIndex  The song to select
   * @return the information from an Ogg Vorbis stream in a nice, humanly-readable format.
   */
  public String getSongAlbum( int songIndex )
  {
    return ( (OggFileRef)songs.get( songIndex ) ).getAlbum();
  }


  /**
   *  Gets the ARTIST information from an Ogg Vorbis reference.
   *
   * @param  songIndex  The song to select
   * @return the information from an Ogg Vorbis stream in a nice, humanly-readable format.
   */
  public String getSongArtist( int songIndex )
  {
    return ( (OggFileRef)songs.get( songIndex ) ).getArtist();
  }


  /**
   *  Gets the TrackNumber information from an Ogg Vorbis reference.
   *
   * @param  songIndex  The song to select
   * @return the information from an Ogg Vorbis stream in a nice, humanly-readable format.
   */
  public int getTrackNumber( int songIndex )
  {
    return ( (OggFileRef)songs.get( songIndex ) ).getTrackNumber();
  }


  /**
   *  Gets the TITLE information from an Ogg Vorbis reference.
   *
   * @param  songIndex  The song to select
   * @return the information from an Ogg Vorbis stream in a nice, humanly-readable format.
   */
  public String getSongTitle( int songIndex )
  {
    return ( (OggFileRef)songs.get( songIndex ) ).getTitle();
  }


  /**
   *  Gets the songSelected attribute of the PlayList object
   *
   * @param  songName  Description of the Parameter
   * @return           The songSelected value
   */
  public boolean isSongSelected( String songName )
  {
    return isSongSelected( findSong( songName ) );
  }


  /**
   *  Gets the songSelected attribute of the PlayList object
   *
   * @param  songIndex  Description of the Parameter
   * @return            The songSelected value
   */
  public boolean isSongSelected( int songIndex )
  {
    return ( (OggFileRef)songs.get( songIndex ) ).isSongSelected();
  }


  /**
   *  Loads a playlist from the playlist file specified in the playListFilename_
   *  field. It first trys to load each line as a URL; if that fails it loads
   *  them as files. It simply adds the songs listed to the playList.
   *
   * @param  filenameURL  the filename to use as the playlist - specified as a
   *      URL
   */
  public void loadPlaylist( URL filenameURL )
  {
    InputStream is = null;
    if ( filenameURL != null )
    {
      /*
       *  get the file ready to read
       */
      try
      {
        // load it as a URL

        URLConnection urlc = filenameURL.openConnection();
        // throws IOException
        is = urlc.getInputStream();

        /*
         *  Now load the songs listed
         */
        String line = null;
        int indx = -1;
        // check each time if the is has not disappeared
        while ( is != null && ( line = readline( is ) ) != null )
        {
          try
          {
            indx = line.indexOf( "=" );
            if ( indx != -1 )
            {
              addSong( new URL( line.substring( indx + 1 ) ) );
            }
            else
            {// just assume its a URL all on its own
              addSong( new URL( line ) );
            }
          }
          catch ( MalformedURLException ee )
          {
            // NOTHING JUST GO TO NEXT to save load time
          }
        }
      }
      catch ( MalformedURLException ee )
      {
        System.out.println( "The Playlist Filename has some errors. " +
            "Please check the filename." );
      }
      catch ( IOException ee )
      {
        System.out.println( "The Playlist Filename cannot be read." );
      }
    }
    if (is != null)
      try{is.close();}catch(IOException ioEx){}
  }



  /**
   *  Saves the playlist to the file specified in the playListFilename_ field.
   *  <PRE>[playlist]
   * File1=H:\MP3s\U2\October\02 - I Fall Down.mp3
   * File2=H:\MP3s\Leonard Cohen\01 - Suzanne.mp3
   * NumberOfEntries=2
   * </PRE>
   *
   * @param  filename  Description of the Parameter
   */
  public void savePlaylistFile( String filename )
  {
    try
    {
      FileWriter out = new FileWriter( filename, false );
      String currUrlStr = "";
      URL[] playUrls = getSongUrls();
      out.write( "[playlist]" +
          System.getProperty( "line.separator", "\n" ) );
      String tempNumStr = "";
      int i;
      for ( i = 0; i < playUrls.length; i++ )
      {
        tempNumStr = "File" + ( i + 1 ) + "=";
        currUrlStr = playUrls[i].toString();
        out.write( tempNumStr + currUrlStr +
            System.getProperty( "line.separator", "\n" ) );
      }
      out.write( "NumberOfEntries=" + i );
      out.close();
    }
    catch ( IOException ex )
    {
      System.out.println( "Could Not Save To Playlist File " + filename );
    }
  }


  /**
   *  Saves the favourites selected in the playlist to the file specified in the playListFilename_ field.
   *  <PRE>[playlist]
   * File1=H:\MP3s\U2\October\02 - I Fall Down.mp3
   * File2=H:\MP3s\Leonard Cohen\01 - Suzanne.mp3
   * NumberOfEntries=2
   * </PRE>
   *
   * @param  filename  Description of the Parameter
   */
  public void saveFavourites(String filename )
  {
    try
    {
      FileWriter out = new FileWriter( filename, false );
      String currUrlStr = "";
      URL[] playUrls = getSongUrls();
      out.write( "[playlist]" +
          System.getProperty( "line.separator", "\n" ) );
      String tempNumStr = "";
      int i;
      int selectedSongs = 0;
      for ( i = 0; i < playUrls.length; i++ )
      {
        if (isSongSelected(i))
        {
          tempNumStr = "File" + ( selectedSongs + 1 ) + "=";
          currUrlStr = playUrls[i].toString();
          out.write( tempNumStr + currUrlStr +
              System.getProperty( "line.separator", "\n" ) );
          selectedSongs++;
        }
      }
      out.write( "NumberOfEntries=" + selectedSongs );
      out.close();
    }
    catch ( IOException ex )
    {
      System.out.println( "Could Not Save To Playlist File " + filename );
    }
  }


  /**
   *  Description of the Method
   *
   * @param  songName  Description of the Parameter
   * @return           Description of the Return Value
   */
  public boolean removeSong( String songName )
  {
    boolean retVal = false;
    int indx = findSong( songName );
    if ( indx >= 0 )
    {
      songs.removeElementAt( indx );
      retVal = true;
    }
    return retVal;
  }
}


/**
 *  A simple class to encapsulate the information required to use/reference an
 *  Ogg file.
 *
 * @author     unknown
 * @created    April 4, 2002
 */
class OggFileRef
{
  /**  File Name of the song. */
  public String name_ = "";

  /**  The URL for the song resource. */
  public URL url_ = null;

  /**  Select This song for playing. */
  private boolean selected_ = true;

  /**  Select This song for playing. */
  private VorbisInfo vorbisInfo_ = null;
  /**  Select This song for playing. */
  private JOrbisComment jOrbisComment_ = null;

  /**
   *  Constructor for the OggFileRef object
   *
   * @param  newUrl  Description of the Parameter
   */
  OggFileRef( URL newUrl )
  {
    url_ = newUrl;
    String urlStr = url_.toString();
    name_ =urlStr.substring( urlStr.lastIndexOf( '/' ) + 1 );
  }

  /** Instantiates the JOrbisCommentObject **/
  private void initVorbisComment()
  {
    if (jOrbisComment_ == null)
      try
      {
        //System.err.print("\nvorbisURL("+url_+") is "+(url_==null?"":"NOT ")+" null");
        URLConnection conn = url_.openConnection();
        if (url_.getProtocol().equals("file"))
        {
          File f = new File(url_.getFile());
          if (f.canRead())
          {
            jOrbisComment_ = new JOrbisComment(f);
            //jOrbisComment_.read();
          }
          else
          {
            System.err.println("Cant Access File: "+url_.getFile());
            vorbisInfo_ = null;
            jOrbisComment_ = null;
           }
        }
        else
        {
            jOrbisComment_ = new JOrbisComment(url_.openStream());
            jOrbisComment_.read();
        }
      }
      catch (Exception ex)
      {
        //System.err.println("vorbisURL is "+(url_==null?"":"NOT ")+" null");
        vorbisInfo_ = null;
        jOrbisComment_ = null;
        //ex.printStackTrace();
      }
  }

  /**
   *  Gets the songSelected attribute of the OggFileRef object
   *
   * @return    The songSelected value
   */
  public boolean isSongSelected()
  {
    return selected_;
  }


  /**
   *  Gets the average bitrate for this ogg vorbis file.
   *
   * @return    the average bitrate
   */
  public long getBitrate()
  {
    long retVal = 0l;
    if ( vorbisInfo_ != null)
      retVal = vorbisInfo_.getBitrate();
    return retVal;
  }


  /**
   *  Gets the numer of Channels for this ogg vorbis file.
   *
   * @return    the average bitrate
   */
  public int getChannels()
  {
    int retVal = 0;
    if ( vorbisInfo_ != null)
      retVal = vorbisInfo_.getChannels();
    return retVal;
  }


  /**
   *  Sets the Artist Comment for this ogg vorbis file.
   *
   * @value String  holding the current track artist
   */
  public boolean setArtist(String value)
  {
    initVorbisComment();
    return (jOrbisComment_!=null?jOrbisComment_.setArtist(value):false);
  }


  /**
   *  Sets the Album Comment for this ogg vorbis file.
   *
   * @value String  holding the current track Album
   */
  public boolean setAlbum(String value)
  {
    initVorbisComment();
    return (jOrbisComment_!=null?jOrbisComment_.setAlbum(value):false);
  }


  /**
   *  Sets the Title Comment for this ogg vorbis file.
   *
   * @value String  holding the current track Title
   */
  public boolean setTitle(String value)
  {
    initVorbisComment();
    return (jOrbisComment_!=null?jOrbisComment_.setTitle(value):false);
  }


  /**
   *  Sets the Track num for this ogg vorbis file.
   *
   * @value String  holding the current track num
   */
  public boolean setTracknumber(String value)
  {
    initVorbisComment();
    return (jOrbisComment_!=null?jOrbisComment_.setTracknumber(value):false);
  }


  /**
   *  Sets the Track total for this ogg vorbis file.
   *
   * @value String  holding the current track total
   */
  public boolean setTrackTotal(String value)
  {
    initVorbisComment();
    return (jOrbisComment_!=null?jOrbisComment_.setTrackTotal(value):false);
  }


  /**
   *  Sets the genre  num for this ogg vorbis file.
   *
   * @value String  holding the current genre
   */
  public boolean setGenre(String value)
  {
    initVorbisComment();
    return (jOrbisComment_!=null?jOrbisComment_.setGenre(value):false);
  }


  /**
   *  Gets the Artist Comment for this ogg vorbis file.
   *
   * @return    comment holding the current track artist
   */
  public String getArtist()
  {
    /*String retVal = "";
    if ( vorbisInfo_ != null)
    {
      Vector v = vorbisInfo_.getComments("ARTIST");
      for (int i = 0; v != null && i < v.size(); i++)`
      {
        retVal += (String) v.get(i);
        if (i+1 < v.size())
          retVal += Util.SYSTEM_LINE_SEPERATOR;
      }
    }
    return retVal; */
    initVorbisComment();
    return (jOrbisComment_!=null?jOrbisComment_.getArtist():"");
  }


  /**
   *  Gets the Song Title Comment for this ogg vorbis file.
   *
   * @return   comment holding the current track title
   */
  public String getTitle()
  {
    /*String retVal = "";
    if ( vorbisInfo_ != null)
    {
      Vector v = vorbisInfo_.getComments("TITLE");
      for (int i = 0; v != null && i < v.size(); i++)
      {
        retVal += (String) v.get(i);
        if (i+1 < v.size())
          retVal += Util.SYSTEM_LINE_SEPERATOR;
      }
    }
    return retVal;*/
    initVorbisComment();
    return (jOrbisComment_!=null?jOrbisComment_.getTitle():"");
  }


  /**
   *  Gets the ALBUM Comment for this ogg vorbis file.
   *
   * @return    comment holding the current track albumname
   */
  public String getAlbum()
  {
    /*String retVal = "";
    if ( vorbisInfo_ != null)
    {
      Vector v = vorbisInfo_.getComments("ALBUM");
      for (int i = 0; v != null && i < v.size(); i++)
      {
        retVal += (String) v.get(i);
        if (i+1 < v.size())
          retVal += Util.SYSTEM_LINE_SEPERATOR;
      }
    }
    return retVal;*/
    initVorbisComment();
    return (jOrbisComment_!=null?jOrbisComment_.getAlbum():"");
  }


  /**
   *  Gets the TRACKNUMBER Comment for this ogg vorbis file.
   *
   * @return    the number or -1 if not spec'd
   */
  public int getTrackNumber()
  {
    /*int retVal = -1;
    if ( vorbisInfo_ != null)
    {
      Vector v = vorbisInfo_.getComments("TRACKNUMBER");
      if ( v!=null && v.size()>0 )
        retVal = Integer.parseInt((String) v.get(v.size()-1));
    }
    return retVal;*/
    int retVal = -1;
    initVorbisComment();
    String trNum = jOrbisComment_.getTracknumber();

    return (jOrbisComment_!=null&&!jOrbisComment_.getTracknumber().equals("")
            ?Integer.parseInt(jOrbisComment_.getTracknumber())
            :-1);
  }


  /**
   *  Sets the selected attribute of the OggFileRef object
   *
   * @param  s  The new selected value
   */
  public void setSelected( boolean s )
  {
    selected_ = s;
  }


  /**
   * Prints out the information from an Ogg Vorbis stream in a
   * nice, humanly-readable format.
   */
  public String toString() {

    String retVal = url_+"\n";

    if ( vorbisInfo_ != null)
      retVal += vorbisInfo_.toString();

    return retVal;
  }


}
/*
 *  Here is the revision log
 *  ------------------------
 *  $Log: PlayList.java,v $
 *  Revision 1.8  2004/11/14 04:46:46  tgutwin
 *  Changed the getSongNames method to getSongFileNames to be more accurate.
 *  Added the sortSongs methods.
 *
 *  Revision 1.6  2002/04/23 22:22:34  tgutwin
 *  Added the ability to add the songs from a playlist if passed the name of the playlist.
 *
 *  Revision 1.5  2002/04/11 23:15:37  anonymous
 *  Added a couple methods for a jCheckboxDropdown bug fix.
 *
 *  Revision 1.4  2002/04/06 06:03:07  anonymous
 *  small change to loading of a playlist file URL
 *
 *  Revision 1.3  2002/04/05 23:05:53  anonymous
 *  Bug Fixes.
 *
 *  Revision 1.2  2001/09/02 03:24:23  tgutwin
 *  Initial Rev of my custom DropDown Boxes.
 *  Revision 1.1  2001/08/12 06:00:01  tgutwin
 *  Intial Revision. Includes OGG files Only.
 */

