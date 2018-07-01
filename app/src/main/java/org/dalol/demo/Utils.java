package org.dalol.demo;

import android.net.Uri;
import android.text.TextUtils;

/**
 * @author Filippo
 * @version 1.0.0
 * @since Sun, 01/07/2018 at 09:18.
 */
public final class Utils {

    private Utils() {}

    public static boolean isValidVideoURL(String url) {
        boolean isValidURL = false;
        if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            isValidURL = uri.getHost().contains("youtube") && !TextUtils.isEmpty(uri.getQueryParameter("v"));
        }
        return isValidURL;
    }
}
