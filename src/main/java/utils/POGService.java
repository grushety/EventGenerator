package utils;

import domain.Event;
import domain.POG;
import domain.StreamObject;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class POGService {
    public POGService() {
    }

    public Set<StreamObject> addPOGsToEventStream(Set<Event> events, int pog_distance, int max_index) {
        int lastIndex = pog_distance;
        Set<StreamObject> stream = events.stream().map(event -> (StreamObject)event ).collect(Collectors.toSet());
        System.out.println("size before pog: " + stream.size() );
        while (lastIndex <= max_index) {
            int index = lastIndex;
            Optional<Event> matchingObject = events.stream().
                    filter(p -> p.getId() == index).
                    findFirst();
            if(matchingObject.isEmpty()){
                lastIndex++;
            }
            else {
                Event event = matchingObject.get();
                POG pog = new POG(event.getType(), event.getTs(), event.getAts());
                stream.add(pog);
                lastIndex += pog_distance;
            }
        }
        System.out.println("size after pog: " + stream.size() );
        return stream;
    }

}
