package com.hateoes;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private MessageSource messageSource;

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
		}

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

			if (action.equalsIgnoreCase("AllUSER")) {
				resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllUser())
						.withRel("getAllUser"));
			}

			if (action.equalsIgnoreCase("UPDATE")) {
				resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).updateUser(id, user))
						.withRel("updateUser"));

			}

		});
		return new ResponseEntity<>(resource, HttpStatus.OK);
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

	// dont return a list from api this is not a good prectis in java
	// for getting all users
	@GetMapping("/users")
	public List<User> getAllUser() {
		List<User> users = userService.allUser();

		if (users == null) {
			throw new UserNotFoundException("There is no user present");
		}

		return users;
	}

	// for adding the user
	@PostMapping("/users")
	public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
		URI location;

		if (user != null) {
			User savedUser = userService.addUser(user);
			/*
			 * location =
			 * ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
			 * buildAndExpand(savedUser.getId()) .toUri(); return
			 * ResponseEntity.created(location).build();
			 */

			EntityModel<User> em = EntityModel.of(savedUser);

			em.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllUser())
					.withRel("all-user"));

			return new ResponseEntity<>(em, HttpStatus.CREATED);
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

	// internationalization

	@GetMapping(path = "/i18n")
	public String internationalization(@RequestHeader(value = "Accept-Language", required = false) Locale local) {
		// return messageSource.getMessage("good.morning.message", null, local);

		return messageSource.getMessage("good.morning.message", null, LocaleContextHolder.getLocale());
	}

	// -------------------------------------------------------------------------------------------------
	// for sending link operation
	protected List<String> alowedAction(User u) {
		List<String> actions = new ArrayList<>();
		User usr = userService.getUser(u.getId());
		if (usr != null) {
			actions.add("DELETE");
			actions.add("UPDATE");
			actions.add("AllUSER");

			return actions;
		} else {
			actions.add("ADD");
			return actions;
		}
	}

}
