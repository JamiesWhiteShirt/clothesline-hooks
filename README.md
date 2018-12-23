# Clothesline Hooks
[![Maven Repository](https://img.shields.io/maven-metadata/v/https/maven.jamieswhiteshirt.com/libs-release/com/jamieswhiteshirt/clothesline-hooks/maven-metadata.xml.svg)](https://maven.jamieswhiteshirt.com/libs-release/com/jamieswhiteshirt/clothesline-hooks/)

Core mod component for [Clothesline](https://github.com/JamiesWhiteShirt/clothesline).
Clothesline Hooks is an independent mod that does nothing by itself, but adds some necessities to make Clothesline work.

## Developing Clothesline Hooks

To get started, refer to the [MinecraftForge documentation](https://mcforge.readthedocs.io/en/latest/gettingstarted/).

## Usage

*These usage instructions are for Clothesline Hooks only.
See [Clothesline](https://github.com/JamiesWhiteShirt/clothesline) for usage of both Clothesline and Clothesline Hooks.*

To use this mod in your workspace, add the following to your `build.gradle`:

```groovy
repositories {
    maven {url "https://maven.jamieswhiteshirt.com/libs-release/"}
}

dependencies {
    deobfCompile "com.jamieswhiteshirt:clothesline-hooks:<CLOTHESLINE_HOOKS_VERSION>"
}
```

Clothesline Hooks has an API, but it is currently unstable and with limited functionality.
The API is located in the `com.jamieswhiteshirt.clothesline.hooks.api` package.
Clothesline Hooks is designed to suit the needs of Clothesline only, so usage by other projects is not recommended.
