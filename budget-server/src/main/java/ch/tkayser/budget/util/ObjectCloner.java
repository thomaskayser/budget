/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-03-22 08:43:25 +0100 (Di, 22 Mrz 2011) $
 *   $Author: tom $
 *   $Revision: 2034 $ 
 */
package ch.tkayser.budget.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Object Cloner. verwendet serialisierung
 */
public class ObjectCloner {

    /**
     * Erstellt ein Kopie eines Objects unter Verwendung von Serialisierung.
     */
    public static final Object deepCopy(Object object) {
        if (object == null) {
            return null;
        } else {
            // serialize and deserialize
            List<byte[]> chunks = ObjectCloner.serializeToMemoryChunks(object);
            return ObjectCloner.deserializeFromMemoryChunks(chunks);
        }
    }

    /**
     * Serialisiert das uebergebene Objekt und gibt die resultierenden Memory Chunks zurueck.
     * 
     * @param object Das zu serialisierende Object
     * @return Die serialisierten Memory Chunks mit dem originalen Objekts
     * @throws IOException
     */
    private static List<byte[]> serializeToMemoryChunks(Object object) {
        MemoryChunksOutputStream mcos = new MemoryChunksOutputStream();

        List<byte[]> chunks = null;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(mcos);

            oos.writeObject(object);
            oos.flush();

            chunks = mcos.getMemoryChunks();

            oos.close();
            mcos.close();
        } catch (IOException e) {
            throw new RuntimeException("No IO possible", e);
        }

        return chunks;
    }

    /**
     * Deserialisiert ein Objekt aus einer serialisierten Memory Chunk Repraesentanz
     * 
     * @param chunks Die Memory Chunks, die das serialisierte Object enthalten.
     * @return Das urspruengliche Objekt
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static Object deserializeFromMemoryChunks(List<byte[]> chunks) {
        MemoryChunksInputStream mcis = new MemoryChunksInputStream(chunks);
        Object object = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(mcis);

            object = ois.readObject();

            ois.close();
        } catch (IOException e) {
            throw new RuntimeException("No IO possible", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class Not Found", e);
        } finally {
            if (mcis != null) {
                try {
                    mcis.close();
                } catch (IOException e) {
                }                
            }            
        }

        return object;
    }

}
