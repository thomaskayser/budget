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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * MemoryChunksOutputStream entspricht in etwa einem ByteArrayOutputStream, unterscheidet sich aber in folgenden Aspekten: - Reserviert den
 * Speicher in Bloecken a 512 Byte und nicht am Stueck. - Die Methoden sind nicht synchronisiert. - Beim Herausgeben des Stream-Inhalts
 * wird keine Kopie erstellt.
 * 
 */
final class MemoryChunksOutputStream extends OutputStream {
    /**
     * Die Liste der Memory Chunks
     */
    private List<byte[]> chunks = new ArrayList<byte[]>();

    /**
     * Die aktuelle Laenge des OutputStreams
     */
    private int length = 0;

    /**
     * Die Groesse der einzelnen Memory Chunks
     */
    static final int CHUNK_LENGTH = 512;

    /**
     * Erzeugt einen leeren Memory Chunk basierten OutputStream
     */
    public MemoryChunksOutputStream() {
    }

    /**
     * Liefert die Liste der Memory Chunks.
     */
    public final List<byte[]> getMemoryChunks() {
        return this.chunks;
    }

    /**
     * Liefert die Laenge des gesamten Streams
     */
    public int getLength() {
        return this.length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.OutputStream#write(int)
     */
    public void write(int b) throws IOException {
        int newLength = this.length + 1;

        if (newLength > this.chunks.size() * MemoryChunksOutputStream.CHUNK_LENGTH) {
            this.chunks.add(new byte[MemoryChunksOutputStream.CHUNK_LENGTH]);
        }

        byte[] buffer = this.chunks.get(this.length / MemoryChunksOutputStream.CHUNK_LENGTH);

        buffer[this.length % MemoryChunksOutputStream.CHUNK_LENGTH] = (byte)b;
        this.length++;
    }

}
