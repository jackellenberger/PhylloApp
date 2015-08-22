package edu.uchicago.cs234.spr15.quokka.phyllo;

/**
 * Created by jellenberger on 5/14/15.
 */
public class ClassUserInfo {
    protected String userName;
    protected String emailAddress;
    protected String password;
    protected String[] tags;

    public String getUserName(){return userName;}
    public String getPassword(){return password;}
    public String getEmailAddress(){return emailAddress;}
    public String[] getTags(){return tags;}

    public void setUserName(String username){this.userName = username;}
    public void setPassword(String password){this.password = password;}
    public void setEmailAddress(String emailAddress){this.emailAddress = emailAddress;}
    public void setTags (String[] tags){this.tags = tags;}

}
