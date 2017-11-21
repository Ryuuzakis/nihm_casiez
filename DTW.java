import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.function.BiFunction;

public class DTW {


    public static double calculateDistance(Vector<Point2D> ref, Vector<Point2D> gesture) {
        BiFunction<Integer, Integer, Double> getCost = (r, c) -> ref.get(r).distance(gesture.get(c));
        List<Couple> predecessors = new ArrayList<>();

        int n = ref.size();
        int m = gesture.size();
        Matrix dtw = new Matrix(n, m);

        dtw.set(0, 0, 0);

        for (int r = 1; r < n; r++) {
            dtw.set(r, 0, dtw.get(r - 1, 0) + getCost.apply(r, 0));
        }

        for (int c = 1; c < m; c++) {
            dtw.set(0, c, dtw.get(0, c - 1) + getCost.apply(0, c));
        }

        for (int r = 1; r < n; r++) {
            for (int c = 1; c < m; c++) {
                double left = dtw.get(r - 1, c);
                double up = dtw.get(r, c - 1);
                double leftUp = dtw.get(r - 1, c - 1);

                double min = min(left, up, leftUp);
                if (min == left) predecessors.add(new Couple(r - 1, c));
                else if (min == up) predecessors.add(new Couple(r, c - 1));
                else if (min == leftUp) predecessors.add(new Couple(r - 1, c - 1));

                double cost = getCost.apply(r, c) + min;
                dtw.set(r, c, cost);
            }
        }

        return dtw.getLastElement();
    }

    private static double min(double a, double b, double c) {
        return Math.min(a, Math.min(b, c));
    }

    public static Vector<Point2D> preTreat(Vector<Point2D> points) {
        Vector<Point2D> res = new Vector<>();
        double xmin = Double.MAX_VALUE, ymin = Double.MAX_VALUE, xmax = Double.MIN_VALUE, ymax = Double.MIN_VALUE;

        for (Point2D p : points) {
            double x = p.getX(), y = p.getY();
            if (x < xmin) xmin = x;
            if (x > xmax) xmax = x;
            if (y < ymin) ymin = y;
            if (y > ymax) ymax = y;
        }

        double deltaX = xmax - xmin;
        double deltaY = ymax - xmin;

        System.out.println(xmin + " - " + xmax  + " - " + ymin  + " - " + ymax);

        for (Point2D p: points) {
            Point2D newPoint = new Point2D((p.getX() - xmin) / deltaX, (p.getY() - ymin) / deltaY);

            System.out.println("old point " + p + " new point " + newPoint);

            res.add(newPoint);
        }

        return res;
    }
}
