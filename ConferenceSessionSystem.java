import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//ConferenceSessionSystem manages conference sessions
//it links speakers to time slots and checks for booking conflicts
//it extends BaseSystemService to inherit shared helper methods

//conference session system to manage session scheduling
public class ConferenceSessionSystem extends BaseSystemService {
    private static final String SESSION_FILE = "sessions.txt"; // get the sessions.txt
    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    //create one session row and save if no time conflict
    public static boolean addSession(String eventId,String speakerUid,String sessionTitle,String sessionTopic,String room,String startDateTime,String endDateTime)  {
        try {
            //if fields are empty and return false  
        if (isBlank(eventId) || isBlank(speakerUid) || isBlank(sessionTitle) || isBlank(sessionTopic) || isBlank(room) || isBlank(startDateTime) || isBlank(endDateTime)) {
            return false;
        }

        //convert string to date time
        LocalDateTime start;
        LocalDateTime end;
        try {
            start = LocalDateTime.parse(startDateTime.trim(), DATE_TIME_FMT);
            end = LocalDateTime.parse(endDateTime.trim(), DATE_TIME_FMT);
        } catch (DateTimeParseException ex) {
            return false;
        }

        //end time must be after start time
        if (!end.isAfter(start)) {
            return false;
        }

        //check :speaker or Room overlop
        if (hasScheduleConflict(eventId, speakerUid, room, start, end)) {
            return false;
        }

        //generate Id and save the session
        String sessionId = UUID.randomUUID().toString();
        String line = sessionId + "|" + eventId.trim() + "|" + speakerUid.trim() + "|" + sessionTitle.trim() + "|" + sessionTopic.trim() + "|" + room.trim() + "|" + start.format(DATE_TIME_FMT) + "|" + end.format(DATE_TIME_FMT);

        //save session into file if everything is valid
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SESSION_FILE, true))) {
            
            writer.write(line);
            writer.newLine();
            return true;
        
            } catch (IOException e) {
            return false;
        }
    
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }//load sessions by eventID
            }

    //load all sessions under one event
    public static List<String[]> loadSessionsByEvent(String eventId) {
        try {
        List<String[]> rows = new ArrayList<>();
        if (isBlank(eventId)) {
            return rows;
        }

        File file = new File(SESSION_FILE);
        if (!file.exists()) {
            return rows;
        }

        //read session file and get all sessions for a specific event
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length < 8) {
                    continue;
                }
                if (!eventId.trim().equals(parts[1].trim())) {  //only get sessions for this event
                    continue;
                }
                rows.add(parts);
            }
        
            } catch (IOException ignored) {
            return rows;
        }
        return rows;
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }//geerate session summary
    }

    //create session summary text for an event
    public static String buildSessionSummary(String eventId) {
        try {
        List<String[]> sessions = loadSessionsByEvent(eventId); //load all sessions for this event
        if (sessions.isEmpty()) {
            return "No sessions scheduled.";
        }

        StringBuilder buildLine = new StringBuilder();
        for (String[] session : sessions) {
            buildLine.append("- ")
                    .append("Title: ").append(safe(session, 3)).append(" | ")
                    .append("Topic: ").append(safe(session, 4)).append(" | ")
                    .append("Speaker UID: ").append(safe(session, 2)).append(" | ")
                    .append("Room: ").append(safe(session, 5)).append(" | ")
                    .append("Start: ").append(safe(session, 6)).append(" | ")
                    .append("End: ").append(safe(session, 7))
                    .append("\n");
        }
        return buildLine.toString().trim();
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }//logic for time and resource
    }

    //reject overlapping speaker/room slot in same event
    private static boolean hasScheduleConflict(String eventId,String speakerUid,String room,LocalDateTime start,LocalDateTime end){
        try {
        //List<String[] sessions> is stire all raw data rows from the text file
        List<String[]> sessions = loadSessionsByEvent(eventId);
        for (String[] Sessions : sessions) {
            LocalDateTime existingStart;
            LocalDateTime existingEnd;
            try {
                //parse is based on the format defined in DATE_TIME_FMT,if passing is false, it will catch out the problem
                existingStart = LocalDateTime.parse(safe(Sessions, 6), DATE_TIME_FMT);
                existingEnd = LocalDateTime.parse(safe(Sessions, 7), DATE_TIME_FMT);
            } catch (DateTimeParseException ex) {
                continue;
            }

            boolean overlap = start.isBefore(existingEnd) && end.isAfter(existingStart);
            boolean sameSpeaker = speakerUid.trim().equals(safe(Sessions, 2));
            boolean sameRoom = room.trim().equalsIgnoreCase(safe(Sessions, 5));
            if (overlap && (sameSpeaker || sameRoom)) {
                return true;
            }
        }
        return false;
    
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }//array bounds safety helper
            }

    //safe array access help to avoid data format issues and null pointer exception
    private static String safe(String[] arr, int idx) {
        try {
        if (arr == null || idx < 0 || idx >= arr.length || arr[idx] == null) {
            return "";
        }
        return arr[idx].trim();
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    static {
        try {
        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
