package com.qxgcloud.rpc.common.zk.inet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InetDevice {
  private static final Logger logger = LoggerFactory.getLogger(InetDevice.class);

  public static InetAddress getInetAddressForLocal() {
    try {
      return InetAddress.getLocalHost();
    } catch (UnknownHostException e) {
      logger.error(e.getMessage());
    }
    return null;
  }

  public static InetAddress getInetAddressDefault() {
    return getInetAddressForLocal();
  }

  public static InetAddress getInetAddressForVMNet8() {
    try {
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      while (interfaces.hasMoreElements()) {
        NetworkInterface ni = interfaces.nextElement();
        String deviceName = ni.getDisplayName();
        if (deviceName != null && deviceName.contains("VMnet8")) {
          Enumeration<InetAddress> address = ni.getInetAddresses();
          while(address.hasMoreElements()){
            InetAddress element = address.nextElement();
            if (matchIPV4(element.getHostAddress())) {
              return element;
            }
          }
        }
      }
    } catch (SocketException e) {
      logger.error(e.getMessage());
    }
    return null;
  }

  private static Boolean matchIPV4(String address) {
    String pattern = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";
    Pattern r = Pattern.compile(pattern);
    Matcher matcher = r.matcher(address);
    return matcher.matches();
  }
}
