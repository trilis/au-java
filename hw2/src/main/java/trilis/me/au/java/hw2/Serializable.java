package trilis.me.au.java.hw2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface for data structures which contents are able to be
 * serialized, read from InputStream and written to OutputStream.
 */
public interface Serializable {

    /**
     * Writes data of this trie into the specified output stream.
     *
     * @param out the specified output stream.
     *
     * @throws IOException if an I/O error occurs.
     */
    void serialize(OutputStream out) throws IOException;

    /**
     * Reads serialized data of trie from the specified input stream
     * and replaces this trie with the read one.
     *
     * @param in the specified input stream.
     */
    void deserialize(InputStream in);
}
