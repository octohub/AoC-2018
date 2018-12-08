import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.*

fun main(args: Array<String>) {
  val timeStamps = prepareTimestamps()
  val guardToSleepMinutes = mapGuardToSleepMinutes(timeStamps)
  val mostSleepyGuard = getMostSleepyGuard(guardToSleepMinutes)
  val mostSleepyMinute = getMostSleepyMinute(mostSleepyGuard, guardToSleepMinutes)
  val partOneAnswer = mostSleepyGuard * mostSleepyMinute

  val guardsToSleepMinutes = guardsToSleepMinutes(guardToSleepMinutes)
  val partTwoAnswer = getGuardMostFrequentlyAsleepOnSameMinute(guardsToSleepMinutes)


  println("Part One: Most sleepy guard is $mostSleepyGuard. Most sleepy minute is $mostSleepyMinute. " +
      "Result is $partOneAnswer. " +
      "Part Two: $partTwoAnswer.")

}

fun getGuardMostFrequentlyAsleepOnSameMinute(guardsToSleepMinutes: Map<Int, Map<Int, Int>>): Int{
  var guardId = -1
  var maxMinute = -1
  var maxOccurrences = -1

  for (guard in guardsToSleepMinutes) {
    val minutes = guard.value
    for (minute in minutes) {
      val minuteToCheck = minute.key
      val minuteToCheckTotalOccurrences = minute.value

      assert(minuteToCheck in 0..60)
      assert(minuteToCheckTotalOccurrences >0)

      if(minuteToCheckTotalOccurrences > maxOccurrences) {
        maxOccurrences = minuteToCheckTotalOccurrences
        maxMinute = minuteToCheck
        guardId = guard.key
      }
    }
  }
  return guardId * maxMinute
}

fun guardsToSleepMinutes(guardToSleepMinutes: Map<Int, List<Int>>): Map<Int, Map<Int, Int>> {
  val guardsToSleepMinutes: MutableMap<Int, MutableMap<Int, Int>> = mutableMapOf()

  for (guard in guardToSleepMinutes) {
    val minutesAsleep = guard.value
    val frequencyOfMinutesAsleep: MutableMap<Int, Int> = mutableMapOf()

    for (minute in minutesAsleep) {
      val currentAmount = frequencyOfMinutesAsleep[minute]
      if (currentAmount == null) {
        frequencyOfMinutesAsleep[minute] = 1
      } else {
        frequencyOfMinutesAsleep[minute] = currentAmount + 1
      }
    }

    guardsToSleepMinutes[guard.key] = frequencyOfMinutesAsleep

  }

  return guardsToSleepMinutes
}

fun getMostSleepyMinute(mostSleepyGuard: Int, guardToSleepMinutes: Map<Int, List<Int>>): Int {
  val minutesAsleep = guardToSleepMinutes[mostSleepyGuard]
  val frequencyOfMinutesAsleep: MutableMap<Int, Int> = mutableMapOf()

  assert(minutesAsleep != null)

  if (minutesAsleep != null) {
    for (minute in minutesAsleep) {
      val currentAmount = frequencyOfMinutesAsleep[minute]
      if (currentAmount == null) {
        frequencyOfMinutesAsleep[minute] = 1
      } else {
        frequencyOfMinutesAsleep[minute] = currentAmount + 1
      }
    }
  }

  // Max minute
  var maxSleepMinutes = -1
  var mostSleepyMinute = -1

  for (entry in frequencyOfMinutesAsleep) {
    val sleepMinutes = entry.value
    if (sleepMinutes > maxSleepMinutes) {
      mostSleepyMinute = entry.key
      maxSleepMinutes = sleepMinutes
    }
  }

  return mostSleepyMinute
}

private fun getMostSleepyGuard(guardToSleepMinutes: Map<Int, List<Int>>): Int {
  var maxSleepMinutes = -1
  var mostSleepyGuard = -1

  for (entry in guardToSleepMinutes) {
    val sleepMinutes = entry.value.size
    if (sleepMinutes > maxSleepMinutes) {
      mostSleepyGuard = entry.key
      maxSleepMinutes = sleepMinutes
    }
  }

  return mostSleepyGuard
}

private fun prepareTimestamps(): List<TimeStamp> {
  val timeStamps = toTimeStamps(getInputValues())
  timeStamps.sort()
  populateGuardIds(timeStamps)
  return timeStamps
}

