# Reactive LiveData
[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.21-blue.svg)](https://kotlinlang.org)
[![CI](https://github.com/musichin/ReactiveLiveData/actions/workflows/ci.yml/badge.svg)](https://github.com/musichin/ReactiveLiveData/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/musichin/ReactiveLiveData/branch/main/graph/badge.svg?token=6M3tKXUbf3)](https://codecov.io/gh/musichin/ReactiveLiveData)
[![Maven Central](https://img.shields.io/maven-central/v/de.musichin.reactivelivedata/reactivelivedata)](https://central.sonatype.com/artifact/de.musichin.reactivelivedata/reactivelivedata)

This library provides RxJava-style operators for your [`LiveData`](https://developer.android.com/topic/libraries/architecture/livedata.html) objects.

## Operators

### Filtering
* `filter`, `filterNot`: emit only elements that match, or don't match, a predicate
* `filterNotNull`: drops `null` elements
* `filterIsInstance`: emits only elements of a given type
* `distinct`: emits each element only once
* `distinctUntilChanged`: drops elements equal to the previous one, by key or comparer
* `first`, `take`, `takeUntil`, `takeWhile`: emit the first elements, then stop
* `elementAt`: emits only the element at a given index
* `skip`, `skipUntil`, `skipWhile`, `skipLast`: skip the first or the last elements
* `sample`: emits the latest element whenever another `LiveData` emits

### Transforming
* `mapNotNull`: transforms elements and drops `null` results
* `cast`: casts elements to a given type
* `scan`: emits running accumulations of the elements
* `buffer`: emits elements in lists of a given size
* `zipWithNext`: pairs each element with the one before it
* `switchLatest`: follows the latest `LiveData` emitted by a `LiveData` of `LiveData`s

### Combining
* `merge`, `mergeArray`, `mergeWith`, `+`: emit the elements of all sources
* `combineLatest`, `combineLatestWith`: combine the latest elements of all sources
* `withLatestFrom`: combines each element with the latest element of another `LiveData`
* `amb`, `ambWith`: follow whichever source emits first
* `startWith`: emits given elements before the source's own

### Side effects
* `doOnValue`, `doOnActive`, `doOnInactive`: run code for each element, or when the result becomes active or inactive

`map`, `switchMap` and `distinctUntilChanged()` without arguments come from AndroidX Lifecycle (`androidx.lifecycle`).

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
val source1: LiveData<Int> = //    1,     2,     3
val source2: LiveData<Int> = // 10,    20,    30
listOf(source1, source2).merge().observe(this) {
    println(it) // 10, 1, 20, 2, 30, 3
}
```

### `withLatestFrom`
```kotlin
val input: LiveData<String> =  // "a", "ab",       "abc"
val submits: LiveData<Unit> =  //            Unit,        Unit
submits.withLatestFrom(input) { _, text -> text }.observe(this) {
    println(it) // "ab", "abc"
}
```

## Good to know
* Like `LiveData` itself, all operators must be called on the main thread.
* `LiveData` keeps only its latest value, so operators see only the elements they receive while the returned
  `LiveData` is active.

## Installation
Available on Maven Central.

```kotlin
dependencies {
    implementation("de.musichin.reactivelivedata:reactivelivedata:2.0.0")
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
* `liveData()`, `liveData(value)` and `liveData { }` were renamed to `emptyLiveData()`, `liveDataOf(value)` and
  `lazyLiveData { }`, because `liveData { }` clashed with the coroutine builder of AndroidX Lifecycle.

See the [2.0.0 release notes](https://github.com/musichin/ReactiveLiveData/releases/tag/2.0.0) for all changes.

## Contributing
Contributions are welcome: open a pull request that describes your changes.

## Issues or feature requests?
Open a [GitHub issue](https://github.com/musichin/ReactiveLiveData/issues) for bugs or feature requests.

## License
MIT, see [LICENSE](LICENSE).
