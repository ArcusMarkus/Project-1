package maze.core;

import java.util.*;

import core.Direction;
import core.Pos;

public class MazeExplorer {
	private Maze m;
	private Pos location;
	MazePath path;
	private TreeSet<Pos> treasureFound;
	private MazeExplorer goal;
	
	public MazeExplorer(Maze m, Pos location) {
		this.m = m;
		this.location = location;
		treasureFound = new TreeSet<>();
		path = new MazePath(m.getStart().getX(), m.getStart().getY());
	}
	
	public Pos getLocation() {return location;}

	public Set<Pos> getAllTreasureFromMaze() {
		return m.getTreasures();
	}

	public Set<Pos> getAllTreasureFound() {
		return treasureFound;
	}

	public int getNumTreasuresFound() {
		return treasureFound.size();
	}

	public MazeExplorer getGoal() {
		if (goal == null) {
			goal = m.getGoal();
		}
		return goal;
	}

	public ArrayList<MazeExplorer> getSuccessors() {
		ArrayList<MazeExplorer> result = new ArrayList<>();
		ArrayList<Pos> neighbors = m.getNeighbors(location);

		for (Pos neighbor : neighbors){
			if (location.getManhattanDist(neighbor) > 1)
				continue;
			if (m.blocked(location, Direction.between(location, neighbor)))
				continue;
			if (path != null && path.hasVisited(neighbor))
				continue;

			MazePath newPath = new MazePath(path.getStart().getX(), path.getStart().getY());
			for (int i = 0; i < path.getLength(); i++){
				newPath.append(path.getNth(i));
			}
			newPath.append(neighbor);

			MazeExplorer successor = new MazeExplorer(m, neighbor);
			successor.path = newPath;
			successor.treasureFound = new TreeSet<>(this.treasureFound);
			if (m.isTreasure(neighbor)){
				successor.treasureFound.add(neighbor);
			}

			result.add(successor);
		}
		return result;
	}
	
	public void addTreasures(Collection<Pos> treasures) {
		treasureFound.addAll(treasures);
	}
	
	public String toString() {
		StringBuilder treasures = new StringBuilder();
		for (Pos t: treasureFound) {
			treasures.append(";");
			treasures.append(t.toString());
		}
		return "@" + location.toString() + treasures;
	}
	
	@Override
	public int hashCode() {return toString().hashCode();}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof MazeExplorer that) {
			return this.location.equals(that.location) && this.treasureFound.equals(that.treasureFound);
		} else {
			return false;
		}
	}

	public boolean achievesGoal() {
		return this.equals(getGoal());
	}

	public Maze getM() {
		return m;
	}
}
