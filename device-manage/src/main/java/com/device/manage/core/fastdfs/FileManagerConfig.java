package com.device.manage.core.fastdfs;

import java.io.Serializable;

/**
 * @author Administrator
 */
public interface FileManagerConfig extends Serializable {

  public static final String FILE_DEFAULT_WIDTH 	= "120";
  public static final String FILE_DEFAULT_HEIGHT 	= "120";
  public static final String FILE_DEFAULT_AUTHOR 	= "root";
  
  public static final String PROTOCOL = "http://";
  public static final String SEPARATOR = "/";

  /**:*/
  public final static String COLON    = ":";
  
  public static final String TRACKER_NGNIX_PORT 	= "80";
  
  public static final String CLIENT_CONFIG_FILE   = "fdfs_client.conf";
  
  
}