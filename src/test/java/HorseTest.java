import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class HorseTest {

    private MockedStatic<Horse> horseMockedStatic;

    @BeforeEach
    void setUp() {
        horseMockedStatic = Mockito.mockStatic(Horse.class);
    }

    @AfterEach
    void tearDown() {
        horseMockedStatic.close();
    }

    @Test
    void shouldThrowIllegalArgumentException_whenPassNullNameToHorseConstructor() {
        var ex = assertThrows(IllegalArgumentException.class, () -> new Horse(null, 0));
        assertEquals("Name cannot be null.", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "\n"})
    void shouldThrowIllegalArgumentException_whenPassEmptyNameToHorseConstructor(String name) {
        var ex = assertThrows(IllegalArgumentException.class, () -> new Horse(name, 0));
        assertEquals("Name cannot be blank.", ex.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentException_whenPassNegativeSpeedToHorseConstructor() {
        var ex = assertThrows(IllegalArgumentException.class, () -> new Horse("Vasya", -1));
        assertEquals("Speed cannot be negative.", ex.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentException_whenPassNegativeDistanceToHorseConstructor() {
        var ex = assertThrows(IllegalArgumentException.class, () -> new Horse("Vasya", 0, -1));
        assertEquals("Distance cannot be negative.", ex.getMessage());
    }

    @Test
    void shouldGetCorrectName_whenPassNameViaConstructor() {
        var horse = new Horse("Vasya", 5);
        assertEquals("Vasya", horse.getName());
    }

    @Test
    void shouldGetCorrectSpeed_whenPassSpeedViaConstructor() {
        var horse = new Horse("Vasya", 25);
        assertEquals(25, horse.getSpeed());
    }

    @Test
    void shouldGetCorrectDistance_whenPassDistanceViaConstructor() {
        var horse = new Horse("Vasya", 25, 30);
        assertEquals(30, horse.getDistance());
    }

    @Test
    void shouldGetZeroDistance_whenNotPassDistanceViaConstructor() {
        var horse = new Horse("Vasya", 25);
        assertEquals(0, horse.getDistance());
    }

    @Test
    void shouldCallGetRandomDoubleOnce_whenHorseMoveIsCalled() {
        var horse = new Horse("Vasya", 25, 30);
        horse.move();
        horseMockedStatic.verify(() -> Horse.getRandomDouble(0.2, 0.9), times(1));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.3, 0.4, 0.5})
    void shouldCalculateCorrectDistance_whenHorseMoveIsCalled(double randomDoubleResult) {
        //given
        var horse = new Horse("Vasya", 25, 10);
        var expected = horse.getDistance() + horse.getSpeed() * randomDoubleResult;
        horseMockedStatic.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(randomDoubleResult);
        //when
        horse.move();
        //then
        assertEquals(expected, horse.getDistance());
    }
}