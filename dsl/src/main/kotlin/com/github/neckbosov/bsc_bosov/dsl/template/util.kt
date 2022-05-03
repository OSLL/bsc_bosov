package com.github.neckbosov.bsc_bosov.dsl.template

import kotlin.random.Random

fun Random.nextLong(from: Long?, until: Long?): Long = if (from == null) {
    if (until == null) {
        this.nextLong()
    } else {
        this.nextLong(until)
    }
} else {
    if (until == null) {
        this.nextLong(from, Long.MAX_VALUE)
    } else {
        this.nextLong(from, until)
    }
}

fun Random.nextInt(from: Int?, until: Int?): Int = if (from == null) {
    if (until == null) {
        this.nextInt()
    } else {
        this.nextInt(until)
    }
} else {
    if (until == null) {
        this.nextInt(from, Int.MAX_VALUE)
    } else {
        this.nextInt(from, until)
    }
}

fun Random.nextDouble(from: Double?, until: Double?): Double = if (from == null) {
    if (until == null) {
        this.nextDouble()
    } else {
        this.nextDouble(until)
    }
} else {
    if (until == null) {
        this.nextDouble(from, Double.MAX_VALUE)
    } else {
        this.nextDouble(from, until)
    }
}


