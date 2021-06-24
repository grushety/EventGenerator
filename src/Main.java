import domain.StreamObject;

import java.time.Clock;
import java.time.Duration;
import java.util.List;

public class Main {

    //Generation Parameters
    int number_of_events = 100;  //for each Event Type
    int out_of_order_quota = 30;
    int negative_quota = 20;
    int pog_quota = 20;
    int number_of_events_type = 10;

    public static void main(String[] args) {

        Clock c = Clock.systemUTC();
        System.out.println(c.instant());
        Duration d = Duration.ofHours(5);
        Clock clock = Clock.offset(c, d);
        System.out.println(clock.instant());

    }
}
