/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.net.dv8tion;

/**
 * @author Wisp
 */
// EC stands for error code. This prefix is used for autocomplete grouping.
public class Response 
{
	  ////////////////////////////////////////
	 // Variables, both public and private //
	////////////////////////////////////////
	// All good!
	public static final int EC_NO_ERROR = 0;

	// General codes internal to KittyBot
	public static final int EC_INVALID = -1;

	// Generic HTTP Error codes
	public static final int EC_HTTP_404_RESOUCE_NOT_FOUND = 404;
	public static final int EC_HTTP_501_NOT_IMPLEMENTED = 501;
	public static final int EC_HTTP_503_INTERNAL_SERVER_ERROR = 503;
	
	// E621 Responses
	public static final int EC_E621_SUCCESS = 200;				// It worked!
	public static final int EC_E621_ACCESS_DENIED = 403;		// Access denied, may need login.
	public static final int EC_E621_NOT_FOUND = 404;			// Resource not found at URL.
	public static final int EC_E621_INVALID_RECORD = 420;		// Record couldn't be saved.
	public static final int EC_E621_USER_THROTTLED = 421;		// Too many Requests. Max at 2/sec. (Queue requests?)
	public static final int EC_E621_LOCKED = 422;				// You can't modify the thing you're trying to modify.
	public static final int EC_E621_ALREDY_EXISTS = 423;		// Resource you're working with already exists.
	public static final int EC_E621_INVALID_PARAMETERS = 424;	// You provided some parameters that didn't make sense. 
	public static final int EC_E621_INTERNAL_ERROR = 500;		// Not sure what the error was, but it was on the server's side.
	public static final int EC_E621_BAD_GATEWAY = 502;			// E621 had a gateway server that didn't know what was going on.
	public static final int EC_E621_SERVICE_UNAVAILABLE = 504;  // Either rate limit exceeded, or server couldn't handle the request.
	public static final int EC_E621_UNKNOWN_ERROR = 520;        // Something kinda strange happened, and it didn't follow protocol.
	public static final int EC_E621_ORIGIN_TIMEOUT_NC = 522;    // Cloudflare could not connect (NC) to e621 servers at all.
	public static final int EC_E621_ORIGIN_TIMEOUT_T = 524;     // Cloudflare connected to e621 servers but e621 HTTP response timed out.
	public static final int EC_E621_SSL_HANDSHAKE_FAILED = 525; // SSL handshake between cloudflare and e621 didn't work.
	
	// Private Variables
    private String content_;
    private boolean valid_;
	private int errorCode_;
	
	
	
      ////////////////////////////////////
	 // Public-facing member functions //
	////////////////////////////////////
    // Default constructor, which by default creates an invalid response.
    public Response() 
    {
        this.content_ = ""; 
        this.errorCode_ = EC_INVALID;
		this.valid_ = false;
    } 

	// Response code constructor: By default, not valid, but does contian
	// some information and an error code as set by the user.
    public Response(int error_code) 
    {
        this.content_ = ""; 
        this.errorCode_ = error_code;
		this.valid_ = false;
    } 
	
    // General Constructor and the closest we can get to a conversion 
    // constructor in Java. Responses generated with this are valid, and CAN
    // be empty string.
    public Response(String message)
    {
        this.content_ = message;
        this.valid_ = true;
		this.errorCode_ = EC_NO_ERROR;
    }
	
    // General getters and setters
	// Get...
	public boolean isValid()         { return valid_; }
    public String getContent()       { return this.content_; }
	public int getErrorCode()        { return this.errorCode_; }
	
	// Set...
	public void setContent(String s) { this.content_ = s; this.valid_ = true; }
	public void setErrorCode(int c)  { this.errorCode_ = c; }
}
