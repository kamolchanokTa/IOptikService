package optik.models.response;

public class Response {
	public String status;
	
	public String message;
	
	public Object data;
	
	public Response(String status,String  message, Object data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}
}
