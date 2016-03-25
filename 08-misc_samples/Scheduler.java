    import java.util.regex.Matcher;
    import java.util.regex.Pattern;
    import java.util.*;
    import java.util.concurrent.*;
    import java.text.*;
    import java.io.*;

    /**
    * Match daily tasks
    * DateFormat: http://www.tutorialspoint.com/java/java_date_time.htm
    * To write regex without extra \ http://regexpal.com/ , http://regex101.com/
    * (\d{1,2}-\d{1,2}-\d{4})\: (\d{2}\:\d{2}) ([A|P]M) to (\d{2}\:\d{2}) ([A|P]M) -- ([\w ]+)
    * Matches: 11-6-2014: 05:18 AM to 06:00 AM -- code review
    * Challenge: https://www.reddit.com/r/dailyprogrammer/comments/2ledaj/11052014_challenge_187_intermediate_finding_time/
    */

    public class Scheduler {
        private static Long totalDuration = new Long(0L);

        public static void main(String[] args) {
            String pattern = "(\\d{1,2}-\\d{1,2}-\\d{4})\\: (\\d{2}\\:\\d{2}) ([A|P]M) to (\\d{2}\\:\\d{2}) ([A|P]M) -- ([\\w ]+)";
                List<String> lines = new ArrayList<String>();
            List<Event> eventList = new ArrayList<Event>();
            TreeMap<String, List<Event>> eventTable = new TreeMap<String, List<Event>>();
            HashMap<String, Long> activityDuration = new HashMap<String, Long>();

            // Read file into lines
            try {
                BufferedReader br = new BufferedReader(new FileReader("input.txt"));
                        String line = br.readLine();

                while (line != null) {
                    lines.add(line);
                    line = br.readLine();
                }
            } catch (Exception ex) {
                System.out.println("File read exception");
            }

            // Create pattern
            Pattern r = Pattern.compile(pattern);
            SimpleDateFormat ft = new
                    SimpleDateFormat("M-d-yyyy hh:mm a");

            // Create array of Event object
            for (String line : lines) {
                // Create matcher
                Matcher m = r.matcher(line);
                if (m.find()) {

                    try {
                        Event evt = new Event();

                        String group = m.group(1) + " " + m.group(2) + " " + m.group(3);
                        evt.setStart(ft.parse(group));

                        group = m.group(1) + " " + m.group(4) + " " + m.group(5);
                        evt.setEnd(ft.parse(group));

                        evt.setActivity(m.group(6));

                        eventList.add(evt);

                    } catch (Exception ex) {
                        System.out.println("Date format exception !! ");
                    }
                } else {
                    System.out.println("No Match");
                }
            }

            // Sort the list
            Collections.sort(eventList);


            List<Event> tmpEventList = new ArrayList<Event>();
            // Add each day to the treemap
            for(Event event: eventList) {
                String dateKey = new SimpleDateFormat("MM-dd-yyyy").format(event.getStart());
                if (!eventTable.containsKey(dateKey)) {
                    tmpEventList = new ArrayList<Event>();
                    eventTable.put(dateKey, tmpEventList);
                }

                tmpEventList.add(event);
            }



            addReddit(eventTable);
            calculateDuration(eventTable, activityDuration);

            // Now print the values
            printSchedule(eventTable);
            printDurationForTasks(activityDuration);

        }

        private static void calculateDuration(TreeMap<String, List<Event>> eventTable,
                                            HashMap<String, Long> activityDuration) {
            Set keys = eventTable.keySet();

            for(Iterator i = keys.iterator(); i.hasNext();) {
                String key = (String) i.next();
                List<Event> eventList = (List<Event>)eventTable.get(key);

                for (int j = 0; j < eventList.size(); j++) {

                    Event evt = (Event) eventList.get(j);

                    if(!activityDuration.containsKey(evt.getActivity())) {
                        activityDuration.put(evt.getActivity(), 0L);
                    }

                    long duration = TimeUnit.MILLISECONDS.toMinutes(evt.getEnd().getTime() - evt.getStart().getTime());
                    totalDuration += duration; // update global value
                    duration += activityDuration.get(evt.getActivity());
                    activityDuration.put(evt.getActivity(), duration);
                }
            }
        }


        private static void addReddit(TreeMap<String, List<Event>> eventTable)
        {

            Set keys = eventTable.keySet();

            long tmpTotalDuration = 0L;

            for(Iterator i = keys.iterator(); i.hasNext();) {
                String key = (String) i.next();

                int index = 0;
                long longestMinutes = 0;
                List<Event> eventList = (List<Event>)eventTable.get(key);

                for (int j = 1; j < eventList.size(); j++) {
                    Date start = eventList.get(j).getStart();
                    Date prevEnd = eventList.get(j-1).getEnd();

                    long duration = TimeUnit.MILLISECONDS.toMinutes(start.getTime() - prevEnd.getTime());

                    tmpTotalDuration += duration;
                    if (duration > longestMinutes) {
                        longestMinutes = duration;
                        index = j;
                    }
                }

                // Create reddit Event
                Event redditEvent = new Event();
                redditEvent.setStart(eventList.get(index-1).getEnd());
                redditEvent.setEnd(eventList.get(index).getStart());
                redditEvent.setActivity("redditing");
                eventList.add(index, redditEvent);

            }
        }


        private static void printSchedule(TreeMap<String, List<Event>> eventTable) {

            Set keys = eventTable.keySet();
            System.out.println("Your Schedule");
            System.out.println("==============");

            for(Iterator i = keys.iterator(); i.hasNext();) {
                String key = (String) i.next();

                System.out.println("\nSchedule for " + key);

                for (Event event: (List<Event>)eventTable.get(key)) {
                    String printValue = "From ";
                    String dateValue = new SimpleDateFormat("hh:mm a").format(event.getStart());
                    printValue += dateValue + " to ";
                    dateValue = new SimpleDateFormat("hh:mm a").format(event.getEnd());
                    printValue += dateValue + " -- ";
                    printValue += event.getActivity();
                    System.out.println(printValue);
                }
            }
        }

        private static void printDurationForTasks(HashMap<String, Long> activityDuration) {
            System.out.println("\nTotal time spent - " + totalDuration + " minutes");

            Iterator<String>  keyIterator = activityDuration.keySet().iterator();
            while(keyIterator.hasNext()){
            String key = keyIterator.next();
            Long value = activityDuration.get(key);
            double percentage = (double)value/totalDuration * 100;
            String percentageFormat = new DecimalFormat("#.#").format(percentage);
            System.out.println(key + " - " + value + " minutes - " + percentageFormat + "%");
            }
        }

        private static class Event implements Comparable<Event> {
            private Date start;
            private Date end;
            private String activity;

            public void setStart(Date start) {
                this.start = start;
            }

            public Date getStart() {
                return this.start;
            }

            public void setEnd(Date end) {
                this.end = end;
            }

            public Date getEnd() {
                return this.end;
            }

            public String getActivity() {
                return this.activity;
            }

            public void setActivity(String activity) {
                this.activity = activity;
            }

            public int compareTo(Event event) {
                Date compareDate = ((Event) event).getStart();
                return this.start.compareTo(compareDate);
            }

            public String toString() {
                return "Start: " + start + ", End: " + end + ", Activity: " + activity;
            }
        }
    }
