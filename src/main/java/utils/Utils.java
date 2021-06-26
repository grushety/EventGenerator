package utils;

import domain.Event;
import domain.POG;
import domain.StreamObject;

import java.time.Clock;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Utils {
    public Utils() {
    }

    public Clock getNextTime(Clock start, int min_delay, int max_delay) {
        Random t = new Random();
        int timeDiff = t.nextInt(max_delay - min_delay + 1) + min_delay;
        Duration duration = Duration.ofSeconds(timeDiff);
        return Clock.offset(start, duration);
    }

    public Set<StreamObject> addPOGsToEventStream(Set<Event> events, int pog_distance, int max_index) {
        int lastIndex = pog_distance;
        Set<StreamObject> stream = events.stream().map(event -> (StreamObject) event).collect(Collectors.toSet());
        while (lastIndex <= max_index) {
            int index = lastIndex;
            Optional<Event> matchingObject = events.stream().
                    filter(p -> p.getId() == index).
                    findFirst();
            if (matchingObject.isEmpty()) {
                lastIndex++;
            } else {
                Event event = matchingObject.get();
                POG pog = new POG(event.getType(), event.getTs(), event.getAts());
                stream.add(pog);
                lastIndex += pog_distance;
            }
        }
        return stream;
    }
}
