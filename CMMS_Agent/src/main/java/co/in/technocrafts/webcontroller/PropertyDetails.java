package co.in.technocrafts.webcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class PropertyDetails {
	
	@GetMapping("/welcome")
	public String welcome(Model model) {
		model.addAttribute("message","Hello World!");
		System.out.println("hii");
		return "welcome";
	}

}
