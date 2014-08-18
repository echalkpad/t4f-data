package io.datalayer.elasticsearch.analysis.url.tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.Arrays;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

public final class UrlTokenizer extends Tokenizer {

    /** Default read buffer size */
    public static final int DEFAULT_BUFFER_SIZE = 250;

    private boolean done = false;
    private int finalOffset;

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);

    public UrlTokenizer(AttributeFactory factory, Reader input, int bufferSize) {
        super(factory, input);
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("bufferSize must be > 0");
        }
        termAtt.resizeBuffer(bufferSize);
    }

    @Override
    public final boolean incrementToken() throws IOException {

        if (!done) {
        
            clearAttributes();
            done = true;
            int upto = 0;
            char[] buffer = termAtt.buffer();
            
            while (true) {
                final int length = input.read(buffer, upto, buffer.length - upto);
                if (length == -1) {
                    break;
                }
                upto += length;
                if (upto == buffer.length) {
                    buffer = termAtt.resizeBuffer(1 + buffer.length);
                }
            }
            
            String url = new String(Arrays.copyOf(buffer, upto));
            String host = new URL(url).getHost();
            termAtt.append(host);

            termAtt.setLength(host.length());
            finalOffset = correctOffset(host.length());
            offsetAtt.setOffset(correctOffset(0), finalOffset);
            
            return true;
        
        }
    
        return false;

    }

    @Override
    public final void end() {
        // set final offset
        offsetAtt.setOffset(finalOffset, finalOffset);
    }

    @Override
    public void reset() throws IOException {
        this.done = false;
    }

}
