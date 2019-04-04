package iii_conventions

data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int) : Comparable<MyDate> {
    // #25
    override fun compareTo(other: MyDate) = when {
        year != other.year -> year - other.year
        month != other.month -> month - other.month
        else -> dayOfMonth - other.dayOfMonth
    }
}

enum class TimeInterval {
    DAY,
    WEEK,
    YEAR
}

class DateRange(val start: MyDate, val endInclusive: MyDate)

// #26
operator fun DateRange.contains(date: MyDate) = date >= start && date <= endInclusive

// #27
operator fun MyDate.rangeTo(other: MyDate): DateRange = DateRange(this, other)

// #28
operator fun DateRange.iterator(): Iterator<MyDate> =
        object : Iterator<MyDate> {
            var current = start

            override fun hasNext(): Boolean {
                return current <= endInclusive
            }

            override fun next(): MyDate {
                return current.apply {
                    current = current.nextDay()
                }
            }
        }

// #29
operator fun MyDate.plus(interval: TimeInterval) : MyDate = addTimeIntervals(interval, 1)

data class RepeatedTimeInterval(val ti: TimeInterval, val n: Int)

operator fun TimeInterval.times(n: Int): RepeatedTimeInterval = RepeatedTimeInterval(this, n)

operator fun MyDate.plus(interval: RepeatedTimeInterval) : MyDate = addTimeIntervals(interval.ti, interval.n)
