# SwingDPI
Make using your Swing application convenient on HiDPI screens

SwingDPI allows you to scale your application for convenient using on screens with high resolution (e.g. FullHD, 4K)

You can download the latest build from `Releases` tab, supports Java 7 and later

![Swing application that running with SwingDPI compared with the same application without DPI scaling](https://raw.githubusercontent.com/krlvm/SwingDPI/master/comparasion.png "Swing application that running without DPI scaling compared with the same application running with SwingDPI")
###### Swing application that running with DPI-scaling compared with the same application without it

### Using in a project
At first, you need to connect SwingDPI to your project. You can download .jar from the releases section and connect it to your project as a dependency, which will be extracted into your jarfile. After that, you can import SwingDPI API to your program loader class: `import ru.krlvm.swingdpi.SwingDPI`

You also can add SwingDPI using Maven:
```
<dependency>
  <groupId>io.github.krlvm</groupId>
  <artifactId>swingdpi</artifactId>
  <version>1.1.10</version>
</dependency>
```

The easiest method of usage is adding this line to your loader code, before creating any frames: `SwingDPI.applyScalingAutomatically()` - SwingDPI automatically determine the system DPI scaling and apply it for your program - nothing more is needed. For more fine tuning you can play with `scale(dimension)` method that allow you to change frames and elements size depending on screen DPI. You can explore and edit the code for you according to your needs - source code is opened under the MIT license.

### Using outside a project
You can scale existing Java Swing applications by modifying its .jar files.

At first, open SwingDPI.jar and jar file of the application by an achiever (e.g., 7-zip) and copy contents of SwingDPI.jar to target application jar.

Then open `MANIFEST.MF` of the combined JAR and look for parameter `Main-Class`. Create in the application folder a text file named `SwingDPI.txt` and fill it with `Main-Class` value, after that set `Main-Class` of the manifest to `ru.krlvm.swingdpi.CustomBootstrap`.

The application should support DPI scaling now.
