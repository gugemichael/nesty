package org.nesty.commons.writer;


import org.nesty.commons.utils.Time;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class DailyRollFileWriter extends GenericFileWriter {

    // rotate file name carry with daytime
    private static final String ROTATE_FILE_PATTERN = "%s.%d-%02d-%02d";
    // rotate file name if conflict with default daytime rotate file, carry with autonumber
    private static final String ROTATE_FILE_CONFLICT_PATTERN = "%s.%d";
    // rotate locking
    @SuppressWarnings("unused")
    private ReentrantLock lock = new ReentrantLock();
    // file timestamp
    private volatile Time lastest;

    public DailyRollFileWriter(boolean buffered) {
        super(buffered);
    }

    @Override
    public void write(byte[] content, int offset, int count) {

        Time current = Time.fetch();

        super.write(content, offset, count);

        if (current.diffSecond(lastest)) {
            // roll file if necessary
            checkRotate(current);
        }
    }

    /**
     * rotate and split in synchronize
     */
    protected synchronized void checkRotate(Time current) {

        try {
            if (lastest != null) {

                // check if daytime changed
                if (current.diffDay(lastest)) {

                    // start rotate file

                    /**
                     * 1. first of all, rename the file to "file.yyyyy-mm-dd"
                     *     parallel write will be lead to this file, so the previos
                     *     file pointer is still valid
                     */
                    archive(lastest);

                    /**
                     * 2. snapshot the current file output stream, we close it
                     *     after we open new output stream
                     *
                     */
                    FileOutputStream close = super.out;

                    /**
                     * 3. open new stream ! all operations will effect the new
                     *     output stream, and we can close the previos *snapshot*
                     *     safe
                     */
                    open(getFileName(), getFileWriteMode());

                    /**
                     * 4. safe close
                     */
                    if (close != null) {
                        close.flush();
                        close.close();
                    }
                }
            }

            lastest = current;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void archive(Time time) {
        String newName = String.format(ROTATE_FILE_PATTERN, getFileName(), time.getYear(), time.getMonth(), time.getDay());
        File archive = new File(newName);
        File current = new File(getFileName());
        if (archive.exists()) {
            int sequence = 1, max = Short.MAX_VALUE;
            File newFile;
            while (sequence++ != max) {
                newFile = new File(String.format(ROTATE_FILE_CONFLICT_PATTERN, newName, sequence));
                if (!newFile.exists() && current.renameTo(newFile))
                    break;
            }
        } else
            current.renameTo(archive);
    }

}

