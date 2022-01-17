import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BaseballElimination {

  private final String[] teams;
  private final int[] w;
  private final int[] l;
  private final int[] r;
  private final int[][] g;
  private final ArrayList<String>[] certificateOfElimination;
  private final int numOfVertices;

  public BaseballElimination(String filename) {
    In in = new In(filename);
    int numberOfTeams = in.readInt();
    if (numberOfTeams <= 0) {
      throw new IllegalArgumentException();
    }

    teams = new String[numberOfTeams];
    numOfVertices = 1 + (numberOfTeams - 1) * (numberOfTeams - 2) + (numberOfTeams - 1) + 1;

    w = new int[numberOfTeams];
    l = new int[numberOfTeams];
    r = new int[numberOfTeams];
    g = new int[numberOfTeams][numberOfTeams];
    certificateOfElimination = new ArrayList[numberOfTeams];

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
    throw new IllegalArgumentException("no such team: " + team);
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
    return certificateOfElimination(team).iterator().hasNext();
  }

  public Iterable<String> certificateOfElimination(String team) {
    int index = findTeamIndex(team);
    if (certificateOfElimination[index] == null && !isTriviallyEliminated(index)) {
      runFordFulkerson(index);
    }
    return certificateOfElimination[index];
  }

  private void runFordFulkerson(int index) {
    final FlowNetwork flowNetwork = new FlowNetwork(numOfVertices);
    final int s = 0;
    final int t = numOfVertices - 1;

    int v = 1;
    for (int i = 0; i < teams.length; i++) {
      if (i != index) {
        for (int j = i + 1; j < teams.length; j++) {
          if (j != index) {
            flowNetwork.addEdge(new FlowEdge(s, v, g[i][j]));
            flowNetwork.addEdge(new FlowEdge(v, vertexOfTeam(i, index), Double.POSITIVE_INFINITY));
            flowNetwork.addEdge(new FlowEdge(v, vertexOfTeam(j, index), Double.POSITIVE_INFINITY));
            v++;
          }
        }
      }
    }

    final int maxWins = w[index] + r[index];
    for (int i = 0; i < teams.length; i++) {
      if (i != index) {
        v = vertexOfTeam(i, index);
        flowNetwork.addEdge(new FlowEdge(vertexOfTeam(i, index), t, maxWins - w[i]));
      }
    }

    FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, s, t);
    for (int i = 0; i < teams.length; i++) {
      if (i != index) {
        v = vertexOfTeam(i, index);

        if (fordFulkerson.inCut(v)) {
          certificateOfElimination[index].add(teams[i]);
        }
      }
    }
  }

  private int vertexOfTeam(int i, int index) {
    int v = numOfVertices - teams.length + i;
    if (i > index) {
      v--;
    }
    return v;
  }

  private boolean isTriviallyEliminated(int index) {
    final int maxWins = w[index] + r[index];
    certificateOfElimination[index] = new ArrayList<>();
    for (int i = 0; i < teams.length; i++) {
      if (i != index) {
        if (w[i] > maxWins) {
          certificateOfElimination[index].add(teams[i]);
        }
      }
    }
    return certificateOfElimination[index].size() > 0;
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
