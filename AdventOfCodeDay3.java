import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AdventOfCodeDay3 {

  // Notes
  // How many square inches of fabric are within two or more claims?
  // Need to read from the input and populate a model that contains all the info.
  // Go through each square inch of fabric and run through every claim and see if any overlaps with it. this is O(n^2)


  // Get inches from top plus max height to get height of total.
  // Get inches from left plus max width to get width of total.


  public static void main(String[] args) throws IOException {
    System.out.println(partOne(getInputValues()));
    System.out.println(partTwo(getInputValues()));

  }

  private static int partOne(List<Claim> claims) {
    int totalClaims = 0;

    for (int x = 0; x < 1000; x++) {
      for (int y = 0; y < 1000; y++) {
        if (isTwoOrMoreClaims(claims, x, y)) {
          totalClaims++;
        }
      }
    }

    return totalClaims;
  }

  private static int partTwo(List<Claim> claims) {
    for (Claim claim : claims) {
      if (!doesThisClaimOverlap(claims, claim)) {
        return claim.id;
      }
    }

    return 0;
  }

  private static boolean isTwoOrMoreClaims(List<Claim> claims, int x, int y) {
    int totalClaims = 0;

    for (Claim claim : claims) {
      if ((claim.inchesFromLeftEdge < x) && (x <= claim.inchesFromLeftEdge + claim.width) &&
          (claim.inchesFromTopEdge < y) && (y <= claim.inchesFromTopEdge + claim.height)) {
        totalClaims++;
        if (totalClaims > 1) {
          return true;
        }
      }
    }

    return false;
  }

  private static boolean doesThisClaimOverlap(List<Claim> claims, Claim claimToCheck) {

    final int claimToCheckXStart = claimToCheck.inchesFromLeftEdge;
    final int claimToCheckXFinish = claimToCheckXStart + claimToCheck.width;

    final int claimToCheckYStart = claimToCheck.inchesFromTopEdge;
    final int claimToCheckYFinish = claimToCheckYStart + claimToCheck.height;

    final List<Integer> claimToCheckXRange = IntStream.range(claimToCheckXStart, claimToCheckXFinish).boxed().collect(Collectors.toList());
    final List<Integer> claimToCheckYRange = IntStream.range(claimToCheckYStart, claimToCheckYFinish).boxed().collect(Collectors.toList());

    for (Claim claim : claims) {
      if(claimToCheck.id == claim.id) {
        continue; // Don't check against itself.
      }

      final int claimXStart = claim.inchesFromLeftEdge;
      final int claimXFinish = claimXStart + claim.width;

      final int claimYStart = claim.inchesFromTopEdge;
      final int claimYFinish = claimYStart + claim.height;

      final List<Integer> claimXRange = IntStream.range(claimXStart, claimXFinish).boxed().collect(Collectors.toList());
      final List<Integer> claimYRange = IntStream.range(claimYStart, claimYFinish).boxed().collect(Collectors.toList());

      boolean overlapX = !Collections.disjoint(claimToCheckXRange, claimXRange);
      boolean overlapY = !Collections.disjoint(claimToCheckYRange, claimYRange);

      if (overlapX && overlapY) {
        return true;
      }

    }

    return false;
  }


  public static class Claim {
    int id;

    int inchesFromLeftEdge;
    int inchesFromTopEdge;

    int width;
    int height;

    public Claim(int id, int inchesFromLeftEdge, int inchesFromTopEdge, int width, int height) {
      this.id = id;
      this.inchesFromLeftEdge = inchesFromLeftEdge;
      this.inchesFromTopEdge = inchesFromTopEdge;
      this.width = width;
      this.height = height;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Claim claim = (Claim) o;
      return id == claim.id;
    }

    @Override
    public int hashCode() {
      return Objects.hash(id);
    }
  }

  private static List<Claim> getInputValues() throws IOException {
    final File file = new File("/Users/richard/Desktop/input.txt");
    final BufferedReader br = new BufferedReader(new FileReader(file));

    List<Claim> inputList = new ArrayList<>();

    String stringValue;

    while ((stringValue = br.readLine()) != null) {
      inputList.add(stringToClaim(stringValue));
    }
    br.close();

    return inputList;

  }

  private static Claim stringToClaim(String str) {
    int id = Integer.parseInt((str.substring(1, str.indexOf("@"))).replaceAll("\\s+", ""));
    int inchesFromLeftEdge = Integer.parseInt(str.substring(str.indexOf("@") + 1, str.indexOf(",")).replaceAll("\\s+", ""));
    int inchesFromTopEdge = Integer.parseInt(str.substring(str.indexOf(",") + 1, str.indexOf(":")).replaceAll("\\s+", ""));
    int width = Integer.parseInt(str.substring(str.indexOf(":") + 1, str.indexOf("x")).replaceAll("\\s+", ""));
    int height = Integer.parseInt(str.substring(str.indexOf("x") + 1).replaceAll("\\s+", ""));

    return new Claim(id, inchesFromLeftEdge, inchesFromTopEdge, width, height);
  }

}

