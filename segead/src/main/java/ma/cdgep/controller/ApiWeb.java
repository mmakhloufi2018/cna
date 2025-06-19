package ma.cdgep.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/web")
@ApiIgnore
public class ApiWeb {

	
	@GetMapping
	public String index() {
		
		 Resource resource = new ClassPathResource("index.html");
		    try {
		        InputStream inputStream = resource.getInputStream();
		        byte[] byteData = FileCopyUtils.copyToByteArray(inputStream);
		        String content = new String(byteData, StandardCharsets.UTF_8);
		        return content;
		    } catch (IOException e) {
		    }
		    return null;
	}
	
}
