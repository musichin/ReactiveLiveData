# Reactive LiveData
[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.21-blue.svg)](http://kotlinlang.org)
[![CI](https://github.com/musichin/ReactiveLiveData/actions/workflows/ci.yml/badge.svg)](https://github.com/musichin/ReactiveLiveData/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/musichin/ReactiveLiveData/branch/main/graph/badge.svg?token=6M3tKXUbf3)](https://codecov.io/gh/musichin/ReactiveLiveData)
[![Maven Central](https://img.shields.io/maven-central/v/de.musichin.reactivelivedata/reactivelivedata)](https://central.sonatype.com/artifact/de.musichin.reactivelivedata/reactivelivedata)

This library provides basic transformation functions for your [`LiveData`](https://developer.android.com/topic/libraries/architecture/livedata.html) objects.

## Functions
* `switchLatest`
* `filter`
* `filterNot`
* `filterNotNull`
* `filterIsInstance`
* `distinct`
* `distinctUntilChanged`
* `merge`
* `mergeWith`
* `amb`
* `ambWith`
* `combineLatest`
* `combineLatestWith`
* `withLatestFrom`
* `startWith`
* `first`
* `elementAt`
* `take`
* `takeUntil`
* `takeWhile`
* `skip`
* `skipUntil`
* `skipWhile`
* `buffer`
* `sample`
* `scan`
* `cast`
* `doOnValue`
* `doOnActive`
* `doOnInactive`
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
val source1: LiveData<Int> =    //    1,    2,    3
val source2: LiveData<String> = // a,    b,    c
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

## Requirements
| ReactiveLiveData | minSdk | Kotlin | AndroidX Lifecycle |
|------------------|--------|--------|--------------------|
| 2.x              | 23     | ≥ 2.1  | ≥ 2.11.0           |
| 1.2.x            | 14     | ≥ 1.8  | ≥ 2.6.2            |

## Migrating from 1.x
* `map`, `switchMap` and `distinctUntilChanged()` were removed, because AndroidX Lifecycle provides the same functions.
  Import them from `androidx.lifecycle` instead; the calls stay the same.
* `combineLatest` and `merge` return `LiveData<T>` instead of `LiveData<out T>`, so casts at call sites are no longer needed.

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
