package org.dalol.videozilla.transformers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import okhttp3.ResponseBody;

/**
 * @author Filippo
 * @version 1.0.0
 * @since Sat, 16/06/2018 at 14:45.
 */
public class DefaultGzipTransformer implements Transformer<ResponseBody, String> {

    @Override
    public String transform(ResponseBody input) throws Exception {
        GZIPInputStream gzipInputStream = new GZIPInputStream(input.byteStream());
        InputStreamReader streamReader = new InputStreamReader(gzipInputStream, "UTF-8");
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        input.close();
        streamReader.close();
        gzipInputStream.close();
        reader.close();
        return builder.toString();
    }
}
