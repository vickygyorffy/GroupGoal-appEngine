package groupgoal.frontend;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Place entity.
 */
@Entity
public class Goal {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Key key;
  private String placeId;
  private String name;
  private String address;
  private String description;
  private GeoPt location;
  private User owner;
  private ArrayList<User> attendees;
  private Date date;
  private boolean isPrivate;
  private Category category;
  
  protected enum Category {
	    A, B, C, D,
	    E, F, G 
  };

  public Key getKey() {
    return key;
  }

  public String getPlaceId() {
    return placeId;
  }

  public void setplaceID(String placeId) {
    this.placeId = placeId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
  
  public String getDescription() {
	  return description;
  }
  
  public void setDescription(String description) {
	  this.description = description;
  }

  public GeoPt getLocation() {
    return location;
  }
  
  public void setLocation(GeoPt location) {
    this.location = location;
  }
  
  public User getOwner() {
	  return owner;
  }
  
  public void setOwner(User owner) {
	  this.owner = owner;
  }
  
  public Date getDate() {
	  return date;
  }
  
  public void setDate(Date date) {
	  this.date = date;
  }
  
  public boolean isPrivate() {
	  return isPrivate;
  }
  
  public void setPrivate (boolean isPrivate) {
	  this.isPrivate = isPrivate;
  }
  
  public Category getCategory() {
	  return category;
  }
  
  public void setCategory(Category category) {
	  this.category = category;
  }
  
  public void addAttendee(User user) {
	  if(!attendees.contains(user)) {
		  attendees.add(user);
	  }
  }
  
  public void removeAttendee(User user) {
	  if(attendees.contains(user)) {
		  attendees.remove(user);
	  }
  }
  
  public ArrayList<User> getAttendees() {
	  return attendees;
  }
  
  public void setAttendees(ArrayList<User> attendees) {
	  this.attendees = attendees;
  }
}