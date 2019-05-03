package unipos.auth.components.user;

import com.wordnik.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import unipos.auth.components.user.usernamePassword.UsernamePasswordService;

import java.util.List;

/**
 * Created by Dominik on 27.05.2015.
 */

/**
 * The annotation @RestController tells Spring that this class is a controller, whose actions contain response bodies
 */
@RestController

/**
 * With @RequestMapping, the actions of these classes are mapped to the url "/entities".
 * The result will be HTTP with application-type JSON.
 */
@RequestMapping(value="/users", produces= MediaType.APPLICATION_JSON_VALUE)
@Api(value="/users")

public class UserController {
    /**
     * The service that is used by the controller is injected by Springs IoC.
     */
    @Autowired
    private UserService userService;
    @Autowired
    private UsernamePasswordService usernamePasswordService;

    /**
     * This simple action will return a list of all entities in JSON-format.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value="Find all users",
            response = User.class,
            responseContainer = "List")
    public List<User> findAll()
    {
        return userService.findAllUsers();
    }

    /**
     * Returns the user with the given name
     * @param username the name og the user you are looking for
     * @return The User Instance as a JSON
     */
    @RequestMapping(value = "/username/{username}", method = RequestMethod.GET)
    @ApiOperation(value = "Find a specific User by his USERNAME", response = User.class)
    public User findUserByUsername(@PathVariable("username")String username) {
        return userService.findUserByUsername(username);
    }

    /**
     * Returns the user with the given ID
     * @param id the ID of the user you are looking for
     * @return the user with the given ID
     */
    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Find a specific User by his ID", response = User.class)
    public User findUserById(@PathVariable("id")String id) {
        return userService.findUserById(id);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "Create a new User")
    @ApiResponses({
            @ApiResponse(code = 200, message = "User created successful"),
            @ApiResponse(code = 500, message = "An error occured during the creating or saving process")
    })
    public User createUser(@RequestBody User user) {
        if(user.getId() == null) {
            userService.createUser(user);
        } else {
            userService.updateUser(user);
        }
        return user;
    }

    /**
     * Remove the user with the given ID
     * @param id The id of the user you want to remove
     */
    @RequestMapping(value = "/id", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Remove a user by his ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Deletion successful")
    })
    public @ResponseBody void removeUserById(@RequestParam("id")String id) {
        userService.deleteUser(id);
    }

    /**
     * Remove a user by his Username
     * @param username The name of the user you want to delete
     */
    @RequestMapping(value = "/username", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Remove a user by his USERNAME")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Deletion successful")
    })
    public void removeUserByUsername(@RequestParam("username") String username) {
        userService.deleteUserByUsername(username);
    }

    @RequestMapping(value = "/guid", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteByGuid(@RequestParam("guid") String guid) {
        userService.deleteByGuid(guid);
    }
}
