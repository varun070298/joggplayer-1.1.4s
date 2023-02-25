package ca.bc.webarts.widgets;

import java.net.Authenticator;
import java.net.PasswordAuthentication;


/**
 *  FirewallAuthenticator class *
 *
 * @author    tgutwin
 */
public class FirewallAuthenticator extends Authenticator
{
  public static final String DEFAULT_PROXY_SET = "true";
  public static final String DEFAULT_PROXY_HOST = "bctcproxy1";
  public static final String DEFAULT_PROXY_PORT = "80";
  public static final String DEFAULT_PROXY_NOPROXY = "";
  /**  The username to use for the proxy authentication. */
  String proxyUsername = "";
  /**  The password to use for the proxy authentication. */
  String proxyPassword = "";
  /**  The impl of the  PasswordAuthentication for the Authenticator. */
  PasswordAuthentication pw = new PasswordAuthentication(
      proxyUsername, proxyPassword.toCharArray() );


  /**
   *  Constructor for the FirewallAuthenticator object
   *
   * @param  pw  Description of the Parameter
   */
  public FirewallAuthenticator( PasswordAuthentication pw )
  {
    this.pw = pw;
  }


  /**
   *  Constructor for the FirewallAuthenticator object
   *
   * @param  proxyUser  Description of the Parameter
   * @param  proxyPass  Description of the Parameter
   */
  public FirewallAuthenticator( String proxyUser, String proxyPass )
  {
    if ( proxyUser != null && !proxyUser.equals( "" ) )
    {
      proxyUsername = proxyUser;
    }
    if ( proxyPass != null && !proxyPass.equals( "" ) )
    {
      proxyPassword = proxyPass;
    }
    this.pw = new PasswordAuthentication(
        proxyUsername, proxyPassword.toCharArray() );
  }


  /**
   *  Impl for Authenticator so an authorizing proxy will work.
   *
   * @return    The passwordAuthentication value
   */
  protected PasswordAuthentication getPasswordAuthentication()
  {
    return this.pw;
  }
}

