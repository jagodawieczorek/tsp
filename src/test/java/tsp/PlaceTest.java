package tsp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for Place
 *
 * @author Jagoda Wieczorek
 */
class PlaceTest {

	@Test
	@DisplayName("Should set geographical distance from one place to another")
	void shouldSetDistanceTo() {
		// given
		final Place startPlace = new Place(1, 71.17f, -156.47f);
		final Place endPlace = new Place(2, 64.51f, -147.43f);
		// when
		startPlace.setDistanceTo(endPlace);
		// then
		assertThat(startPlace.getDistances().get(endPlace.getId())).isEqualTo(876);
	}
}
