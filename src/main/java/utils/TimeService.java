package utils;

import java.time.Clock;
import java.time.Duration;
import java.util.Random;

public class TimeService {
    public TimeService() {}

    public Clock getNextTime(Clock start, int min_delay, int max_delay){
        Random t = new Random();
        int timeDiff = t.nextInt(max_delay - min_delay + 1) + min_delay;
        Duration duration = Duration.ofSeconds(timeDiff);
        return Clock.offset(start, duration);
    }
}
