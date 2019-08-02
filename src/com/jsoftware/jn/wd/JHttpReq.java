package com.jsoftware.jn.wd;

import android.util.Log;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JHttpReq
{

// ---------------------------------------------------------------------
  public static int httpreq(String req, String s, byte[] data, Object[] res) throws IOException
  {
    if (s.isEmpty()) return 1;
    URL url = new URL(s);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    if(req.equals("post")||req.equals("put")) {
      connection.setRequestMethod((req.equals("post"))?"POST":"PUT");
      connection.setDoInput(true);
      connection.setDoOutput(req=="post");
      OutputStream os = connection.getOutputStream();
      os.write(data);
    }
    if(req.equals("post")||req.equals("get")) {
      byte[] buffer = new byte[8192];
      InputStream is = connection.getInputStream();
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      for (int bytesRead; (bytesRead = is.read(buffer)) >= 0; )
        output.write(buffer, 0, bytesRead);
      res[0]= output.toByteArray();
      res[1]= new int[] {2,-1,-1};
      connection.disconnect();
      return -1;
    } else {
      connection.disconnect();
      return 0;
    }
  }
}
