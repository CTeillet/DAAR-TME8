package algorithms;

import java.awt.Point;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultTeam {

    public ArrayList<Point> calculFVS(ArrayList<Point> points, int edgeThreshold) {
        ArrayList<Point> fvs = new ArrayList<Point>();
        Map<Point, List<Point>> v = transformationList(points, edgeThreshold);
        greedyAlgotrithm(v);
        ArrayList<Point> res = new ArrayList<>(v.keySet());
        return res;
    }

    private List<List<Point>> findCycles(Map<Point, List<Point>> pointsToNeighbors) {
        List<List<Point>> cycles = new ArrayList<>();
        for (Point point : pointsToNeighbors.keySet()) {
            Set<Point> visited = new HashSet<>();
            List<Point> path = new ArrayList<>();
            if (findCyclesUtils(point, pointsToNeighbors, visited, path)) {
                cycles.add(path);
            }
        }
        return cycles;
    }

    private boolean findCyclesUtils(Point point, Map<Point, List<Point>> pointsToNeighbors, Set<Point> visited, List<Point> path) {
        visited.add(point);
        path.add(point);
        for (Point neighbor : pointsToNeighbors.get(point)) {
            if (visited.contains(neighbor)) {
                if (path.contains(neighbor)) {
                    return true;
                }
            } else {
                if (findCyclesUtils(neighbor, pointsToNeighbors, visited, path)) {
                    return true;
                }
            }
        }
        path.remove(point);
        return false;
    }

    public void greedyAlgotrithm(Map<Point, List<Point>> pointsToNeighbors) {
        List<List<Point>> cycles = findCycles(pointsToNeighbors);
        while(!cycles.isEmpty()) {
            for (List<Point> cycle : cycles) {
                Point point = maxDegree(cycle, pointsToNeighbors);
                removePoint(point, pointsToNeighbors);
                cycles.stream().filter(c -> c.contains(point)).forEach(c -> c.remove(point));
                cycles = cycles.stream().filter(c -> c.size()>1).collect(Collectors.toList());
            }
        }

    }

    private void removePoint(Point point, Map<Point, List<Point>> pointsToNeighbors) {
        if (point == null)return;
        pointsToNeighbors.remove(point);
        for (Point neighbor : pointsToNeighbors.keySet()) {
            pointsToNeighbors.get(neighbor).remove(point);
        }
    }

    private Point maxDegree(List<Point> cycle, Map<Point, List<Point>> pointsToNeighbors) {
        int max = 0;
        Point point = null;
        for (Point p : cycle) {
            if (pointsToNeighbors.get(p)!=null && max < pointsToNeighbors.get(p).size()) {
                max = pointsToNeighbors.get(p).size();
                point = p;
            }
        }
        return point;
    }

    public Map<Point, List<Point>> transformationList(ArrayList<Point> points, int edgeThreshold) {
        Map<Point, List<Point>> pointsToNeighbors = new HashMap<>();
        for (Point point : points) {
            for (Point neighbor : points) {
                if (point!=neighbor && point.distance(neighbor) <= edgeThreshold) {
                    if(pointsToNeighbors.get(point) == null) {
                        List<Point> temp = new ArrayList<>(List.of(neighbor));
                        pointsToNeighbors.put(point, temp);
                    }else{
                        pointsToNeighbors.get(point).add(neighbor);
                    }
                    if(pointsToNeighbors.get(neighbor) == null) {
                        List<Point> temp = new ArrayList<>(List.of(point));
                        pointsToNeighbors.put(neighbor, temp);
                    }else{
                        pointsToNeighbors.get(neighbor).add(point);
                    }
                }
            }
        }
        return pointsToNeighbors;
    }


}
