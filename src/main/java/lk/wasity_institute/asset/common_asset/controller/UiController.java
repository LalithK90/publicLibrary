package lk.wasity_institute.asset.common_asset.controller;


import lk.wasity_institute.asset.user_management.entity.User;
import lk.wasity_institute.asset.user_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {

  private final UserService userService;

  @Autowired
  public UiController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping( value = {"/", "/index"} )
  public String index() {
    return "index";
  }

  @GetMapping( value = {"/home", "/mainWindow"} )
  public String getHome(Model model) {
    User user = userService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
    if ( user.getStudent() != null ) {
      return "redirect:/studentDetail";
    }
    return "mainWindow";
  }

}

