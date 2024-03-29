# Reactive LiveData
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.10-blue.svg)](http://kotlinlang.org)
[![CI](https://github.com/musichin/ReactiveLiveData/actions/workflows/ci.yml/badge.svg)](https://github.com/musichin/ReactiveLiveData/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/musichin/ReactiveLiveData/branch/main/graph/badge.svg?token=6M3tKXUbf3)](https://codecov.io/gh/musichin/ReactiveLiveData)
[![Maven Central](https://img.shields.io/maven-central/v/de.musichin.reactivelivedata/reactivelivedata)](https://search.maven.org/artifact/de.musichin.reactivelivedata/reactivelivedata)

This library provides basic transformation functions for your [`LiveData`](https://developer.android.com/topic/libraries/architecture/livedata.html) objects.

## Functions
* `map`
* `switchMap`
* `switchLatest`
* `filter`
* `filterNotNull`
* `distinct`
* `distinctUntilChanged`
* `merge`
* `combineLatest`
* `startWith`
* `first`
* `take`
* `takeUntil`
* `takeWhile`
* `buffer`
* `cast`
* and more...

## Usage

### `filter`
```kotlin
val source: LiveData<Int> = // 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
source.filter { it % 2 == 1 }.observe(this) {
    println(it) // 1, 3, 5, 7, 9
}
```

### `distinct`
```kotlin
val source: LiveData<Int> = // 1, 2, 1, 3, 2, 4, 1, 3, 2, 5
source.distinct().observe(this) {
    println(it) // 1, 2, 3, 4, 5
}
```

### `merge`
```kotlin
val source1: LiveData<String> = // 1,    2,    3,
val source2: LiveData<Int> = // a,    b,    c,
listOf(source1, source2).merge().observe(this) { value ->
    println(value) // a, 1, b, 2, c, 3
}
```

## Binaries
```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'de.musichin.reactivelivedata:reactivelivedata:x.y.z'
}
```

## Contributing
Contributors are more than welcome. Just upload a PR with a description of your changes.

## Issues or feature requests?
File github issues for anything that is unexpectedly broken.

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
