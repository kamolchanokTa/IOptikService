package optik.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import optik.PropertiesReader;
import optik.models.user.*;
import optik.models.response.*;

/**
 * A class to test interactions with the MySQL database using the UserDao class.
 *
 * @author netgloo
 */
@Controller
public class UserController extends BaseController {

	// ------------------------
	// PUBLIC METHODS
	// ------------------------

	UserController() {
		super();
	}

	/**
	 * /create --> Create a new user and save it in the database.
	 * 
	 * @param User
	 *            Request
	 * @param appid
	 * @return A string describing if the user is successfully created or not.
	 */

	@RequestMapping(value = "/user/create", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody UserRequest userRequest,
			@RequestHeader(value = "appId") String appid) {
		if (isValidAppid(appid)) {
			User user = null;
			SimpleDateFormat sdf = new SimpleDateFormat(getAppProperties().getDateformat());

			Date convertedDob = null;
			try {
				if (userRequest.dob != null) {
					convertedDob = sdf.parse(userRequest.dob);
				}
				user = new User(userRequest.email, userRequest.firstname, userRequest.lastname, convertedDob,
						userRequest.address, userRequest.city, userRequest.country, userRequest.zipcode,
						userRequest.userType.charAt(0), userRequest.password);
				userDao.save(user);
			} catch (Exception ex) {
				return new ResponseEntity<>(new Response(getAppProperties().getStatus().getFail(),
						"Error creating the user: " + ex.toString(), null), HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(
					new Response(getAppProperties().getStatus().getSuccess(), "User successfully created!", user),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					new Response(getAppProperties().getStatus().getUnautherized(), "Access By unauthorized app", null),
					HttpStatus.FORBIDDEN);
		}

	}

	/**
	 * /delete --> Delete the user having the passed id.
	 * 
	 * @param id
	 *            The id of the user to delete
	 * @return A string describing if the user is successfully deleted or not.
	 */
	@RequestMapping("/user/delete")
	@ResponseBody
	public ResponseEntity<?> delete(String id, @RequestHeader(value = "appId") String appid) {
		if (isValidAppid(appid)) {
			try {
				UUID uuid = UUID.fromString(id);
				User user = userDao.findById(uuid).get();
				userDao.delete(user);
			} catch (Exception ex) {
				return new ResponseEntity<>(new Response(getAppProperties().getStatus().getFail(),
						"Error deleting the user: " + ex.toString(), null), HttpStatus.FORBIDDEN);
			}
			return new ResponseEntity<>(
					new Response(getAppProperties().getStatus().getSuccess(), "User successfully deleted!", null),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					new Response(getAppProperties().getStatus().getUnautherized(), "Access By unauthorized app", null),
					HttpStatus.FORBIDDEN);
		}
	}

	@RequestMapping("/users")
	@ResponseBody
	public ResponseEntity<?> getUsers(String id, @RequestHeader(value = "appId") String appid) {
		if (isValidAppid(appid)) {
			Iterator<User> users;
			try {
				users = userDao.findAll().iterator();
			} catch (Exception ex) {
				return new ResponseEntity<>(new Response(getAppProperties().getStatus().getFail(),
						"Error retrieve the users: " + ex.toString(), null), HttpStatus.FORBIDDEN);
			}
			return new ResponseEntity<>(users, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					new Response(getAppProperties().getStatus().getUnautherized(), "Access By unauthorized app", null),
					HttpStatus.FORBIDDEN);
		}
	}

//	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
//	@ResponseBody
//	public ResponseEntity<?> create(@RequestBody LoginRequest loginRequest,
//			@RequestHeader(value = "appId") String appid) {
//		if (isValidAppid(appid)) {
//			try {
//				User user = userDao.findByEmailAndPassword(loginRequest.email, loginRequest.password);
//				if(user != null) {
//					return new ResponseEntity<>(new Response(getAppProperties().getStatus().getSuccess(),"Found the user ", user)
//							, HttpStatus.OK);
//				}
//				else {
//					return new ResponseEntity<>(
//							new Response(getAppProperties().getStatus().getFail(),"Cannot find user with this username and password", null)
//							, HttpStatus.NOT_FOUND);
//				}
//			} catch (Exception ex) {
//				return new ResponseEntity<>(new Response(getAppProperties().getStatus().getFail(),
//						"Error creating the user: " + ex.toString(), null), HttpStatus.BAD_REQUEST);
//			}
//		} else {
//			return new ResponseEntity<>(
//					new Response(getAppProperties().getStatus().getUnautherized(), "Access By unauthorized app", null),
//					HttpStatus.FORBIDDEN);
//		}
//
//	}
	
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	  @ResponseBody
	  public ResponseEntity<?>  create(@RequestBody LoginRequest loginRequest, @RequestHeader(value="appId") String appid, HttpServletRequest req) {
		  System.out.println("Checking the authentication: " + loginRequest.email.toString());
		  if(isValidAppid(appid) && req.getSession().getAttribute("User") == null) {
			  try {
				  User user = userDao.findByEmailAndPassword(loginRequest.email, loginRequest.password);
				  if (user != null) {
					  System.out.println("Successfully logined in...");
					  System.out.println(req.getSession().getId());
					  req.getSession().setAttribute("User", user);
					  req.getSession().setMaxInactiveInterval(60*10);
					  System.out.println(((User) req.getSession().getAttribute("User")).getEmail());
				  }
				  // return new ResponseEntity<>( user, HttpStatus.OK) ;
				  return new ResponseEntity<>( user , HttpStatus.OK) ;

			  }
			  catch (Exception ex) {
				  return new ResponseEntity<>( new Response(getAppProperties().getStatus().getFail(), "Error creating the user: " + ex.toString(), null), HttpStatus.BAD_REQUEST);
			  }
		  }
		  else {
			  if(req.getSession().getAttribute("User") != null) {
				  System.out.println("Already logined...");
				  return new ResponseEntity<>( new Response(getAppProperties().getStatus().getSuccess(), "Already logined", null) , HttpStatus.OK) ;
			  }
			  else {
				  System.out.println("no valid appid");
				  return new ResponseEntity<>( new Response(getAppProperties().getStatus().getUnautherized(), "Access By unauthorized app", null), HttpStatus.FORBIDDEN);
			  }
		  }	
	    
	  }
	  
	  @RequestMapping(value = "/user/logout", method = RequestMethod.GET)
	  @ResponseBody
	  public ResponseEntity<?>  logout(@RequestHeader(value="appId") String appid, HttpServletRequest req) {
		  System.out.println("Login out...");
		  if(isValidAppid(appid)) {
			  try {
				  if(req.getSession().getAttribute("User") != null)
				  {
					  req.getSession().setAttribute("User", null);
					  System.out.println("Successfully logout...");
					  req.getSession().invalidate();
				  }
				  // return new ResponseEntity<>( user, HttpStatus.OK) ;
				  return new ResponseEntity<>( new Response(getAppProperties().getStatus().getSuccess(), "Successfully logout", null), HttpStatus.OK) ;

			  }
			  catch (Exception ex) {
				  return new ResponseEntity<>( new Response(getAppProperties().getStatus().getFail(), "Error creating the user: " + ex.toString(), null), HttpStatus.BAD_REQUEST);
			  }
		  }
		  else {
			  System.out.println("no valid appid");
			  return new ResponseEntity<>( new Response(getAppProperties().getStatus().getUnautherized(), "Access By unauthorized app", null), HttpStatus.FORBIDDEN);
		  }	
	    
	  }

	@RequestMapping(value = "/user/update/address", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody UpdateAddressRequest addressRequest,
			@RequestHeader(value = "appId") String appid) {
		if (isValidAppid(appid)) {
			try {
				UUID uuid = UUID.fromString(addressRequest.id);
				User user = userDao.findById(uuid).get();
				user.setAddress(addressRequest.address, addressRequest.city, addressRequest.country,
						addressRequest.zipcode);
				userDao.save(user);
				return new ResponseEntity<>(
						new Response(getAppProperties().getStatus().getSuccess(), "Success updating the user address", user),
						HttpStatus.OK);
			} catch (Exception ex) {
				return new ResponseEntity<>(new Response(getAppProperties().getStatus().getFail(),
						"Error updating the user address: " + ex.toString(), null), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>(
					new Response(getAppProperties().getStatus().getUnautherized(), "Access By unauthorized app", null),
					HttpStatus.FORBIDDEN);
		}

	}
	
	@RequestMapping(value = "/user/update/credit", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody UpdateCreditCardRequest creditCardRequest,
			@RequestHeader(value = "appId") String appid) {
		if (isValidAppid(appid)) {
			try {
				UUID uuid = UUID.fromString(creditCardRequest.id);
				User user = userDao.findById(uuid).get();
				user.setCreditCard(creditCardRequest.creditcardnumber, creditCardRequest.creditcardtype, creditCardRequest.cardexpmonth, creditCardRequest.cardexpyear);
				userDao.save(user);
				return new ResponseEntity<>(
						new Response(getAppProperties().getStatus().getSuccess(), "Success updating the user credit dard", user),
						HttpStatus.OK);
			} catch (Exception ex) {
				return new ResponseEntity<>(new Response(getAppProperties().getStatus().getFail(),
						"Error updating the user credit card: " + ex.toString(), null), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>(
					new Response(getAppProperties().getStatus().getUnautherized(), "Access By unauthorized app", null),
					HttpStatus.FORBIDDEN);
		}

	}

	/**
	 * /get-by-email --> Return the id for the user having the passed email.
	 * 
	 * @param email
	 *            The email to search in the database.
	 * @return The user id or a message error if the user is not found.
	 */
	@RequestMapping("/user/get-by-email")
	@ResponseBody
	public String getByEmail(String email) {
		String userId;
		try {
			User user = userDao.findByEmail(email);
			userId = String.valueOf(user.getId());
		} catch (Exception ex) {
			return "User not found";
		}
		return "The user id is: " + userId;
	}

	/**
	 * /update --> Update the email and the name for the user in the database having
	 * the passed id.
	 * 
	 * @param id
	 *            The id for the user to update.
	 * @param email
	 *            The new email.
	 * @param name
	 *            The new name.
	 * @return A string describing if the user is successfully updated or not.
	 */
	@RequestMapping("/user/update")
	@ResponseBody
	public String updateUser(UUID id, String email, String name) {
		try {
			User user = userDao.findById(id).get();
			user.setEmail(email);
			// user.setName(name);
			userDao.save(user);
		} catch (Exception ex) {
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