package org.nesty.commons.writer;

public class ByteArrayBuffer {

	/**
	 * buffer capacity max size
	 */
	private static final int DEFAULT_CAPACITY = 1024 * 8;

	private final int capacity;
	private final byte[] buffer;

	private int coursor = 0;
	
	public ByteArrayBuffer() {
		this(DEFAULT_CAPACITY);
	}
	
	public ByteArrayBuffer(int capacity) {
		if (capacity <= 0)
			throw new IllegalArgumentException("byte array buffer capacity is negative");
		this.capacity = capacity;
		this.buffer = new byte[capacity];
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public int getOffset() {
		return coursor;
	}

	public void clear() {
		coursor = 0;
	}

	/**
	 * check if there have space to append size of ${count}
	 * 
	 * @param count
	 *            , size in bytes
	 * @return true if space is enough
	 */
	public boolean ensureCapacity(int count) {
		return coursor + count <= capacity;
	}

	/**
	 * append content to buffer , ensureCapacity() will be called before this
	 * method
	 * 
	 * @param content
	 *            , content bytes
	 * @param count
	 *            , total bytes of content
	 */
	public ByteArrayBuffer append(byte[] content, int offset, int count) {
		if (content != null && count > 0 && offset >= 0 && ensureCapacity(count)) {
			int written = Math.min(count, capacity);
			System.arraycopy(content, 0, buffer, coursor, written);
			this.coursor += written;
		}
		return this;
	}
}
