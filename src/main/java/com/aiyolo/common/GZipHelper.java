package com.aiyolo.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZipHelper {

    public static byte[] compress(final String content) throws IOException {
        if (content == null || content.length() == 0) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream(content.length());
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(content.getBytes());
        gzip.close();

        return out.toByteArray();
    }

    public static String decompress(final byte[] compressed) throws IOException {
        if (compressed == null || compressed.length == 0) {
            return null;
        }

        if (isCompressed(compressed)) {
            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, "UTF-8"));

            StringBuilder content = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }

            return content.toString();
        } else {
            return new String(compressed, "UTF-8");
        }
    }

    public static boolean isCompressed(final byte[] compressed) {
        return (compressed[0] == (byte) GZIPInputStream.GZIP_MAGIC) && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }

}
