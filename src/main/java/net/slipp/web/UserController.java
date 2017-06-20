package net.slipp.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.slipp.domain.User;
import net.slipp.domain.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {
	
	private List<User> users = new ArrayList<User>();
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/form")
	public String form() {
		return "/user/form";
	}
	
	@GetMapping("/loginForm")
	public String loginForm() {		
		return "/user/login";
	}
	
	@PostMapping("/login")
	public String login(String userId, String password, HttpSession session) {
		
		User user = userRepository.findByuserId(userId);
		if (user == null) {
			return "redirect:/users/loginForm";
		}		
		if (!(user.mathPassword(password))) {
			return "redirect:/users/loginForm";			
		}		
		session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);			
		return "redirect:/";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
			
		session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
		return "redirect:/";
	}
	
	@PostMapping("")
	public String create(User user) {
		System.out.println("user : " + user);
		users.add(user);
		userRepository.save(user);
		return "redirect:/users";
	}
	
	@GetMapping("")
	public String list(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "/user/list";
	}
	
	@GetMapping("/{id}/form")
	public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
		
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "redirect:/users/loginForm";			
		}
		if (!HttpSessionUtils.getUserFormSession(session).mathId(id)) {
			throw new IllegalStateException("자신의 정보만 수정할 수 있습니다.");
		}
		
		model.addAttribute("user", userRepository.findOne(id));
		return "/user/updateForm";
	}	
	
	@PutMapping("/{id}")
	public String update(@PathVariable Long id, User updatedUser, HttpSession session) {
		
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "redirect:/users/loginForm";			
		}
		if (!HttpSessionUtils.getUserFormSession(session).mathId(id)) {
			throw new IllegalStateException("자신의 정보만 수정할 수 있습니다.");
		}
		
		User user = userRepository.findOne(id);
		user.update(updatedUser);
		userRepository.save(user);
		return "redirect:/users";
	}
}
