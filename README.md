# SwingDPI
Make using your Swing application convenient on HiDPI screens
SwingDPI allows you to scale your application for convenient using on screens with high resolution (e.g. FullHD)

![Swing application that running without DPI scaling compared with the same application running with SwingDPI](https://raw.githubusercontent.com/krlvm/SwingDPI/master/comparasion.png "Swing application that running without DPI scaling compared with the same application running with SwingDPI")
###### Swing application that running without DPI scaling compared with the same application running with SwingDPI

### Usage
At first, you need to connect SwingDPI to your project. You can download .jar from the releases section and connect it to your project as a dependency, which will be extracted into your jarfile. After that, you can import SwingDPI API to your program loader class: `import ru.krlvm.swingdpi.SwingDPI`
Easiest method of usage is adding this line to your loader code, before creating any frames: `SwingDPI.applyScalingAutomatically()` - SwingDPI automatically determine the system DPI scaling and apply it for your program - no more needed. For more fine tuning you can play with `scale(dimension)` method that allow you to change frames and elements size depending on screen DPI. You can explore and edit the code for you according to your needs - it is opened under the MIT license.