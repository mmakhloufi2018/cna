package ma.cdgp.af.ControllerThymeleaf;

import ma.cdgp.af.dto.af.PartenaireEnum;
import ma.cdgp.af.entity.ParametrageCollection;
import ma.cdgp.af.repository.CandidatsRepository;
import ma.cdgp.af.repository.ParametrageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Controller
public class HomeControllerThym {



    @GetMapping("/login")
    public String login() {
        return "login";
    }




    @RequestMapping("/stats")
    public ModelAndView statsPage() {
        return new ModelAndView("stats");
    }
}
