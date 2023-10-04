import edu.princeton.cs.algs4.*;
import java.util.ArrayList;

public class BaseballElimination {
    private final int numberOfTeams;
    private final ArrayList<String> teams = new ArrayList<>();
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] gamesAgainst;
    private FlowNetwork flowNetwork;
    private ArrayList<String> subsetR;

    // set flow network
    private void setFlowNetwork(String eliminatedTeam, ArrayList<String> otherTeams) {
        int vertices = 2 + otherTeams.size(); // s and t vertices + other teams' vertices
        // other teams' games vertices
        int gamesVertices = 0;
        for (int i = 1; i < otherTeams.size(); i++) {
            gamesVertices += i;
        }
        vertices += gamesVertices; // add other teams' games vertices
        flowNetwork = new FlowNetwork(vertices);
        // set flow from s --> games vertices
        int x = 0;
        int n = 1;
        int y = n;
        int p = 1;
        for (int i = 0; i < otherTeams.size()-1; i++) {
            while (y < otherTeams.size()) {
                int gameFlow = against(otherTeams.get(x), otherTeams.get(y));
                flowNetwork.addEdge(new FlowEdge(0, p, gameFlow));
                y++;
                p++;
            }
            x++;
            n++;
            y=n;
        }
        // set flow from games vertices --> teams vertices
        int v = 1;
        int i = 1 + gamesVertices;
        int w = i+1;
        for (int j = 0; j < otherTeams.size()-1; j++) {
            while (w < gamesVertices + otherTeams.size() + 1) {
                flowNetwork.addEdge(new FlowEdge(v, i, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(v, w, Double.POSITIVE_INFINITY));
                v++;
                w++;
            }
            i++;
            w = i+1;
        }
        // set flow from team vertices --> t
        int teamVertex = gamesVertices + 1;
        for (String otherTeam : otherTeams) {
            flowNetwork.addEdge(new FlowEdge(teamVertex, flowNetwork.V()-1,
                    Math.abs(wins(eliminatedTeam) + remaining(eliminatedTeam) -
                            wins(otherTeam))));
            teamVertex++;
        }
    }

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        numberOfTeams = in.readInt();
        wins = new int[numberOfTeams()];
        losses = new int[numberOfTeams()];
        remaining = new int[numberOfTeams()];
        gamesAgainst = new int[numberOfTeams()][numberOfTeams()];
        int teamNumber = 0;
        while (in.hasNextLine()) {
            if (in.isEmpty()) {break;}
            teams.add(in.readString());
            wins[teamNumber] = in.readInt();
            losses[teamNumber] = in.readInt();
            remaining[teamNumber] = in.readInt();
            for (int i = 0; i < numberOfTeams(); i++) {
                gamesAgainst[teamNumber][i] = in.readInt();
            }
            teamNumber++;
        }
    }

    // number of teams
    public int numberOfTeams() {
        return numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return teams;
    }

    // number of wins for given team
    public int wins(String team) {
        if (team == null) {throw new IllegalArgumentException();}
        return wins[teams.indexOf(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (team == null) {throw new IllegalArgumentException();}
        return losses[teams.indexOf(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (team == null) {throw new IllegalArgumentException();}
        return remaining[teams.indexOf(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (team1 == null || team2 == null) {throw new IllegalArgumentException();}
        if (team1.equals(team2)) {
            System.out.println("Team " + team1 + " cannot play against itself");
            throw new IllegalArgumentException();
        }
        return gamesAgainst[teams.indexOf(team1)][teams.indexOf(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (team == null) {throw new IllegalArgumentException();}
        subsetR = new ArrayList<>();
        // trivial elimination
        for (String otherTeam : teams()) {
            if (wins(team) + remaining(team) < wins(otherTeam)) {
                subsetR.add(otherTeam);
            }
        }
        if (!subsetR.isEmpty()) {return true;}
        // nontrivial elimination
        ArrayList<String> otherTeams = new ArrayList<>();
        for (String otherTeam : teams()) {
            if (otherTeam.equals(team)) {continue;}
            otherTeams.add(otherTeam);
        }
        setFlowNetwork(team, otherTeams);
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, flowNetwork.V()-1);
        int index = 0;
        for (int v = flowNetwork.V()-numberOfTeams(); v < flowNetwork.V()-1; v++ ) {
            if (fordFulkerson.inCut(v)) {
                subsetR.add(otherTeams.get(index));
            }
            index++;
        }
        for (FlowEdge edge : flowNetwork.adj(0)) {
            if (edge.residualCapacityTo(edge.to()) != 0) {
                return true;
            }
        }
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (team == null) {throw new IllegalArgumentException();}
        if (subsetR.isEmpty()) {
            System.out.println("First you need to check if " + team + " is eliminated!");
            return null;
        }
        return subsetR;
    }

    // output format
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("teams5.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}