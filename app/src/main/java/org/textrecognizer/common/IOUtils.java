package org.textrecognizer.common;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import androidx.annotation.RawRes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class IOUtils {

    public static String readRawResource(final Context context, @RawRes final int id) throws IOException {
        final InputStream is = context.getResources().openRawResource(id);
        return readFile(is.available(), is);
    }

    public static String readAsset(final Context context, final String fileName) throws IOException {
        final InputStream is = context.getAssets().open(fileName);
        return readFile(getSize(fileName, context.getAssets()), is);
    }

    public static FileInputStream getFileInputStream(final File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public static FileOutputStream getFileOutputStream(final File file) {
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private static int getSize(String fileName, AssetManager assets) throws IOException {
        final AssetFileDescriptor fd = assets.openFd(fileName);
        final int size = (int) fd.getLength();
        fd.close();
        return size;
    }

    private static String readFile(final int size, final InputStream is) throws IOException {
        final byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        return new String(buffer);
    }

    public static void saveSource2Sink(final InputStream source, final File sink) {
        try {
            final byte[] buffer = new byte[source.available()];
            source.read(buffer);
            try (final OutputStream outStream = new FileOutputStream(sink)) {
                outStream.write(buffer);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void persist(final String source, final OutputStream sink) {
        try (final Writer writer = new OutputStreamWriter(sink)) {
            writer.write(source);
            writer.flush();
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
