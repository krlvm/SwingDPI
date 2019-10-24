package ru.krlvm.swingdpi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;

/**
 * SwingDPI Custom Bootstrap
 *
 * This class allows launching third-party Swing-based UI Java applications
 * with SwingDPI DPI scaling. Merge contents of this JAR with the your app JAR
 * and set Main-Class of the united JAR manifest to ru.krlvm.swingdpi.CustomBootstrap,
 * then create file SwingDPI.text in the program folder and write there initial
 * Main-Class of the manifest
 *
 * SwingDPI allows you to scale your application for convenient using on HiDPI screens
 * Call SwingDPI.applyScalingAutomatically() on your application start for easy scaling
 * GitHub Page: https://github.com/krlvm/SwingDPI
 *
 * @version 1.1
 * @author krlvm
 */
public class CustomBootstrap {

    public static void main(String[] args) throws Exception {
        SwingDPI.applyScalingAutomatically();
        BufferedReader br = new BufferedReader(new FileReader("SwingDPI.txt"));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }
        br.close();
        String main = sb.toString();
        Class<?> clazz = Class.forName(main);
        Method method = null;
        for (final Method clazzMethod : clazz.getMethods()) {
            if (clazzMethod.getName().equals("main")) {
                method = clazzMethod;
            }
        }
        if (method == null) {
            throw new Exception("Main method not found");
        }
        method.invoke(null, (Object) args);
    }
}
