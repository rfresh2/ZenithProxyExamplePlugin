plugins {
    id("zenithproxy.plugin.dev") version "1.1.+"
    id("org.graalvm.buildtools.native") version "1.1.6"
}

group = property("maven_group") as String
version = property("plugin_version") as String
val mc = property("mc") as String
val pluginId = property("plugin_id") as String
val pluginName = property("plugin_name") as String

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
        nativeImageCapable = true
    }
}

zenithProxyPlugin {
    templateProperties = mapOf(
        // variables in your BuildConstants.java template class
        "version" to project.version,
        "mc_version" to mc,
        "plugin_id" to pluginId,
        "maven_group" to group as String,
    )
    // the minimum supported java version for users of your plugin
    javaReleaseVersion = JavaLanguageVersion.of(21)
    // set to false if developing against a zenith version before 3.7.0
    runTaskMixinLauncher = true
}

repositories {
    /** uncomment to use ZenithProxy pre-release snapshots **/
//    maven("https://maven.2b2t.vc/snapshots") {
//        description = "ZenithProxy Prereleases"
//    }
    maven("https://maven.2b2t.vc/releases") {
        description = "ZenithProxy Releases"
    }
    maven("https://maven.2b2t.vc/remote") {
        description = "Dependencies used by ZenithProxy"
    }
}

dependencies {
    zenithProxy("com.zenith:ZenithProxy:$mc-SNAPSHOT")

    /** or select a specific ZenithProxy version **/
//    zenithProxy("com.zenith:ZenithProxy:3.7.0+$mc")

    compileOnly("org.graalvm.sdk:nativeimage:25.1.3")

    /** to include dependencies into your plugin jar **/
//    shade("com.github.ben-manes.caffeine:caffeine:3.2.0")
}

tasks {
    shadowJar {
        /**
         * relocate shaded dependencies to avoid conflicts with other plugins
         * transitive dependencies should also be relocated or removed (with exclude)
         * build and examine your plugin jar contents to check
         * https://gradleup.com/shadow/configuration/relocation/
         */
//        val basePackage = "${project.group}.shadow"
//        relocate("com.github.benmanes.caffeine", "$basePackage.caffeine")

        /**
         * remove unneeded transitive dependencies
         * https://gradleup.com/shadow/configuration/dependencies/#filtering-dependencies
         */
//        dependencies {
//            exclude(dependency(":error_prone_annotations:.*"))
//            exclude(dependency(":jspecify:.*"))
//        }
    }
    nativeCompile {
        notCompatibleWithConfigurationCache("not compatible with configuration cache")
        dependsOn(shadowJar, build)
    }
    generateResourcesConfigFile {
        notCompatibleWithConfigurationCache("not compatible with configuration cache")
        dependsOn(shadowJar)
    }
}

graalvmNative {
    binaries {
        named("main") {
            javaLauncher = javaToolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(25))
                nativeImageCapable = true
            }
            imageName = pluginName
            mainClass = "com.zenith.Proxy"
            quickBuild = false // set to true for fast builds while developing
            verbose = true
            sharedLibrary = false
            buildArgs.addAll(
                // required - otherwise plugin classes will be stripped
                // you may need to add more if your plugin has dependencies
                "-H:Preserve=package=${project.group}.*",
                "-O3", // highest optimization level, but slowest build times
                "-H:DeadlockWatchdogInterval=30",
                "-H:+CompactingOldGen",
                "-H:+TrackPrimitiveValues",
                "-H:+TreatAllTypeReachableConditionsAsTypeReached",
                "-H:+UsePredicates",
                "-H:-ReduceImplicitExceptionStackTraceInformation",
                "--future-defaults=all",
                "-R:MaxHeapSize=225m",
                "-march=x86-64-v3",
                "--gc=serial",
                "-J-XX:MaxRAMPercentage=90"
            )
            configurationFileDirectories.from(file("src/main/resources/META-INF/native-image"))
        }
        metadataRepository { enabled = true }
    }
}