private fun mapGuardToSleepMinutes(timeStamps: List<TimeStamp>): Map<Int, List<Int>> {
  val guardToSleepMinutes: MutableMap<Int, MutableList<Int>> = mutableMapOf()

  for (i in 0 until timeStamps.size) {
    val timeStamp = timeStamps[i]
    if (timeStamp.isAwake && i > 0 && !timeStamps[i - 1].isAwake) {
      populateRangeOfSleepMinutes(guardToSleepMinutes, timeStamps[i - 1], timeStamp)

    }
  }

  return guardToSleepMinutes
}

private fun populateRangeOfSleepMinutes(guardToTotalSleepMinutes: MutableMap<Int, MutableList<Int>>,
                                        asleepTimeStamp: TimeStamp, awakeTimeStamp: TimeStamp) {
  val guardId = asleepTimeStamp.guardId
  val sleepStart = asleepTimeStamp.minute
  val sleepEnd = awakeTimeStamp.minute

  for (i in sleepStart..sleepEnd) {
    if (guardToTotalSleepMinutes[guardId] == null) {
      guardToTotalSleepMinutes[guardId] = mutableListOf()
    }
    guardToTotalSleepMinutes[guardId]?.add(i)
  }
}

private fun populateGuardIds(timeStamps: MutableList<TimeStamp>) {
  var guardId = timeStamps[0].guardId

  for (i in 0 until timeStamps.size) {
    if (timeStamps[i].guardId > -1) {
      guardId = timeStamps[i].guardId
    }

    timeStamps[i].guardId = guardId
  }
}

private fun getInputValues(): List<String> {
  val file = File("/Users/richard/Desktop/input.txt")
  val br = BufferedReader(FileReader(file))

  val inputList = ArrayList<String>()

  var stringValue: String? = br.readLine()

  while (stringValue != null) {
    inputList.add(stringValue)
    stringValue = br.readLine()
  }
  br.close()
  return inputList
}

private fun toTimeStamps(input: List<String>): MutableList<TimeStamp> {

  /*
  [1518-11-01 00:00] Guard #10 begins shift
  [1518-11-01 00:05] falls asleep
  [1518-11-01 00:25] wakes up
   */

  val timeStamps = mutableListOf<TimeStamp>()
  for (str in input) {

    val yearStartIndex = 1
    val yearEndIndex = str.indexOf("-")

    val monthStartIndex = yearEndIndex + 1
    val monthEndIndex = str.indexOf("-", monthStartIndex)

    val dayStartIndex = monthEndIndex + 1
    val dayEndIndex = dayStartIndex + 2

    val hourStartIndex = dayEndIndex + 1
    val hourEndIndex = hourStartIndex + 2

    val minuteStartIndex = str.indexOf(":") + 1
    val minuteEndIndex = minuteStartIndex + 2

    val guardIdStartIndex = str.indexOf("#") + 1
    val guardIdEndIndex = str.indexOf("begins") - 1

    val year = Integer.parseInt(str.substring(yearStartIndex, yearEndIndex))
    val month = Integer.parseInt(str.substring(monthStartIndex, monthEndIndex))
    val day = Integer.parseInt(str.substring(dayStartIndex, dayEndIndex))
    val hour = Integer.parseInt(str.substring(hourStartIndex, hourEndIndex))
    val minute = Integer.parseInt(str.substring(minuteStartIndex, minuteEndIndex))
    val isAwake = !str.contains("asleep")

    var guardId = -1
    if (guardIdStartIndex > 0) {
      guardId = Integer.parseInt(str.substring(guardIdStartIndex, guardIdEndIndex))
    }

    timeStamps.add(TimeStamp(guardId, isAwake, year, month, day, hour, minute))
  }

  return timeStamps
}

class TimeStamp(var guardId: Int, val isAwake: Boolean, val year: Int, val month: Int, val day: Int,
                val hour: Int, val minute: Int) : Comparable<TimeStamp> {

  override fun compareTo(other: TimeStamp): Int {
    return when {
      this.year > other.year -> 1
      this.year < other.year -> -1
      this.month > other.month -> 1
      this.month < other.month -> -1
      this.day > other.day -> 1
      this.day < other.day -> -1
      this.hour > other.hour -> 1
      this.hour < other.hour -> -1
      this.minute > other.minute -> 1
      this.minute < other.minute -> -1
      else -> 0
    }

  }

}