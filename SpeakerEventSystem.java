import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

//SpeakerEventSystem links speakers to events in the system
//it reads and writes the speaker-event relationship in users.txt

public class SpeakerEventSystem {
    private static final String SPEAKER_EVENT_FILE = "speaker_event.txt";

    // save a mapping row: speaker UID | event ID.
    public static boolean linkSpeakerToEvent(String speakerUid, String eventId) {
        try {
        if (isBlank(speakerUid) || isBlank(eventId)) {
            return false;
        }
        String row = speakerUid.trim() + " | " + eventId.trim();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SPEAKER_EVENT_FILE, true))) {
            
            writer.write(row);
            writer.newLine();
            return true;
        
            } catch (IOException e) {
            return false;
        }
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static boolean isBlank(String value) {
        try {
        return value == null || value.trim().isEmpty();
    
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
    }


    static {
        try {

        } catch (Exception ex) {
            System.out.println("Unexpected error: " + ex.getMessage());
        }
    }
}
