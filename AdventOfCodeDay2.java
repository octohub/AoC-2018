import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdventOfCodeDay2 {

  // Get input values and put them in a list.
  // Iterate through list. Create a hashmap for each list and insert letters into it. Key = letter, Value = number of occurrences.
  // Iterate through Hashmap and determine if there are exactly two of any letter and three of any letter.
  // Keep a running tally of these and multiply them at the end.

  public static void main(String[] args) throws IOException {
    partOne(getInputValues());
    partTwo(getInputValues());
  }


  private static void partTwo(List<String> inputStrings) {

    for (String strOne :
        inputStrings) {

      boolean foundOneCharacterOff = false;
      Integer characterOffIndex = -1;
      for (String strTwo :
          inputStrings) {

        for (int i = 0; i < strOne.length(); i++) {

          if (strOne.charAt(i) != strTwo.charAt(i)) {
            if (foundOneCharacterOff) {
              break;
            } else {
              foundOneCharacterOff = true;
              characterOffIndex = i;
            }
          }

          if(i == strOne.length() - 1 && foundOneCharacterOff && characterOffIndex > -1) {

            StringBuilder sb = new StringBuilder(strOne);
            sb.deleteCharAt(characterOffIndex);
            String resultString = sb.toString();
            System.out.println("Common id = " + resultString);
            return;
          }
        }
        foundOneCharacterOff = false;
        characterOffIndex = -1;
      }
    }
  }

  private static void partOne(List<String> inputStrings) {

    int totalExactlyTwoOccurrences = 0;
    int totalExactlyThreeOccurrences = 0;

    for (String str :
        inputStrings) {

      final Map<String, Integer> map = new HashMap<>();

      for (char c : str.toCharArray()) {
        final String letter = Character.toString(c);

        if (map.containsKey(letter)) {
          Integer occurrences = map.get(letter);
          map.put(letter, occurrences + 1);
        } else {
          map.put(letter, 1);
        }
      }

      boolean containsExactlyTwo = false;
      boolean containsExactlyThree = false;
      for (Object value : map.values()) { // Must convert value to specific type.
        Integer totalOccurrences = (Integer) value;

        if (totalOccurrences == 2) {
          containsExactlyTwo = true;
        } else if (totalOccurrences == 3) {
          containsExactlyThree = true;
        }
      }

      if (containsExactlyTwo) {
        totalExactlyTwoOccurrences++;
      }

      if (containsExactlyThree) {
        totalExactlyThreeOccurrences++;
      }

    }

    System.out.println("Exactly two occurrences = " + totalExactlyTwoOccurrences);
    System.out.println("Exactly three occurrences = " + totalExactlyThreeOccurrences);
    System.out.println("Checksum = " + totalExactlyTwoOccurrences * totalExactlyThreeOccurrences);
  }

  private static List<String> getInputValues() throws IOException {
    final File file = new File("/Users/richard/Desktop/input.txt");
    final BufferedReader br = new BufferedReader(new FileReader(file));

    List<String> inputList = new ArrayList<>();

    String stringValue;

    while ((stringValue = br.readLine()) != null) {
      inputList.add(stringValue);
    }
    br.close();

    return inputList;

  }
}
