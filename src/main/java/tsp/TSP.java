package tsp;

import static tsp.TSP.DisplayDataType.COORD_DISPLAY;
import static tsp.TSP.EdgeWeightType.GEO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Symmetric Travelling salesman problem
 *
 * @author Jagoda Wieczorek
 */
public class TSP {
	private static final Logger LOGGER = Logger.getLogger(TSP.class.getName());

	private static final ArrayList<EdgeWeightType> ALLOWED_EDGE_WEIGHT_TYPES = new ArrayList<>(List.of(GEO));

	private static final ArrayList<DisplayDataType> ALLOWED_DISPLAY_DATA_TYPES = new ArrayList<>(List.of(COORD_DISPLAY));

	private String sourceFilename;

	private final TreeMap<Integer, Place> places;

	private String name;

	private DisplayDataType displayDataType;

	private EdgeWeightType edgeWeightType;

	private Integer startingPlace;

	public TSP() {
		this.places = new TreeMap<>();
	}

	/**
	 * @param sourceFilename
	 *                           File with places' coordinates
	 */
	public TSP(final String sourceFilename) {
		this();
		this.setFromFile(sourceFilename);
		this.setStartingPlace();
		this.calculateAndAssignDistances();
	}

	/**
	 * @param sourceFilename
	 *                           File with places' coordinates
	 */
	public void setFromFile(final String sourceFilename) {
		this.sourceFilename = sourceFilename;

		try (BufferedReader reader = new BufferedReader(new FileReader(sourceFilename))) {
			String line;

			while ((line = reader.readLine()) != null) {
				if (line.startsWith("NAME:")) {
					this.name = line.replace("NAME:", "").trim();
				} else if (line.startsWith("TYPE:")) {
					final String type = line.replace("TYPE:", "").trim();

					if (!type.equalsIgnoreCase("TSP")) {
						throw new IllegalArgumentException("Source file can't be of type: " + type);
					}
				} else if (line.startsWith("EDGE_WEIGHT_TYPE:")) {
					this.edgeWeightType = EdgeWeightType.valueOf(line.replace("EDGE_WEIGHT_TYPE:", "").trim());

					if (!ALLOWED_EDGE_WEIGHT_TYPES.contains(this.edgeWeightType)) {
						throw new IllegalArgumentException(this.edgeWeightType + " edge weight type is not allowed to use");
					}
				} else if (line.startsWith("DISPLAY_DATA_TYPE:")) {
					this.displayDataType = DisplayDataType.valueOf(line.replace("DISPLAY_DATA_TYPE:", "").trim());

					if (!ALLOWED_DISPLAY_DATA_TYPES.contains(this.displayDataType)) {
						throw new IllegalArgumentException(ALLOWED_DISPLAY_DATA_TYPES + " display data type is not allowed to use");
					}
				} else if (line.trim().equals("NODE_COORD_SECTION")) {
					String place;
					while ((place = reader.readLine()) != null) {
						final String[] placeData = place.trim().split(" ");
						this.places.put(Integer.parseInt(placeData[0]),
								new Place(Integer.parseInt(placeData[0]), Float.parseFloat(placeData[1]), Float.parseFloat(placeData[2])));
					}
				}
			}

		} catch (IOException | IllegalArgumentException e) {
			LOGGER.log(Level.FINE, e.toString());
		}
	}

	public void setStartingPlace() {
		this.startingPlace = this.getPlaces().firstKey();
	}

	public void calculateAndAssignDistances() {
		calculateDistances(this.places);
	}

	public static TreeMap<Integer, Place> calculateDistances(final TreeMap<Integer, Place> places) {
		final Set<Map.Entry<Integer, Place>> listOfPlaces = places.entrySet();

		for (final Map.Entry<Integer, Place> startPlace: listOfPlaces) {
			try {
				for (final Map.Entry<Integer, Place> endPlace: listOfPlaces) {
					if (!startPlace.getKey().equals(endPlace.getKey())) {
						startPlace.getValue().setDistanceTo(endPlace.getValue());
					}
				}

				final Map<Integer, Integer> origDistances = startPlace.getValue().getDistances();
				startPlace.getValue().setDistances(origDistances.entrySet().stream().sorted(Map.Entry.comparingByValue())
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)));
			} catch (final IllegalArgumentException e) {
				LOGGER.log(Level.WARNING, e.toString());
			}
		}

		return places;
	}

	/**
	 * @return String Path to the source file
	 */
	public String getSourceFilename() {
		return this.sourceFilename;
	}

	/**
	 * @return List of places
	 */
	public TreeMap<Integer, Place> getPlaces() {
		return this.places;
	}

	/**
	 * @return TSP name based on source file
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return Used display data type e.g. COORD_DISPLAY
	 */
	public DisplayDataType getDisplayDataType() {
		return this.displayDataType;
	}

	/**
	 * @return Used edge weight type e.g. GEO
	 */
	public EdgeWeightType getEdgeWeightType() {
		return this.edgeWeightType;
	}

	/**
	 * @return Key of the starting place
	 */
	public Integer getStartingPlace() {
		return this.startingPlace;
	}

	public enum EdgeWeightType {
		GEO
	}

	public enum DisplayDataType {
		COORD_DISPLAY
	}
}
