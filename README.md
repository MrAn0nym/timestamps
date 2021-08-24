# `Aliucord-plugins-template`

Template for an [Aliucord](https://github.com/Aliucord) plugins repo

## Setup

1. Generate a repo based on this template

## Getting started with writing your first plugin

This template includes 4 example plugins which you can find in the ExamplePlugins folder.

1. Copy the `HelloWorld` example plugin into the root of this folder as MyFirstPlugin and fix all values in the plugin Manifest method
2. Uncomment the first line in `settings.gradle.kts` to tell gradle to include it. Whenever you add a new plugin you have to add it here
3. Inspect the updater.json file and uncomment the MyFirstPlugin section. Whenever you add a new plugin you have to add it to updater.json similar to the MyFirstPlugin block
4. Run `./gradlew MyFirstPlugin:make` or `./gradlew MyFirstPlugin:deployWithAdb`
