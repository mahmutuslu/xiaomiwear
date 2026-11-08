plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.mihealthalias"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mihealthalias"
        minSdk = 26
        targetSdk = 34
        versionCode = 200
        versionName = "2.0.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // Runtime is provided by an API 102 capable Xposed framework.
    compileOnly("io.github.libxposed:api:102.0.0")
    testImplementation("junit:junit:4.13.2")
}
