import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdventOfCodeDay1 {
  public static void main(String[] args) {
    try {
      List<Integer> frequencyList = getFrequencyList();
      int partOneAnswer = partOne(frequencyList);
      int partTwoAnswer = partTwo(frequencyList);

      System.out.println("Part One Answer is " + partOneAnswer);
      System.out.println("Part Two Answer is " + partTwoAnswer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static List<Integer> getFrequencyList() throws IOException {

    Map<Integer, Boolean> frequencyMap = new HashMap<>();
    File file = new File("/Users/richard/Desktop/input.txt");

    BufferedReader br = new BufferedReader(new FileReader(file));

    List<Integer> frequencyList = new ArrayList<>();

    String stringValue;

    while ((stringValue = br.readLine()) != null) {
      int intValue = Integer.parseInt(stringValue);
      frequencyList.add(intValue);
    }

    return frequencyList;

  }

  private static int partOne(List<Integer> frequencyList) {
    int totalFrequency = 0;

    for (int frequencyElement : frequencyList) {
      totalFrequency += frequencyElement;
    }

    return totalFrequency;
  }

  private static int partTwo(List<Integer> frequencyList) {
    Map<Integer, Boolean> frequencyMap = new HashMap<>();

    int totalFrequency = 0;
    frequencyMap.put(totalFrequency, true);

    while (true) {
      for (int frequencyElement : frequencyList) {
        totalFrequency += frequencyElement;
        if (frequencyMap.get(totalFrequency) != null && frequencyMap.get(totalFrequency)) {
          return totalFrequency;
        } else {
          frequencyMap.put(totalFrequency, true);
        }
      }
    }

  }
}
