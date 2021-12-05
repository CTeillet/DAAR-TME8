package algorithms;

import java.awt.Point;
import java.util.*;

public class DefaultTeam {

    public ArrayList<Point> calculFVS(ArrayList<Point> points, int edgeThreshold) {
        ArrayList<Point> res = new ArrayList<Point>();
        //init graph
        Map<Point, List<Point>> graph = transformationList(points, edgeThreshold);
        // search a cycle
        List<Point> cycle = findCycle(graph);

        int c =0;
        while (cycle != null){
            System.err.println(c);
            c++;
            // break cycle
            Point point = maxDegree(cycle, graph);
            removePoint(point, graph);
            res.add(point);
            // search a new cycle
            cycle = findCycle(graph);
        }

        return res;
    }

    private List<Point> findCycle(Map<Point, List<Point>> graph) {
        //List<Point> cycle = new ArrayList<Point>();
        for (Point point : graph.keySet()) {
            Set<Point> visited = new HashSet<>();
            List<Point> path = new ArrayList<>();
            if (findCycleUtils(point, null, graph, visited, path)) {
                return isolateCycle(graph, path);
            }
        }
        return null;
    }

    private List<Point> isolateCycle(Map<Point, List<Point>> graph, List<Point> path) {
        System.out.println("taille de path : " + path.size());
        ArrayList<Point> cycle = new ArrayList<>();
        Point lastPoint = null;
        for(int i = 0; i < 3; i++){
            lastPoint = path.remove(path.size() -1 );
            cycle.add(lastPoint);
        }
        while(path.size() > 0 && !isNeighbour(graph, path.get(0), lastPoint)) {
            lastPoint = path.remove(path.size() -1 );
            cycle.add(lastPoint);
        }
        return cycle;
    }

    private boolean isNeighbour(Map<Point, List<Point>> graph, Point p1, Point p2) {
        return graph.get(p1).contains(p2);
    }

    private boolean findCycleUtils(Point point, Point previous, Map<Point, List<Point>> graph, Set<Point> visited, List<Point> path) {
        visited.add(point);
        path.add(point);
        if(graph.get(point)==null)return false;
        for (Point neighbor : graph.get(point)) {
            //System.out.println("dehors");
            if (!neighbor.equals(previous)){
                //System.out.println("dedans");
                if (visited.contains(neighbor)) {
                    if (path.contains(neighbor)) {
                        return true;
                    }
                } else {
                    if (findCycleUtils(neighbor, point, graph, visited, path)) {
                        return true;
                    }
                }
            }
        }
        path.remove(point);
        return false;
    }

    /*public void greedyAlgotrithm(Map<Point, List<Point>> pointsToNeighbors) {
        List<List<Point>> cycles = findCycle(pointsToNeighbors);
        System.out.println("nb cycles IN : " + cycles.size());
        while(!cycles.isEmpty()) {
            for (List<Point> cycle : cycles) {
                Point point = maxDegree(cycle, pointsToNeighbors);
                removePoint(point, pointsToNeighbors);
                cycles.stream().filter(c -> c.contains(point)).forEach(c -> c.remove(point));
                cycles = cycles.stream().filter(c -> c.size()>1).collect(Collectors.toList());
            }
            //cycles = findCycles(pointsToNeighbors);
            System.out.println(cycles.size());
        }

    }*/

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
