package net.udrunk;

import java.net.ContentHandler;
import java.net.URLStreamHandlerFactory;

import net.udrunk.infra.JamendoCache;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.google.android.imageloader.BitmapContentHandler;
import com.google.android.imageloader.ImageLoader;

public class UdrunkApplication extends Application {

	private static final int IMAGE_TASK_LIMIT = 3;
	
	public ImageLoader imageLoader = createImageLoader(this);
	
	public static final String API_DOMAIN = "http://udrunk.valentinbourgoin.net";
	
	private static ImageLoader createImageLoader(Context context) {
        // Install the file cache (if it is not already installed)
        JamendoCache.install(context);
        
        // Just use the default URLStreamHandlerFactory because
        // it supports all of the required URI schemes (http).
        URLStreamHandlerFactory streamFactory = null;

        // Load images using a BitmapContentHandler
        // and cache the image data in the file cache.
        ContentHandler bitmapHandler = JamendoCache.capture(new BitmapContentHandler(), null);

        // For pre-fetching, use a "sink" content handler so that the
        // the binary image data is captured by the cache without actually
        // parsing and loading the image data into memory. After pre-fetching,
        // the image data can be loaded quickly on-demand from the local cache.
        ContentHandler prefetchHandler = JamendoCache.capture(JamendoCache.sink(), null);

        // Perform callbacks on the main thread
        Handler handler = null;
        
        return new ImageLoader(IMAGE_TASK_LIMIT, streamFactory, bitmapHandler, prefetchHandler,
                ImageLoader.DEFAULT_CACHE_SIZE, handler);
    }

	
}
