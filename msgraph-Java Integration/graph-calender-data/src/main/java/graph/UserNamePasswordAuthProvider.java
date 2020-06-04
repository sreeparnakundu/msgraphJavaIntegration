package graph;

import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.http.IHttpRequest;

public class UserNamePasswordAuthProvider implements IAuthenticationProvider{

	private String accessToken = null;
	private String clientId = null;
	private String[] scopes = null;
	private String username = null;
	private String password = null;

    public UserNamePasswordAuthProvider(String accessToken) {
        this.accessToken = accessToken;
    }
	
	public UserNamePasswordAuthProvider(String clientId, String[] scopes, String username, String password) {
		super();
		this.clientId = clientId;
		this.scopes = scopes;
		this.username = username;
		this.password = password;
	}

	@Override
	public void authenticateRequest(IHttpRequest request) {
		// TODO Auto-generated method stub
		request.addHeader("Authorization", "Bearer " + accessToken);
	}

}
