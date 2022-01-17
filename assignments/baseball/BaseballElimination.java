import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BaseballElimination {

  private String[] teams;
  private int[] w;
  private int[] l;
  private int[] r;
  private int[][] g;

  public BaseballElimination(String filename) {
    In in = new In(filename);
    int numberOfTeams = in.readInt();
    if (numberOfTeams <= 0) {
      throw new IllegalArgumentException();
    }
    teams = new String[numberOfTeams];
    w = new int[numberOfTeams];
    l = new int[numberOfTeams];
    r = new int[numberOfTeams];
    g = new int[numberOfTeams][numberOfTeams];

    for (int i = 0; i < numberOfTeams; i++) {
      teams[i] = in.readString();
      w[i] = in.readInt();
      l[i] = in.readInt();
      r[i] = in.readInt();

      for (int j = 0; j < numberOfTeams; j++) {
        g[i][j] = in.readInt();
      }
    }
  }

  public int numberOfTeams() {
    return teams.length;
  }

  public Iterable<String> teams() {
    return Arrays.asList(teams);
  }

  public int wins(String team) {
    return w[findTeamIndex(team)];
  }

  private int findTeamIndex(String team) {
    for (int i = 0; i < teams.length; i++) {
      if (team.equals(teams[i])) {
        return i;
      }
    }
    throw new IllegalArgumentException();
  }

  public int losses(String team) {
    return l[findTeamIndex(team)];
  }

  public int remaining(String team) {
    return r[findTeamIndex(team)];
  }

  public int against(String team1, String team2) {
    return g[findTeamIndex(team1)][findTeamIndex(team2)];
  }

  public boolean isEliminated(String team) {
    return false;
  }

  public Iterable<String> certificateOfElimination(String team) {
    return null;
  }

  public static void main(String[] args) {
    BaseballElimination division = new BaseballElimination(args[0]);
    for (String team : division.teams()) {
      if (division.isEliminated(team)) {
        StdOut.print(team + " is eliminated by the subset R = { ");
        for (String t : division.certificateOfElimination(team)) {
          StdOut.print(t + " ");
        }
        StdOut.println("}");
      } else {
        StdOut.println(team + " is not eliminated");
      }
    }
  }
}
