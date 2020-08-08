package com.hateoes;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

	private UserRepo userRepo;

	@Autowired
	public void setUserRepo(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	public List<User> allUser() {

		List<User> users = userRepo.findAll();
		if (users != null) {
			return users;
		}

		return null;
	}

	// get user from db
	public User getUser(int id) {
		User user = userRepo.findById(id).orElse(null);

		if (user != null) {
			return user;
		}
		return null;
	}

	// add user to db
	public User addUser(User user) {
		User addeduser = userRepo.save(user);
		return addeduser;
	}

	// delete user from db
	public User deleteUser(int id) {
		List<User> users = userRepo.findAll();
		Iterator<User> user = users.iterator();
		User u = null;
		while (user.hasNext()) {
			u = user.next();
			if (u.getId() == id) {
				userRepo.delete(u);
				return u;
			}
		}
		return null;
	}

	public List<User> deleteAll() {
		List<User> user = null;
		List<User> presentUsers = userRepo.findAll();

		if (presentUsers.isEmpty()) {
			return user;
		} else {
			userRepo.deleteAll();
			return presentUsers;
		}
	}

	public User update(int id, User user) {
		User u = userRepo.findById(id).orElse(null);

		if (u != null) {

			User newUser = new User();
			// Cloning
			if (u != null) {
				if (u.getId() == id) {
					u.setName(user.getName());
					u.setMsg(user.getMsg());
					u.setDob(user.getDob());

					newUser = userRepo.save(u);
					// User newUSer = userRepo.updateById(id, updatedUser);
				}
				return newUser;
			}
		}
		return null;
	}

}
