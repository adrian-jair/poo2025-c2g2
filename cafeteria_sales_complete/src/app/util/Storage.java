package app.util;

import java.io.*;
import java.util.ArrayList;

// Util simple pa' guardar y cargar listas usando serializacion
public class Storage {
    public static <T> void save(String file, ArrayList<T> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> loadList(String file) {
        File f = new File(file);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object obj = ois.readObject();
            if (obj instanceof ArrayList) return (ArrayList<T>) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
