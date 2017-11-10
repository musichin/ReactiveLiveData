# LiveData Utils [ ![Download](https://api.bintray.com/packages/musichin/maven/livedatautils/images/download.svg)](https://bintray.com/musichin/maven/livedatautils/_latestVersion) [![Kotlin](https://img.shields.io/badge/Kotlin-1.1.51-blue.svg)](http://kotlinlang.org) [![Build Status](https://travis-ci.org/musichin/livedatautils.svg?branch=master)](https://travis-ci.org/musichin/livedatautils)
This library provides basic transformation functions for your [`LiveData`](https://developer.android.com/topic/libraries/architecture/livedata.html) objects

## Functions
* `map`
* `switchMap`
* `filter`
* `filterNotNull`
* `distinctUntilChanged`
* `merge`
* combineLatest
* and more...

## Examples

### `filter`
```kotlin
val data: LiveData<Int> = // 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
data.filter { it % 2 == 1 }.observe(this) {
    println(it) // 1, 3, 5, 7, 9
}
```

### `distinctUntilChanged`
```kotlin
val data: LiveData<Int> = // 1, 2, 2, 3, 4, 4, 4, 5
data.distinctUntilChanged().observe(this) {
    println(it) // 1, 2, 3, 4, 5
}
```

## Binaries
```groovy
repositories {
    maven { url 'https://dl.bintray.com/musichin/maven' }
}

dependencies {
    implementation 'com.github.musichin.livedatautils:livedatautils:x.y.z'
}
```

## License

    MIT License

    Copyright (c) 2017 Anton Musichin

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
