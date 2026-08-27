pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "editor-oss"
include(":app")
include(":core:model", ":core:geometry", ":core:history", ":core:editor", ":core:common")
include(":engine:renderer", ":engine:opengl", ":engine:texture", ":engine:compositor")
include(":image:decoder", ":image:exporter")
include(":image:processor")
include(":data:project", ":data:media")
include(":feature:editor")
