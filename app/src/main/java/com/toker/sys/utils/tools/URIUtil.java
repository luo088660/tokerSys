package com.toker.sys.utils.tools;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.net.*;


public class URIUtil {

    private static final String SCHEME_FILE = "file"; //$NON-NLS-1$
    private static final String UNC_PREFIX = "//"; //$NON-NLS-1$

   
    public static URI append(URI base, String extension) {
        try {
            String path = base.getPath();
            if (path == null)
                return appendOpaque(base, extension);
            //if the base is already a directory then resolve will just do the right thing
            if (path.endsWith("/")) {//$NON-NLS-1$
                URI result = base.resolve(extension);
                //Fix UNC paths that are incorrectly normalized by URI#resolve (see Java bug 4723726)
                String resultPath = result.getPath();
                if (path.startsWith(UNC_PREFIX) && (resultPath == null || !resultPath.startsWith(UNC_PREFIX)))
                    result = new URI(result.getScheme(), "///" + result.getSchemeSpecificPart(), result.getFragment()); //$NON-NLS-1$
                return result;
            }
            path = path + "/" + extension; //$NON-NLS-1$
            return new URI(base.getScheme(), base.getUserInfo(), base.getHost(), base.getPort(), path, base.getQuery(), base.getFragment());
        } catch (URISyntaxException e) {
            //shouldn't happen because we started from a valid URI
            throw new RuntimeException(e);
        }
    }

   
    private static URI appendOpaque(URI base, String extension) throws URISyntaxException {
        String ssp = base.getSchemeSpecificPart();
        if (ssp.endsWith("/")) //$NON-NLS-1$
            ssp += extension;
        else
            ssp = ssp + "/" + extension; //$NON-NLS-1$
        return new URI(base.getScheme(), ssp, base.getFragment());
    }

   
    public static URI fromString(String uriString) throws URISyntaxException {
        int colon = uriString.indexOf(':');
        int hash = uriString.lastIndexOf('#');
        boolean noHash = hash < 0;
        if (noHash)
            hash = uriString.length();
        String scheme = colon < 0 ? null : uriString.substring(0, colon);
        String ssp = uriString.substring(colon + 1, hash);
        String fragment = noHash ? null : uriString.substring(hash + 1);
        //use java.io.File for constructing file: URIs
        if (scheme != null && scheme.equals(SCHEME_FILE)) {
            File file = new File(uriString.substring(5));
            if (file.isAbsolute())
                return file.toURI();
            scheme = null;
            if (File.separatorChar != '/')
                ssp = ssp.replace(File.separatorChar, '/');
        }
        return new URI(scheme, ssp, fragment);
    }

   
    public static boolean sameURI(URI url1, URI url2) {
        if (url1 == url2)
            return true;
        if (url1 == null || url2 == null)
            return false;
        if (url1.equals(url2))
            return true;

        if (url1.isAbsolute() != url2.isAbsolute())
            return false;

        // check if we have two local file references that are case variants
        File file1 = toFile(url1);
        return file1 == null ? false : file1.equals(toFile(url2));
    }

   
    public static File toFile(URI uri) {
        try {
            if (!SCHEME_FILE.equalsIgnoreCase(uri.getScheme()))
                return null;
            //assume all illegal characters have been properly encoded, so use URI class to unencode
            return new File(uri);
        } catch (IllegalArgumentException e) {
            //File constructor does not support non-hierarchical URI
            String path = uri.getPath();
            //path is null for non-hierarchical URI such as file:c:/tmp
            if (path == null)
                path = uri.getSchemeSpecificPart();
            return new File(path);
        }
    }

   
    public static String toUnencodedString(URI uri) {
        StringBuffer result = new StringBuffer();
        String scheme = uri.getScheme();
        if (scheme != null)
            result.append(scheme).append(':');
        //there is always a ssp
        result.append(uri.getSchemeSpecificPart());
        String fragment = uri.getFragment();
        if (fragment != null)
            result.append('#').append(fragment);
        return result.toString();
    }

   
    public static URI toURI(URL url) throws URISyntaxException {
        //URL behaves differently across platforms so for file: URLs we parse from string form
        if (SCHEME_FILE.equals(url.getProtocol())) {
            String pathString = url.toExternalForm().substring(5);
            //ensure there is a leading slash to handle common malformed URLs such as file:c:/tmp
            if (pathString.indexOf('/') != 0)
                pathString = '/' + pathString;
            else if (pathString.startsWith(UNC_PREFIX) && !pathString.startsWith(UNC_PREFIX, 2)) {
                //URL encodes UNC path with two slashes, but URI uses four (see bug 207103)
                pathString = UNC_PREFIX + pathString;
            }
            return new URI(SCHEME_FILE, null, pathString, null);
        }
        try {      return new URI(url.toExternalForm());
        } catch (URISyntaxException e) {
            //try multi-argument URI constructor to perform encoding
            return new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        }
    }

    public static File uriToFile(Uri uri, Context context) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA }, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
            //Log.i(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }
    public static URL toURL(URI uri) throws MalformedURLException {
        return new URL(uri.toString());
    }
}