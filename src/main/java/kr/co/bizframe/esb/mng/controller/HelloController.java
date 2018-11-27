package kr.co.bizframe.esb.mng.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Welcome Page
 * @author bumma
 *
 */
@Controller
@RequestMapping("/")
public class HelloController {
	private Logger logger = Logger.getLogger(getClass());

	@Value("${file.save.dir:abc}")
	private String dir;

	@Value("${hawtio.url}")
	private String hawtioUrl;
	
	@RequestMapping(method = RequestMethod.GET) // value ="/"
	public String sayHello(ModelMap model) {
		return "welcome";
	}

	@RequestMapping(value = "/helloagain", method = RequestMethod.GET)
	public String sayHelloAgain(ModelMap model) {
		model.addAttribute("greeting",
				"Hello World Again, from Spring 4 MVC " + dir + " /// " + hawtioUrl);
		return "welcome";
	}

}
