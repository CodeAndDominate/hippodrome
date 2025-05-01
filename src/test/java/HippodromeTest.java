import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HippodromeTest {

    @Test
    void shouldThrowIllegalArgumentException_whenPassNullListToHippodromeConstructor() {
        var ex = assertThrows(IllegalArgumentException.class, () -> new Hippodrome(null));
        assertEquals("Horses cannot be null.", ex.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentException_whenPassEmptyListToHippodromeConstructor() {
        var ex = assertThrows(IllegalArgumentException.class, () -> new Hippodrome(List.of()));
        assertEquals("Horses cannot be empty.", ex.getMessage());
    }

    @Test
    void shouldGetCorrectHorseList_whenPassHorseListViaConstructor() {
        //given
        List<Horse> horses = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            horses.add(new Horse("Horse #" + i, i));
        }
        //when
        Hippodrome hippodrome = new Hippodrome(horses);
        //then
        assertThat(horses, Matchers.is(hippodrome.getHorses()));
    }

    @DisplayName("should call move() exactly once on each horse when hippodrome.move() is called")
    @Test
    void shouldCheckThatMoveMethodIsCalledForAllHorses_whenSuccess() {
        //given
        List<Horse> horses = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            Horse horse = Mockito.mock(Horse.class);
            horses.add(horse);
        }
        Hippodrome hippodrome = new Hippodrome(horses);
        //when
        hippodrome.move();
        //then
        horses.forEach(horse -> Mockito.verify(horse, Mockito.times(1)).move());
    }

    @Test
    void shouldReturnHorseWithMaxDistance_whenGetWinnerMethodIsCalled() {
        //given
        List<Horse> horses = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            horses.add(new Horse("Horse #" + i, i, i * 10));
        }
        Hippodrome hippodrome = new Hippodrome(horses);
        Horse expected = Collections.max(horses, Comparator.comparing(Horse::getDistance));
        //then
        assertSame(expected, hippodrome.getWinner());
    }
}