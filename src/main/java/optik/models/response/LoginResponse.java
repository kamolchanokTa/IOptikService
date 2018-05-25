package optik.models.response;

import optik.models.user.User;

public class LoginResponse {
	public User user;
	
	public String sessionKey;
	
	public LoginResponse(User user, String sessionKey) {
		this.user = user;
		this.sessionKey = sessionKey;
	}
}
