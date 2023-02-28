package it.siw.viaggi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.siw.viaggi.controller.validator.CredentialsValidator;
import it.siw.viaggi.controller.validator.UserValidator;
import it.siw.viaggi.model.Credentials;
import it.siw.viaggi.model.User;
import it.siw.viaggi.service.CredentialsService;

@Controller
public class AuthenticationController {

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private UserValidator userValidator;

	@Autowired
	private CredentialsValidator credentialsValidator;

	@RequestMapping(value = "/register", method = RequestMethod.GET) 
	public String showRegisterForm (Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "signUp.html";
	}
	@RequestMapping(value = "/registerAdmin", method = RequestMethod.GET) 
	public String showRegisterAdminForm (Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "signUpAdmin.html";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET) 
	public String showLoginForm (Model model) {
		return "loginForm.html";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET) 
	public String logout(Model model) {
		return "index.html";
	}

	@RequestMapping(value = "/default", method = RequestMethod.GET)
	public String defaultAfterLogin(Model model) {

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			User user = getCurrentUser();
			model.addAttribute("user", user);
			return "homeAdmin.html";
		}else if (credentials.getRole().equals(Credentials.DEFAULT_ROLE)) {
			User user = getCurrentUser();
			model.addAttribute("user", user);
			return "homeDefault.html";
		}
		return "index.html";
	}    
	
	private User getCurrentUser () {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		User user = credentialsService.getUserDetails(username);
		return user;
	}

	@RequestMapping(value = { "/register" }, method = RequestMethod.POST)
	public String registerUser(@ModelAttribute("user") User user,
			BindingResult userBindingResult,
			@ModelAttribute("credentials") Credentials credentials,
			BindingResult credentialsBindingResult,
			Model model) {

		// validate user and credentials fields
		this.userValidator.validate(user, userBindingResult);
		this.credentialsValidator.validate(credentials, credentialsBindingResult);

		// if neither of them had invalid contents, store the User and the Credentials into the DB
		if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
			// set the user and store the credentials;
			// this also stores the User, thanks to Cascade.ALL policy
			credentials.setUser(user); //-->dopo il registerAdmin cambia messaggio iniziale
			credentialsService.saveCredentials(credentials);
			return "registrationSuccessful.html";
		}
		return "signUp.html";
	}
	
	@RequestMapping(value = { "/registerAdmin" }, method = RequestMethod.POST)
	public String registerAdmin(@ModelAttribute("user") User user,
			BindingResult userBindingResult,
			@ModelAttribute("credentials") Credentials credentials,
			BindingResult credentialsBindingResult,
			Model model) {

		// validate user and credentials fields
		this.userValidator.validate(user, userBindingResult);
		this.credentialsValidator.validate(credentials, credentialsBindingResult);

		// if neither of them had invalid contents, store the User and the Credentials into the DB
		if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
			// set the user and store the credentials;
			// this also stores the User, thanks to Cascade.ALL policy
			credentials.setUser(user);
			credentialsService.saveCredentialsAdmin(credentials);
			return "homeAdmin.html";
		}
		return "signUpAdmin.html";
	}
}
