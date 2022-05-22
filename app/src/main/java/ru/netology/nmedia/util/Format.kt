package ru.netology.nmedia.util


fun Int.formatNumber(): String {
    return when {
        this > 999_999 -> {
            (this / 1_000_000).toString() + "." + (this % 1_000_000 / 1_000).toString() + "M"
        }
        this > 9999 -> {
            (this / 1_000).toString() + "K"
        }
        this > 1099 -> {
            if (this % 1_000 < 100) {
                (this / 1_000).toString() + "K"
            } else {
                (this / 1_000).toString() + "." + (this % 1_000 / 100).toString() + "K"
            }
        }
        this > 999 -> {
            (this / 1_000).toString() + "K"
        }
        else -> {
            this.toString()
        }
    }
}