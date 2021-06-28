package utils;

import domain.Event;
import domain.POG;
import domain.StreamObject;

import java.time.Clock;
import java.time.Duration;
import java.util.*;
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
        int startIndex = 0;
        Set<StreamObject> stream = events.stream().map(event -> (StreamObject) event).collect(Collectors.toSet());
        while (lastIndex <= max_index) {
            int index = lastIndex;
            int finalStartIndex = startIndex;
            /* use the latest ats in pog distance and latest ts */
           List<Event> partStream = events.stream()
                   .filter(p -> p.getId() < index && p.getId() >= finalStartIndex)
                   .collect(Collectors.toList());
            if (partStream.size() == 0) {
                lastIndex++;
            } else {
                Event eventMaxAts = partStream.stream().max(StreamObject.getAtsComparator()).orElseThrow(NoSuchElementException::new);
                Event eventMaxTs = partStream.stream().max(StreamObject.getTsComparator()).orElseThrow(NoSuchElementException::new);
                POG pog = new POG(eventMaxAts.getType(), eventMaxTs.getTs(), eventMaxAts.getAts());
                stream.add(pog);
                startIndex = lastIndex;
                lastIndex += pog_distance;
            }
        }
        return stream;
    }
}
