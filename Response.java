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
    // Default constructor, which by default creates an invalid response.
    public Response() 
    {
        this.content_ = ""; 
        this.valid_ = false;
    } 

    // General Constructor and the closest we can get to a conversion 
    // constructor in Java. Responses generated with this are valid, and CAN
    // be empty string.
    public Response(String message)
    {
        this.content_ = message;
        this.valid_ = true;
    }
    
    // Public member functions
    public boolean isValid()           { return valid_; }
    public void setContent(String msg) { this.content_ = msg; this.valid_ = true; }
    public String getContent()         { return this.content_; };

    // Variables
    private String content_;
    private boolean valid_;
}
