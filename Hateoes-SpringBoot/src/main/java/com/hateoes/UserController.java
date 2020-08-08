package com.hateoes;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	// public void setUserService(UserService userService) {
	// this.userService = userService;
	// }

	// // for getting single user
	// @GetMapping("/{id}")
	// public Optional<User> getSingleUser(@PathVariable("id") int id) {
	// Optional<User> user = userService.getUser(id);
	// if (user == null) {
	// throw new UserNotFoundException("User Not Found");
	// }
	// return user;
	// }

	// for getting single user
	@GetMapping("/{id}")
	public ResponseEntity<?> getSingleUser(@PathVariable("id") int id) {

		User user = userService.getUser(id);

		if (user == null) {
			throw new UserNotFoundException("User Not Found");
		} else {

			EntityModel<User> resource = EntityModel.of(user);
			/*
			 * resource.add(
			 * WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass
			 * ()). deleteUser(id)).withRel("delete"));
			 */
			List<String> alloweActions = alowedAction(user);
			alloweActions.stream().forEach(action -> {

				if (action.equalsIgnoreCase("DELETE")) {

					resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).deleteUser(id))
							.withRel("delete"));

				}
				if (action.equalsIgnoreCase("ADD")) {
					resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).createUser(user))
							.withRel("createUser"));

				}
				if (action.equalsIgnoreCase("UPDATE")) {
					resource.add(
							WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).updateUser(id, user))
									.withRel("update"));

				}

			});
			return new ResponseEntity<>(resource, HttpStatus.OK);
		}
	}

	@PutMapping("/{id}")
	public User updateUser(@PathVariable("id") Integer id, @RequestBody User user) {
		User newUser = userService.update(id, user);
		if (newUser != null) {
			return newUser;
		}
		throw new UserNotFoundException("There is no user for this id");
	}

	// // for getting all users
	// @GetMapping("/users"})
	// public List<User> getAllUser() {
	// List<User> users = userService.allUser();
	//
	// if (users == null) {
	// throw new UserNotFoundException("There is no user present");
	// }
	// return users;
	// }

	// for getting all users
	@GetMapping(path = "/users", produces = { "application/hal+json" })
	public List<User> getAllUser() {
		List<User> users = userService.allUser();

		if (users == null) {
			throw new UserNotFoundException("There is no user present");
		}
		return users;
	}

	// for adding the user
	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		URI location;

		if (user != null) {
			User savedUser = userService.addUser(user);
			location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
					.toUri();
			return ResponseEntity.created(location).build();
		}
		throw new UserCanNotBeBlank("Please provide user data");
	}

	/*
	 * @PostMapping("/users") public User createUser(@Valid @RequestBody User
	 * user) { User savedUser = userService.addUser(user); return savedUser; }
	 */
	// api for deleting the data
	@DeleteMapping("/{id}")
	@ResponseBody
	public User deleteUser(@PathVariable int id) {

		User u = userService.deleteUser(id);
		if (u == null) {
			throw new UserNotFoundException("User not found");
		}
		return u;
	}

	@DeleteMapping("/users")
	public List<User> deleteAllUser() {

		List<User> users = userService.deleteAll();
		if (users == null) {
			throw new UserNotFoundException("There is no users present");
		}
		return users;
	}

	// for sending link operation
	protected List<String> alowedAction(User u) {
		List<String> actions = new ArrayList<>();

		User usr = userService.getUser(u.getId());

		if (usr != null) {
			actions.add("DELETE");

			return actions;
		} else {
			actions.add("ADD");
			actions.add("UPDATE");

			return actions;
		}
	}
}
