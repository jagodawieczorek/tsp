package tsp;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Path class
 *
 * @author Jagoda Wieczorek
 */
public class Path {
	private final ArrayList<Integer> order;

	private final TreeMap<Integer, Place> places;

	private final ArrayList<Place> path;

	public Path(final TreeMap<Integer, Place> places, final ArrayList<Integer> order) {
		this.places = places;
		this.order = order;
		this.path = new ArrayList<>();

		for (final Integer index: order) {
			this.path.add(places.get(index));
		}
	}

	public ArrayList<Place> getPath() {
		return this.path;
	}
}
