package elasticsearch.client;

import java.net.*;

public class ParentLastClassLoader extends URLClassLoader {
    public ParentLastClassLoader(URL[] urls) {
        super(urls);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                try {
                    c = findClass(name);
                } catch (ClassNotFoundException ignored) {
                }

                if (c == null) {
                    c = getParent().loadClass(name);
                    if (c == null) {
                        throw new ClassNotFoundException(name);
                    }
                }
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }
}
