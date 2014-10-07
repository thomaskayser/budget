/*
 * Software is written by:
 *
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2009
 * 
 */
package ch.tkayser.budget.util;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * MemoryChunksInputStream entspricht in etwa dem ByteArrayInputStream, unterscheidet sich aber in folgenden Merkmalen: - Das Memory wird
 * nicht am Stueck, sondern in Bloecken a 512 Byte reserviert. - Die Methoden sind nicht synchronisiert.
 * 
 * Die Liste mit chunks wird durch einen MemoryChunkOutputStream erzeugt.
 * 
 */
final class MemoryChunksInputStream extends InputStream {

    /**
     * Liste mit allen Memory Chunks
     */
    private final List<byte[]> chunks;

    /**
     * Die Laenge des gesamten Streams
     */
    private final int length;

    /**
     * Die aktuelle Position im Stream
     */
    private int offset;

    /**
     * Erzeugt einen neuen Memory Chunk basierten InputStream
     * 
     * @param chunks Liste mit den Memory Chunks
     * 
     */
    public MemoryChunksInputStream(final List<byte[]> chunks) {
        this(chunks, chunks.size() * MemoryChunksOutputStream.CHUNK_LENGTH);
    }

    /**
     * Erzeugt einen neuen Memory Chunk basierten InputStream
     * 
     * @param chunks Liste mit den Memory Chunks
     * @param length Laenge des Streams
     * 
     */
    public MemoryChunksInputStream(final List<byte[]> chunks, final int length) {
        this.chunks = chunks;
        this.length = length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.InputStream#read()
     */
    public int read() throws IOException {
        if (this.offset == this.length) {
            return -1;
        }

        byte[] buffer = this.chunks.get(this.offset / MemoryChunksOutputStream.CHUNK_LENGTH);
        return buffer[this.offset++ % MemoryChunksOutputStream.CHUNK_LENGTH] & 0xff;
    }
}
