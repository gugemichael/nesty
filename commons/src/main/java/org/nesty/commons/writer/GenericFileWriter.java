package org.nesty.commons.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GenericFileWriter implements FileWriter {

    /**
     * thread cached calender instances
     */
    private ThreadLocal<ByteArrayBuffer> arena = new ThreadLocal<ByteArrayBuffer>() {
        @Override
        protected ByteArrayBuffer initialValue() {
            // Java fill the array buffer with zero
            return new ByteArrayBuffer();
        }
    };

    private static final String seperator = String.format("%n");

    /**
     * file handler
     */
    private volatile String fileName;
    private volatile WriteMode mode;

    /**
     * file write handler , dont's use ${FileWriter} bufferd , we flush() right
     * now after write()
     */
    protected volatile FileOutputStream out;

    private final boolean buffered;

    public GenericFileWriter(boolean buffered) {
        this.buffered = buffered;
    }

    public boolean open(String fileName) throws IOException {
        return open(fileName, WriteMode.APPEND);
    }

    /**
     * open file with FileMode
     *
     * @param fileName, name of the file full path
     * @param mode,     {@link WriteMode} APPEND or TRUNCATE
     * @return true if open success
     * @throws IOException new {@link FileOutputStream} failed
     */
    @Override
    public synchronized boolean open(String fileName, WriteMode mode) throws IOException {

        if (fileName == null || fileName.isEmpty())
            return false;

        File file = new File(fileName);
        if (!file.exists() && !file.createNewFile())
            return false;

        this.fileName = fileName;
        boolean append = ((this.mode = mode) == WriteMode.APPEND);

        this.out = new FileOutputStream(file, append);

        return true;
    }


    /**
     * write String conent
     */
    @Override
    public void writeLine(String content) {
        String line = (content + seperator);
        write(line.getBytes(), 0, line.length());
    }

    /**
     * write byte[] conent
     */
    @Override
    public void write(byte[] content, int offset, int count) {
        if (buffered) {
            ByteArrayBuffer buffer = arena.get();
            // flush directly if we have no space to reserve
            if (!buffer.ensureCapacity(count)) {
                doWrite(buffer.getBuffer(), 0, buffer.getOffset());
                buffer.clear();
            }
            buffer.append(content, offset, count);
        } else {
            doWrite(content, offset, count);
        }
    }

    private void doWrite(byte[] content, int offset, int count) {
        if (content != null && count > 0 && offset >= 0) {
            try {
                out.write(content, offset, count);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * flush the buffered content if this writer buffered enable
     */
    @Override
    public void flush() {
        try {
            if (buffered) {
                //flush buffered content first of all
                ByteArrayBuffer buffer = arena.get();
                if (buffer.getOffset() != 0)
                    doWrite(buffer.getBuffer(), 0, buffer.getOffset());
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        flush();
        try {
            out.close();
            out = null;
        } catch (IOException ignored) {
        }
    }

    protected String getFileName() {
        return fileName;
    }

    protected WriteMode getFileWriteMode() {
        return mode;
    }
}
