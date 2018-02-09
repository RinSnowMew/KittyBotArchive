/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.net.dv8tion;

/**
 *
 * @author Wisp
 */
public class Response 
{
	// All good!
	public static final int EC_NO_ERROR = 0;

	// General codes
	public static final int EC_INVALID = -1;

	// HTTP Error codes
	public static final int EC_HTTP_404_RESOUCE_NOT_FOUND = 404;
	public static final int EC_HTTP_501_NOT_IMPLEMENTED = 501;
	public static final int EC_HTTP_503_INTERNAL_SERVER_ERROR = 503;
	
	// Private Variables
    private String content_;
    private boolean valid_;
	private int errorCode_;
	
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
	
    // Public member functions
    public boolean isValid()         { return valid_; }
    public void setContent(String s) { this.content_ = s; this.valid_ = true; }
	public void setErrorCode(int c)  { this.errorCode_ = c; }
    public String getContent()       { return this.content_; }
	public int getErrorCode()        { return this.errorCode_; }


}
