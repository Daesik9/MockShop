package project.mockshop.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

public class OrderNumberGenerator {
    public static String generateOrdeNumber() {
        long epochSeconds = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int randomNumber = new Random().nextInt(1000000);
        return epochSeconds + String.format("%06d", randomNumber);
    }
}
