package lk.wasity_institute.configuration;


import lk.wasity_institute.asset.user_management.entity.Enum.UserSessionLogStatus;
import lk.wasity_institute.asset.user_management.entity.UserSessionLog;
import lk.wasity_institute.asset.user_management.service.UserService;
import lk.wasity_institute.asset.user_management.service.UserSessionLogService;

@Component( "customAuthenticationSuccessHandler" )
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserSessionLogService userSessionLogService;
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        //do some logic here if you want something to be done whenever
        User authUser = userService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
//if user already have failure attempt clean before a save new session log

        //the user successfully logs in.
        UserSessionLog userSessionLog = new UserSessionLog();
        userSessionLog.setUser(authUser);
        userSessionLog.setUserSessionLogStatus(UserSessionLogStatus.LOGGED);
        userSessionLog.setCreatedAt(LocalDateTime.now());
        userSessionLogService.persist(userSessionLog);

             /*
        //default session is ok ->>>>>
        HttpSession session = request.getSession();
        session.setAttribute("username", authUser.getUsername());
        session.setAttribute("authorities", authentication.getAuthorities());

        */

        //set our response to OK status
        response.setStatus(HttpServletResponse.SC_OK);

        //since we have created our custom success handler, its up to us to where we will redirect the user after
        // successfully login
        response.sendRedirect("/mainWindow");
    }
}
