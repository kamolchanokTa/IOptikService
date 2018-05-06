package optik.controllers;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import optik.models.user.User;
import optik.models.user.UserDao;
import optik.models.user.UserRequest;

/**
 * A class to test interactions with the MySQL database using the UserDao class.
 *
 * @author netgloo
 */
@Controller
public class UserController {

  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  /**
   * /create  --> Create a new user and save it in the database.
   * 
   * @param email User's email
   * @param name User's name
   * @return A string describing if the user is successfully created or not.
   */
  @RequestMapping(value = "/user/create",
		  method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<?>  create(@RequestBody UserRequest userRequest) {
    User user = null;
    try {
      user = new User(userRequest.email,
    		  userRequest.firstname,
    		  userRequest.lastname,
    		  userRequest.dob,
    		  userRequest.address,
    		  userRequest.city,
    		  userRequest.country,
    		  userRequest.zipcode,
    		  userRequest.userType.charAt(0),
    		  userRequest.password
    		  );
      userDao.save(user);
    }
    catch (Exception ex) {
    		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    		//return "Error creating the user: " + ex.toString();
    }
    return new ResponseEntity<>(user, HttpStatus.OK);
  }
  
  /**
   * /delete  --> Delete the user having the passed id.
   * 
   * @param id The id of the user to delete
   * @return A string describing if the user is successfully deleted or not.
   */
  @RequestMapping("/user/delete")
  @ResponseBody
  public String delete(UUID id) {
    try {
      User user = new User(id);
      userDao.delete(user);
    }
    catch (Exception ex) {
      return "Error deleting the user: " + ex.toString();
    }
    return "User successfully deleted!";
  }
  
  /**
   * /get-by-email  --> Return the id for the user having the passed email.
   * 
   * @param email The email to search in the database.
   * @return The user id or a message error if the user is not found.
   */
  @RequestMapping("/user/get-by-email")
  @ResponseBody
  public String getByEmail(String email) {
    String userId;
    try {
      User user = userDao.findByEmail(email);
      userId = String.valueOf(user.getId());
    }
    catch (Exception ex) {
      return "User not found";
    }
    return "The user id is: " + userId;
  }
  
  /**
   * /update  --> Update the email and the name for the user in the database 
   * having the passed id.
   * 
   * @param id The id for the user to update.
   * @param email The new email.
   * @param name The new name.
   * @return A string describing if the user is successfully updated or not.
   */
  @RequestMapping("/user/update")
  @ResponseBody
  public String updateUser(UUID id, String email, String name) {
    try {
      User user = userDao.findById(id).get();
      user.setEmail(email);
      //user.setName(name);
      userDao.save(user);
    }
    catch (Exception ex) {
      return "Error updating the user: " + ex.toString();
    }
    return "User successfully updated!";
  }

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------

  @Autowired
  private UserDao userDao;
  
} // class UserController