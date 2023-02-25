/* ----------------------------------------------------------------------------
   The Kiwi Toolkit
   Copyright (C) 1998-2003 Mark A. Lindner

   This file is part of Kiwi.
   
   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Library General Public
   License as published by the Free Software Foundation; either
   version 2 of the License, or (at your option) any later version.

   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Library General Public License for more details.

   You should have received a copy of the GNU Library General Public
   License along with this library; if not, write to the Free
   Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 
   The author may be contacted at:
   
   mark_a_lindner@yahoo.com
   ----------------------------------------------------------------------------
   $Log: AudioClip.java,v $
   Revision 1.5  2003/01/19 09:50:52  markl
   Javadoc & comment header updates.

   Revision 1.4  2001/03/12 09:27:51  markl
   Source code and Javadoc cleanup.

   Revision 1.3  1999/04/19 05:30:29  markl
   Added getName()/setName() methods.

   Revision 1.2  1999/01/10 01:00:58  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.net.URL;
import java.io.*;
import sun.audio.*;

/** This class represents an audio clip. The audio data is restricted to the
  * 8000Hz, single-channel u-law format. The class relies on the undocumented
  * <b>sun.audio</b> package and thus may not be portable.
  * <p>
  * <code>AudioClip</code>s may be read from streams, from files, or loaded as
  * system resources using a <code>ResourceManager</code> or
  * <code>ResourceLoader</code>.
  *
  * @see kiwi.util.ResourceManager#getSound
  * @see kiwi.util.ResourceLoader#getResourceAsURL
  *
  * @author Mark Lindner
  */

public class AudioClip implements java.applet.AudioClip
  {
  static int length;
  private AudioData audioData;
  private AudioDataStream audioStream = null;
  private ContinuousAudioDataStream cAudioStream = null;
  private String name = "Untitled";

  /** Construct a new <code>AudioClip</code>.
    *
    * @param url The location of the audio data.
    *
    * @exception java.io.IOException If there is a problem reading from the
    * specified URL.
    */

  public AudioClip(URL url) throws java.io.IOException
    {
    audioData = new AudioStream(url.openStream()).getData();
    }

  /** Construct a new <code>AudioClip</code>.
    *
    * @param filename The name of the file that contains the audio data.
    *
    * @exception java.io.IOException If there is a problem reading from the
    * specified file.
    */

  public AudioClip(String file) throws IOException
    {
    this(new FileInputStream(file));
    }

  /** Construct a new <code>AudioClip</code>.
    *
    * @param stream The stream to read the audio data from.
    *
    * @exception java.io.IOException If there is a problem reading from the
    * specified stream.
    */

  public AudioClip(InputStream stream) throws IOException
    {
    AudioStream audioStream = new AudioStream(stream);
    audioData = audioStream.getData();
    }

  /** Play the audio clip. */

  public void play()
    {
    audioStream = new AudioDataStream(audioData);
    AudioPlayer.player.start(audioStream);
    }

  /** Play the audio clip continuously. */

  public void loop()
    {
    cAudioStream = new ContinuousAudioDataStream(audioData);
    AudioPlayer.player.start(cAudioStream);
    }

  /** Stop playing the audio clip. */

  public void stop()
    {
    if(audioStream != null)
      AudioPlayer.player.stop(audioStream);
    if(cAudioStream != null)
      AudioPlayer.player.stop(cAudioStream);
    }

  /** Set the audio clip's name.
   *
   * @param name The name.
   */
  
  public void setName(String name)
    {
    if(name != null)
      this.name = name;
    }

  /** Get the audio clip's name.
   *
   * @return The name.
   */
  
  public String getName()
    {
    return(name);
    }

  /** Get a string representation of this object.
   *
   * @return The name of the audio clip.
   */
  
  public String toString()
    {
    return(getName());
    }

  }

/* end of source file */
