package graph;

import java.util.LinkedList;
import java.util.List;

import com.microsoft.graph.logger.DefaultLogger;
import com.microsoft.graph.logger.LoggerLevel;
import com.microsoft.graph.models.extensions.Contact;
import com.microsoft.graph.models.extensions.ContactFolder;
import com.microsoft.graph.models.extensions.EmailAddress;
import com.microsoft.graph.models.extensions.Event;
import com.microsoft.graph.models.extensions.Group;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.ItemBody;
import com.microsoft.graph.models.extensions.Message;
import com.microsoft.graph.models.extensions.Recipient;
import com.microsoft.graph.models.extensions.Team;
import com.microsoft.graph.models.extensions.User;
import com.microsoft.graph.models.generated.BodyType;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.graph.requests.extensions.IContactCollectionPage;
import com.microsoft.graph.requests.extensions.IContactFolderCollectionPage;
import com.microsoft.graph.requests.extensions.IEventCollectionPage;
import com.microsoft.graph.requests.extensions.IGroupCollectionPage;
import com.microsoft.graph.requests.extensions.IMessageCollectionPage;
import com.microsoft.graph.requests.extensions.ITeamCollectionPage;

/**
 * Graph
 */
public class Graph {

	private static IGraphServiceClient graphClient = null;
	private static SimpleAuthProvider authProvider = null;
	//private static UserNamePasswordAuthProvider authProvider = null;

	private static void ensureGraphClient(String accessToken) {
		if (graphClient == null) {
			// Create the auth provider
			authProvider = new SimpleAuthProvider(accessToken);

			// Create default logger to only log errors
			DefaultLogger logger = new DefaultLogger();
			logger.setLoggingLevel(LoggerLevel.ERROR);

			// Build a Graph client
			graphClient = GraphServiceClient.builder().authenticationProvider(authProvider).logger(logger)
					.buildClient();
		}
	}
	
	/*private static void ensureGraphClient(String clientId,String[] scopes,String username,String password) {
		if (graphClient == null) {
			// Create the auth provider
			//authProvider = new SimpleAuthProvider(accessToken);
			
			//my code
			authProvider = new UserNamePasswordAuthProvider(clientId,scopes,username,password);

			// Create default logger to only log errors
			DefaultLogger logger = new DefaultLogger();
			logger.setLoggingLevel(LoggerLevel.ERROR);

			// Build a Graph client
			graphClient = GraphServiceClient.builder().authenticationProvider(authProvider).logger(logger)
					.buildClient();
		}
	}*/

	public static User getUser(String accessToken) {
		ensureGraphClient(accessToken);

		// GET /me to get authenticated user
		User me = graphClient.me().buildRequest().get();

		return me;
	}

	/*public static User getUser(String clientId, String[] scopes, String username, String password) {
		ensureGraphClient(clientId,scopes,username,password);

		// GET /me to get authenticated user
		User me = graphClient.me().buildRequest().get();

		return me;
	}*/
	
	public static List<Event> getEvents(String accessToken) {
		ensureGraphClient(accessToken);

		// Use QueryOption to specify the $orderby query parameter
		final List<Option> options = new LinkedList<Option>();
		// Sort results by createdDateTime, get newest first
		options.add(new QueryOption("orderby", "createdDateTime DESC"));

		// GET /me/events
		IEventCollectionPage eventPage = graphClient.me().events().buildRequest(options)
				.select("subject,organizer,start,end").get();

		return eventPage.getCurrentPage();
	}

	public static List<Message> getMessage(String accessToken) {
		// ensureGraphClient(accessToken);
		IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider(authProvider)
				.buildClient();

		IMessageCollectionPage messages = graphClient.me().messages().buildRequest().select("sender,subject").get();

		return messages.getCurrentPage();
	}
	
	public static List<ContactFolder> getContactFolders(String accessToken) {
		IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider( authProvider ).buildClient();

		IContactFolderCollectionPage contactFolders = graphClient.me().contactFolders()
			.buildRequest()
			.get();
		return contactFolders.getCurrentPage();
	}

	public static List<Contact> getContacts(String accessToken) {
		IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider(authProvider)
				.buildClient();

		IContactCollectionPage contacts = graphClient.me().contacts().buildRequest().get();
		return contacts.getCurrentPage();
	}

	public static List<Group> getGroups(String accessToken) {
		// ensureGraphClient(accessToken);
		IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider(authProvider)
				.buildClient();

		IGroupCollectionPage groups = graphClient.groups().buildRequest().get();

		return groups.getCurrentPage();
	}

	public static void sendMail(String accessToken) {
		// TODO Auto-generated method stub
		IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider( authProvider ).buildClient();

		Message message = new Message();
		message.subject = "Meet for lunch?";
		ItemBody body = new ItemBody();
		body.contentType = BodyType.TEXT;
		body.content = "The new cafeteria is open.";
		message.body = body;
		LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();
		Recipient toRecipients = new Recipient();
		EmailAddress emailAddress = new EmailAddress();
		emailAddress.address = "sreeparna.kundu@incture.com";
		toRecipients.emailAddress = emailAddress;
		toRecipientsList.add(toRecipients);
		message.toRecipients = toRecipientsList;
		LinkedList<Recipient> ccRecipientsList = new LinkedList<Recipient>();
		Recipient ccRecipients = new Recipient();
		EmailAddress emailAddress1 = new EmailAddress();
		emailAddress1.address = "sreeparna.kundu@incture.com";
		ccRecipients.emailAddress = emailAddress1;
		ccRecipientsList.add(ccRecipients);
		message.ccRecipients = ccRecipientsList;

		boolean saveToSentItems = false;

		graphClient.me()
			.sendMail(message,saveToSentItems)
			.buildRequest()
			.post();
	}
	
	public static List<Group> getListYourTeams(String accessToken) {
		IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider( authProvider ).buildClient();
	
		IGroupCollectionPage joinedTeams = graphClient.me().joinedTeams()
			.buildRequest()
			.get();
		return joinedTeams.getCurrentPage();
	}
	
	
	/*public static List<Group> readChats(String accessToken) {
		// ensureGraphClient(accessToken);
		IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider(authProvider)
				.buildClient();

		IGroupCollectionPage groups = graphClient.users("").groups().buildRequest().get();

		return groups.getCurrentPage();
		
		
		//c#--> graph-beta
		GraphServiceClient graphClient = new GraphServiceClient( authProvider );

		var chats = await graphClient.Users["{id}"].Chats
			.Request()
			.GetAsync();
	}*/
}