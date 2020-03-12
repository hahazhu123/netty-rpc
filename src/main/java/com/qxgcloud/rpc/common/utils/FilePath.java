package com.qxgcloud.rpc.common.utils;

import sun.security.action.GetPropertyAction;

import java.security.AccessController;

public class FilePath {
  public Character slash;

  public FilePath() {
    this.slash = AccessController.doPrivileged(
            new GetPropertyAction("file.separator")).charAt(0);
  }

  public FilePath(Character slash) {
    this.slash = slash;
  }

  public String resolve(String parent, String child) {
    int pn = parent.length();
    if (pn == 0) return child;
    int cn = child.length();
    if (cn == 0) return parent;

    String c = child;
    int childStart = 0;
    int parentEnd = pn;

    if ((cn > 1) && (c.charAt(0) == slash)) {
      if (c.charAt(1) == slash) {
        /* Drop prefix when child is a UNC pathname */
        childStart = 2;
      } else {
        /* Drop prefix when child is drive-relative */
        childStart = 1;

      }
      if (cn == childStart) { // Child is double slash
        if (parent.charAt(pn - 1) == slash)
          return parent.substring(0, pn - 1);
        return parent;
      }
    }

    if (parent.charAt(pn - 1) == slash)
      parentEnd--;

    int strlen = parentEnd + cn - childStart;
    char[] theChars = null;
    if (child.charAt(childStart) == slash) {
      theChars = new char[strlen];
      parent.getChars(0, parentEnd, theChars, 0);
      child.getChars(childStart, cn, theChars, parentEnd);
    } else {
      theChars = new char[strlen + 1];
      parent.getChars(0, parentEnd, theChars, 0);
      theChars[parentEnd] = slash;
      child.getChars(childStart, cn, theChars, parentEnd + 1);
    }
    return new String(theChars);
  }

  public static void main(String[] args) {
    FilePath path = new FilePath('/');
    String resolve = path.resolve("cccc", "dddd");
    System.out.println(resolve);
  }
}