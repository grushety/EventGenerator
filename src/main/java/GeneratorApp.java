import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.Event;
import domain.EventType;
import domain.StreamObject;
import utils.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Clock;
import java.util.*;

public class GeneratorApp {


    public static void main(String[] args) throws IOException {
        Utils util = new Utils();

        //Generation Parameters
        int number_of_events = 100; //for each Event Type
        int out_of_order_quota = 30; // in percent
        int negative_quota_from_out_of_order = 20; // in percent
        int negative_quota = (int) ((double) out_of_order_quota / 100 * negative_quota_from_out_of_order);
        int pog_quota = 20;
        int pog_distance = number_of_events / pog_quota;
        int min_time_in_order = 1;
        int max_time_in_order = 10;
        int max_time_out_of_order = 50;
        int min_generation_delay = 5;
        int max_generation_delay = 30;
        Clock startClock = Clock.systemUTC();

        // generate event stream
        Set<StreamObject> stream = new HashSet<>();
        for (EventType eventType : EventType.values()) {
            Set<Event> subStream = new HashSet<>();
            Clock generationTime = util.getNextTime(startClock, min_generation_delay, max_generation_delay);
            int out_of_order_counter = 0;
            int normal_counter = 0;
            for (int i = 0; i < number_of_events; i++) {
                Random r = new Random();
                int eventKind = r.nextInt(100);
                if (eventKind > negative_quota) {
                    Clock arrivalTime = generationTime;
                    if (eventKind > out_of_order_quota) {
                        // create ats for in order event
                        normal_counter++;
                        arrivalTime = util.getNextTime(generationTime, min_time_in_order, max_time_in_order);
                    } else {
                        // create ats out-of-order event
                        out_of_order_counter++;
                        arrivalTime = util.getNextTime(generationTime, max_time_in_order + 1, max_time_out_of_order);
                    }
                    Event event = new Event(i, eventType, generationTime.instant(), arrivalTime.instant());
                    subStream.add(event);
                    generationTime = util.getNextTime(generationTime, min_generation_delay, max_generation_delay);
                }
                // the rest (negative events) will be not produced
            }
            // generate POGs for each event type
            // System.out.println("normal: " + normal_counter + " out-of-order: " + out_of_order_counter + " negative: " + (100-normal_counter-out_of_order_counter));
            Set<StreamObject> streamWithPOGs = util.addPOGsToEventStream(subStream, pog_distance, number_of_events);
            stream.addAll(streamWithPOGs);
            startClock = util.getNextTime(startClock, 10, 10);
        }

        // sort event stream after ats
        List<StreamObject> streamObjectList = new ArrayList<>(stream);
        streamObjectList.sort(StreamObject.getAtsComparator());

        // convert event stream to json and save in file
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(streamObjectList); // converts to json
        String name = "stream_with_POGs_" + pog_quota + "_out_of_order_" + out_of_order_quota + "_negative_" + negative_quota_from_out_of_order + "_number_of_events_" + number_of_events * 10;
        FileWriter file = new FileWriter("generated/" + name + ".json");
        try {
            // Constructs a FileWriter given a file name, using the platform's default charset
            file.write(json);
            System.out.println("Successfully Copied JSON Object to File...");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }
    }

}
