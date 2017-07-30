package com.wdziemia.githubtimes.util;

/**
 * Helper class for pure Junit tests
 */
public class StringUtil
{
    public static boolean isEmpty(CharSequence string)
    {
        return string == null || string.length() == 0;
    }
}
