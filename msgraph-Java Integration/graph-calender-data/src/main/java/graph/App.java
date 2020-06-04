package graph;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import com.microsoft.graph.models.extensions.Contact;
import com.microsoft.graph.models.extensions.ContactFolder;
import com.microsoft.graph.models.extensions.DateTimeTimeZone;
import com.microsoft.graph.models.extensions.Event;
import com.microsoft.graph.models.extensions.Group;
import com.microsoft.graph.models.extensions.Message;
import com.microsoft.graph.models.extensions.User;

/**
 * Graph Tutorial
 *
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Java Graph Tutorial");
        System.out.println();

     // Load OAuth settings
        final Properties oAuthProperties = new Properties();
        try {
            oAuthProperties.load(App.class.getResourceAsStream("/oAuth.properties"));
        } catch (IOException e) {
            System.out.println("Unable to read OAuth configuration. Make sure you have a properly formatted oAuth.properties file. See README for details.");
            return;
        }

        final String appId = oAuthProperties.getProperty("app.id");
        final String[] appScopes = oAuthProperties.getProperty("app.scopes").split(",");
        System.out.println("HIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
        // Get an access token
        Authentication.initialize(appId);
        final String accessToken = Authentication.getUserAccessToken(appScopes);
        
     // Greet the user
        User user = Graph.getUser(accessToken);
        //User user = Graph.getUser(appId,appScopes,"sreeparna.kundu@incture.com","Pikachu@1");
        System.out.println("Welcome " + user.displayName);
        System.out.println(" userPrincipalName " + user.userPrincipalName);
        System.out.println(" id " + user.id);
        System.out.println();
        
        Scanner input = new Scanner(System.in);
        
        int choice = -1;

        while (choice != 0) {
            System.out.println("Please choose one of the following options:");
            System.out.println("0. Exit");
            System.out.println("1. Display access token");
            System.out.println("2. List calendar events");
            System.out.println("3. List Message");
            System.out.println("4. Contact Folders");
            System.out.println("5. Contacts");
            System.out.println("6. Groups");
            System.out.println("7. Send Mail");
            System.out.println("8. List Your Teams");

            try {
                choice = input.nextInt();
            } catch (InputMismatchException ex) {
                // Skip over non-integer input
                input.nextLine();
            }

            // Process user choice
            switch(choice) {
                case 0:
                    // Exit the program
                    System.out.println("Goodbye...");
                    break;
                case 1:
                    // Display access token
                	System.out.println("Access token: " + accessToken);
                    break;
                case 2:
                	listCalendarEvents(accessToken);
                    // List the calendar
                    break;
                case 3:
                	listMessages(accessToken);
                    // List the Message
                case 4:	
                	listContactFolders(accessToken);
                	 break;
                case 5:
                	listContacts(accessToken);
                    // List the Message
                    break;
                case 6:
                	listGroups(accessToken);
                    // List the Message
                    break;
                case 7:
                	mailSend(accessToken);
                    // List the Message
                    break;  
                /*case 8:
                	getListYourTeams(accessToken);
                    // List Your Teams
                    break;  */
                default:
                    System.out.println("Invalid choice");
            }
        }

        input.close();
    }
    

	private static String formatDateTimeTimeZone(DateTimeTimeZone date) {
        LocalDateTime dateTime = LocalDateTime.parse(date.dateTime);

        return dateTime.format(
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)) +
            " (" + date.timeZone + ")";
    }
    private static void listCalendarEvents(String accessToken) {
        // Get the user's events
        List<Event> events = Graph.getEvents(accessToken);

        System.out.println("Events:");

        for (Event event : events) {
            System.out.println("Subject: " + event.subject);
            System.out.println("  Organizer: " + event.organizer.emailAddress.name);
            System.out.println("  Start: " + formatDateTimeTimeZone(event.start));
            System.out.println("  End: " + formatDateTimeTimeZone(event.end));
        }

        System.out.println();
    }
    
    private static void listMessages(String accessToken) {
        // Get the user's events
        List<Message> messages = Graph.getMessage(accessToken);

        System.out.println("Messages:");

        for (Message message : messages) {
            System.out.println("Subject: " + message.subject);
            System.out.println("  Sender Name: " + message.sender.emailAddress.name);
            System.out.println("  Sender Email: " + message.sender.emailAddress.address);
        }

        System.out.println();
    }
    
    private static void mailSend(String accessToken) {
        // Get the user's events
        Graph.sendMail(accessToken);

        System.out.println("Mail Send:");


        System.out.println();
    }
    
    private static void listContactFolders(String accessToken) {
        // Get the user's events
        List<ContactFolder> contactFolders = Graph.getContactFolders(accessToken);

        System.out.println("Contact Folders:" + contactFolders);

        for (ContactFolder contactFolder : contactFolders) {
            System.out.println("Parent Folder ID: " + contactFolder.parentFolderId);
            System.out.println("  Display Name: " + contactFolder.displayName);
            System.out.println("  Given Name: " + contactFolder.id);
        }

        System.out.println();
    }
    
    private static void listContacts(String accessToken) {
        // Get the user's events
        List<Contact> contacts = Graph.getContacts(accessToken);

        System.out.println("Contacts:");

        for (Contact contact : contacts) {
            System.out.println("Subject: " + contact.parentFolderId);
            System.out.println("  Birthday: " + contact.birthday);
            System.out.println("  File As: " + contact.fileAs);
            System.out.println("  Display Name: " + contact.displayName);
            System.out.println("  Given Name: " + contact.givenName);
            System.out.println("  Initials: " + contact.initials);
        }

        System.out.println();
    }
    private static void listGroups(String accessToken) {
        // Get the user's events
        List<Group> groups = Graph.getGroups(accessToken);

        System.out.println("Groups:");

        for (Group group : groups) {
            System.out.println("Id: " + group.id);
            System.out.println("  Display Name: " + group.displayName);
            System.out.println("  Description: " + group.description);
        }

        System.out.println();
    }
    
    private static void getListYourTeams(String accessToken) {
		// TODO Auto-generated method stub
    	List<Group> groups = Graph.getListYourTeams(accessToken);

        System.out.println("Groups:");

        for (Group group : groups) {
            System.out.println("Id: " + group.id);
            System.out.println("  Display Name: " + group.displayName);
            System.out.println("  Description: " + group.description);
        }

        System.out.println();
	}
}