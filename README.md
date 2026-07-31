# ZenithProxy Example Plugin - GraalVM Native Image

This branch contains an example of how to build a native image of ZenithProxy with your plugin included

See GraalVM documentation for more information: https://www.graalvm.org/latest/reference-manual/native-image/basics/

This is _not_ compiling a GraalVM native image of your plugin alone - it is the full ZenithProxy application with your plugin included.

GraalVM native image is not able to dynamically load more plugins at runtime.

# Usage

`./gradlew nativeCompile`

The executable will be at: `build/native/nativeCompile/`

# Licensing Implications

ZenithProxy is licensed under the [AGPL](https://www.gnu.org/licenses/agpl-3.0.en.html)

If you distribute a native image of ZenithProxy with your plugin, this - including your plugin - must also be licensed under the AGPL

This is different than if you were to only distribute your plugin jar, as it now is a full ZenithProxy application.

# Limitations

### Reachability Metadata

A GraalVM `Feature` class is provided to quickly register additional reflection

You may need to register other types of reachability metadata yourself: https://www.graalvm.org/latest/reference-manual/native-image/metadata/

### Launcher

The compiled build output is a full ZenithProxy application with the plugin included.

So the launcher will not be downloading and running this application during normal operation.

You can execute the application directly from the command line

or the launcher will execute the application if you replace the normal `linux` release channel exe at: `launcher/ZenithProxy` but this is not officially supported.

### Multiple Plugins

This example only builds ZenithProxy with this single plugin

Any plugin included on the compile classpath will be included in the native image, so in theory it may be possible

but no example or additional documentation is provided here

# New GraalVM Plugin Checklist

1. Edit `ExamplePluginReflectionFeature.java`:
    - Move the feature class to your new corresponding package / maven group
    - Enter any additional packages to register reflection for (if needed)
2. Edit `src/resources/META-INF/native-image/org.example/exampleplugin/native-image.properties`
    - Edit the `Feature` class path to match your new feature class
    - Create directories and move this file to your corresponding package/maven group
3. Build and verify
    - Any additional build arguments needed should be set in `build.gradle.kts`'s `graalvmNative` section
    - e.g. config for plugin dependencies, different build options, etc.
