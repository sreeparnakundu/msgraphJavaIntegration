package graph;

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import com.microsoft.aad.msal4j.DeviceCode;
import com.microsoft.aad.msal4j.DeviceCodeFlowParameters;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.PublicClientApplication;
import com.microsoft.aad.msal4j.UserNamePasswordParameters;

/**
 * Authentication
 */
public class Authentication {

    private static String applicationId;
    // Set authority to allow only organizational accounts
    // Device code flow only supports organizational accounts
    private final static String authority = "https://login.microsoftonline.com/common/";
    //private final static String authority = "https://login.microsoftonline.com/78d7cfc1-dedd-4464-b309-74a59265897e"; //tenant id needs to add for username password auth

    public static void initialize(String applicationId) {
        Authentication.applicationId = applicationId;
    }

    public static String getUserAccessToken(String[] scopes) {
        if (applicationId == null) {
            System.out.println("You must initialize Authentication before calling getUserAccessToken");
            return null;
        }
        
        Set<String> scopeSet = new HashSet<>();
        for(String s: scopes){
        	scopeSet.add(s);
        }
        System.out.println("))))))))))))))))))))");
        ExecutorService pool = Executors.newFixedThreadPool(1);
        
        
        
        
        
        
        
        
        
        PublicClientApplication app;
        try {
            // Build the MSAL application object with
            // app ID and authority
            app = PublicClientApplication.builder(applicationId)
                .authority(authority)
                .executorService(pool)
                .build();
        } catch (MalformedURLException e) {
            return null;
        }
        // Create consumer to receive the DeviceCode object
        // This method gets executed during the flow and provides
        // the URL the user logs into and the device code to enter
        Consumer<DeviceCode> deviceCodeConsumer = (DeviceCode deviceCode) -> {
            // Print the login information to the console
            System.out.println(deviceCode.message());
        };

        System.out.println("--------------------------------------");
        // Request a token, passing the requested permission scopes
        IAuthenticationResult result = app.acquireToken(
            DeviceCodeFlowParameters
                .builder(scopeSet, deviceCodeConsumer)
                .build()
        ).exceptionally(ex -> {
            System.out.println("Unable to authenticate - " + ex.getMessage());
            return null;
        }).join();

        
        
        
       
        
        
       /* 
        
        //mycode
        PublicClientApplication app = null;
		try {
			app = PublicClientApplication.builder(applicationId).
			        authority(authority).
			        build();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        String password="Pikachu@1";
        UserNamePasswordParameters paramaters = 
                UserNamePasswordParameters.builder(
                		scopeSet,
                    "Sreeparna.Kundu@incture.com",
                    password.toCharArray()).build();

        IAuthenticationResult result = null;
		try {
			result = app.acquireToken(paramaters).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to authenticate 1- " + e.getMessage());
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to authenticate 2- " + e.getMessage());
			e.printStackTrace();
		}
        //mycode
         
         */
        
        
        
        System.out.println("++++++++++++++++++++++++");
        
        
        pool.shutdown();

        if (result != null) {
            return result.accessToken();
        }

        return null;
    }
}