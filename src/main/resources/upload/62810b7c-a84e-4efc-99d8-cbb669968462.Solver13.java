/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;


public class Solver {
    private int moves = 0;
    private SearchNode finalNode;
    private Stack<Board> boards;


    public Solver(Board initial) {
        //if (!initial.isSolvable()) throw new IllegalArgumentException("Unsolvable puzzle");

        // this.initial = initial;
        MinPQ<SearchNode> minPQ = new MinPQ<>();

        Board dequeuedBoard = initial;
        Board previous = null;
        SearchNode dequeuedNode = new SearchNode(initial, 0, null);
        Iterable<Board> boards;

        while (!dequeuedBoard.isGoal()) {
            boards = dequeuedBoard.neighbors();
            moves++;

            for (Board board : boards) {
                if (!board.equals(previous)) {
                    minPQ.insert(new SearchNode(board, moves, dequeuedNode));
                }
            }

            previous = dequeuedBoard;
            dequeuedNode = minPQ.delMin();
            dequeuedBoard = dequeuedNode.current;
        }
        finalNode = dequeuedNode;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (boards != null) return boards.size() - 1;
        solution();
        return boards.size() - 1;
    }

    public Iterable<Board> solution() {
        if (boards != null) return boards;
        boards = new Stack<Board>();
        SearchNode pointer = finalNode;
        while (pointer != null) {
            boards.push(pointer.current);
            pointer = pointer.previous;
        }
        return boards;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final int priority;
        private final SearchNode previous;
        private final Board current;


        public SearchNode(Board current, int moves, SearchNode previous) {
            this.current = current;
            this.previous = previous;
            this.priority = moves + current.manhattan();
        }

        @Override
        public int compareTo(SearchNode that) {
            int cmp = this.priority - that.priority;
            return Integer.compare(cmp, 0);
        }


    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        Solver solver = new Solver(initial);
        System.out.println(solver.moves());
    }
}

