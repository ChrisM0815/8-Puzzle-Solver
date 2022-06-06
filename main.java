import java.util.*;

public class main {
    public static void main(String[] args) {
        //Starting state of the 8Puzzle
        //int[][] start = {{1,2,3},{4,5,6},{7,8,0}};//depth 0
        //int[][] start = {{1,2,3},{4,5,6},{7,0,8}};//depth 1
        //int[][] start = {{1,2,0},{4,5,3},{7,8,6}};//depth 2 Tiefensuche finds a solution here
        //int[][] start = {{1,2,3},{4,8,5},{7,6,0}};//depth 5
        //int[][] start = {{1,8,2},{0,4,3},{7,6,5}};//depth 9
        int[][] start = {{2, 0, 4}, {6, 7, 1}, {8, 5, 3}};//depth 23
        //int[][] start = {{8,6,7},{2,5,4},{3,0,1}};//depth 31

        //This List holds only the starting node
        ArrayList<Knoten> knotenliste = new ArrayList<>();
        knotenliste.add(new Knoten());
        knotenliste.get(0).setField(start);
        Scanner input = new Scanner(System.in);

        /*Algorithms
         * By default the Algorithms will check for cycles
         * when creating new child nodes
         * The code to do this without checking for cycles is
         * commented out in Implementations.
         */

        System.out.println("Solving 8 Puzzle");
        try {
            System.out.println("===============Running A*-Search with Heuristik 2=======================");
            printParents(asearch(new KnotenH(start, false)));
            System.out.println("===============Running Iterative Deepening==============================");
            iterativeDeepening(knotenliste.get(0));
            System.out.println("===============Running A*-Search with Heuristik 1=======================");
            printParents(asearch(new KnotenH(start, true)));
            System.out.println("===============Running Breitensuche=====================================");
            printParents(breitensuche(knotenliste));
            System.out.println("===============Running Tiefensuche======================================");
            tiefensuche(knotenliste.get(0));
        }
        catch (OutOfMemoryError e)
        {
            System.out.println("Run out of Memory");
            System.exit(-1);
        }
        catch (StackOverflowError e)
        {
            System.out.println("Stackoverflow");
            System.exit(-1);
        }

    }

    public static void printParents(Knoten knoten) {
        while (knoten != null) {
            knoten.printKnoten();
            knoten = knoten.getParent();
        }
    }

    //Checks if the Knoten is in the goal state
    private static boolean isGoalState(Knoten knoten) {
        int[][] goal = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        return Arrays.deepEquals(knoten.getField(), goal);
    }

    //Implementation of Breitensuche
    private static Knoten breitensuche(ArrayList<Knoten> knotenliste) {
        ArrayList<Knoten> neueKnoten = new ArrayList<Knoten>();
        System.out.println("Breitensuche: Checking new Level with " + knotenliste.size() + " Nodes");
        for (Knoten i : knotenliste) {
            if (isGoalState(i)) {
                System.out.println("Breitensuche: Solution found!");
                return i;
            }
            //neueKnoten.addAll(i.getChildren());//Childnodes without checking for cycles
            neueKnoten.addAll(i.getChildrenNoCycle());//Childnodes with checking for cycles
        }
        if (!neueKnoten.isEmpty())
            return breitensuche(neueKnoten);
        else {
            System.out.println("Breitensuche: Keine Lösung gefunden!");
            return null;
        }
    }

    /*Implementation of Tiefensuche
     *Doesn't work in most cases if there is no check for cycles
     * when creating childnodes
     */
    private static boolean tiefensuche(Knoten knoten) {
        ArrayList<Knoten> neueKnoten = new ArrayList<Knoten>();
        //System.out.println("Tiefensuche: Checking new Node:");
        //knoten.printKnoten();
        if (isGoalState(knoten)) {
            System.out.println("Tiefensuche: Solution found!");
            printParents(knoten);
            return true;
        }
        //neueKnoten.addAll(knoten.getChildren());//Childnodes without checking for cycles
        neueKnoten.addAll(knoten.getChildrenNoCycle());//Childnodes with checking for cycles
        while (!neueKnoten.isEmpty()) {
            boolean solved = tiefensuche(neueKnoten.get(0));
            if (solved) return true;
            neueKnoten.remove(0);
        }
        System.out.println("Tiefensuche: No Solution found!");
        return false;
    }

    /*Implementation of tiefensuche with Tiefenschranke
     * Used in IterativeDeepening
     */
    private static boolean tiefensuche(Knoten knoten, int depth, int schranke) {
        ArrayList<Knoten> neueKnoten = new ArrayList<Knoten>();
        //System.out.println("Tiefensuche: Checking new Node:");
        //knoten.printKnoten();
        if (isGoalState(knoten)) {
            System.out.println("IterativeDeepening: Solution found!");
            printParents(knoten);
            return true;
        }
        //neueKnoten.addAll(knoten.getChildren());//Childnodes without checking for cycles
        neueKnoten.addAll(knoten.getChildrenNoCycle());//Childnodes with checking for cycles
        while (!neueKnoten.isEmpty() && depth < schranke) {
            boolean solved = tiefensuche(neueKnoten.get(0), depth + 1, schranke);
            if (solved) return true;
            neueKnoten.remove(0);
        }
        return false;
    }

    //Implementation of IterativeDeepening
    private static void iterativeDeepening(Knoten knoten) {
        int schranke = 0;
        boolean solved;
        do {
            System.out.println("IterativeDeepening: Checking down to level " + schranke);
            solved = tiefensuche(knoten, 0, schranke);
            schranke++;
        } while (solved == false);
    }

    //Implementation of A*-Search
    private static KnotenH asearch(KnotenH knoten) {
        System.out.println("A*-Search: Search running!");
        ArrayList<KnotenH> knotenliste = new ArrayList<>();
        knotenliste.add(knoten);
        while (true) {
            //current.printKnoten();//Uncomment this to see every Node the A*-Search looks at
            if (knotenliste.isEmpty()) {
                System.out.println("A*-Search: No solution found!");
                return null;
            }
            KnotenH current = knotenliste.get(0);
            knotenliste.remove(0);
            if (isGoalState(current)) {
                System.out.println("A*-Search: Solution found!");
                return current;
            }

            //Sorting the childnodes into the Nodelist

            for (KnotenH k:current.getChildrenNoCycleH()) {
                int i = 0;
                if (knotenliste.isEmpty()) knotenliste.add(k);

                else {
                    while (i < knotenliste.size() && knotenliste.get(i).getF() < k.getF()) i++;//get index of Element with bigger F than the one to add
                    if(i == knotenliste.size()) knotenliste.add(k);
                    else knotenliste.add(i,k);

                }
            }

            //knotenliste.addAll(current.getChildrenNoCycleH());
            //knotenliste.sort(KnotenH::compareTo);



        }

    }


}
