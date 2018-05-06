package optik.models.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

/**
 * An entity User composed by three fields (id, email, name).
 * The Entity annotation indicates that this class is a JPA entity.
 * The Table annotation specifies the name for the table in the db.
 *
 * @author Kamolchanok Tangsri
 */
@Entity
@Table(name = "user")
public class User {

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------
  
  // An autogenerated id (unique for each user in the db)
  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(columnDefinition = "BINARY(20)")
  private UUID id;
  
  // The user's email
  @NotNull
  private String email;
  
  // The user's password
  @NotNull
  private String password;
  
  // The user's first name
  @NotNull
  private String firstname;

  // The user's last name
  @NotNull
  private String lastname;
  
  // The user's Date of birth
  @Column(name = "DOB", columnDefinition="DATETIME")
  @Temporal(TemporalType.TIMESTAMP)
  private Date DOB;
  
  // The user's Address

  private String address;
  
//The user's city

 private String city;
 
//The user's country

private String country;

//The user's zipcode

private String zipcode;

// The user Type (0 = admin, 1 = customer)
@NotNull
private char userType;


  public User() { }

  public User(UUID id) { 
    this.id = id;
  }
  
  public User(String email, String firstName, String lastName, String dob, String address, String city, String country, String zipcode, char userType, String password) {
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	  Date convertedDob;
		try {
			convertedDob = sdf.parse(dob);
			this.DOB = convertedDob;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  this.email = email;
	  this.firstname = firstName;
	  this.lastname = lastName;
	  this.address = address;
	  this.city = city;
	  this.country = country;
	  this.zipcode = zipcode;
	  this.userType = userType;
	  this.password = password;
  }

  // Getter and setter methods

  public UUID getId() {
    return id;
  }

  public void setId(UUID value) {
    this.id = value;
  }

  public String getEmail() {
    return email;
  }
  
  public void setEmail(String value) {
    this.email = value;
  }
  
  public String getName() {
    return firstname.concat(" ").concat(lastname);
  }

//  public void setName(String value) {
//    this.name = value;
//  }
  
} // class User
