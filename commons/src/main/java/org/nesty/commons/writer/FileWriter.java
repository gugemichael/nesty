package org.nesty.commons.writer;

import java.io.IOException;

public interface FileWriter {

    enum WriteMode {
        APPEND, TRUNCATE
    }

    /**
     * write content
     *
     * @param content
     */
    void write(byte content[], int offset, int count);

    /**
     * write with content string type
     *
     * @param content
     */
    void writeLine(String content);

    /**
     * write something to underlay resource
     */
    void flush();

    /**
     * close the writer
     */
    void close();

    /**
     * open file with full path name and mode
     *
     * @param fileName, full path name
     * @param mode,     {@link WriteMode}
     * @return true if open success
     */
    boolean open(String fileName, WriteMode mode) throws IOException;

}
