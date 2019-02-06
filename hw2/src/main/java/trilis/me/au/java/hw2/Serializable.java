package trilis.me.au.java.hw2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Serializable {

    /**
     * Writes data of this trie into the specified output stream.
     *
     * @param out the specified output stream.
     */
    void serialize(OutputStream out) throws IOException;

    /**
     * Reads serialized data of trie from the specified input stream
     * and replaces this trie with the read one.
     *
     * @param in the specified input stream.
     */
    void deserialize(InputStream in) throws IOException;
}
