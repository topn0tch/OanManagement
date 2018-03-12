package com.oan.management.controller;

import com.oan.management.model.Rank;
import com.oan.management.model.User;
import com.oan.management.service.rank.RankService;
import com.oan.management.service.user.UserService;
import com.oan.management.utility.CustomTimeMessage;
import com.oan.management.utility.RandomQuote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller for the Home (index) page with widgets which can be customized by the /appsettings page
 */

@Controller
public class MainController {
    @Autowired
    private UserService userService;

    @Autowired
    private RankService rankService;

    @GetMapping("/")
    public String root(HttpServletRequest req, Model model, Authentication authentication) {
        User userLogged = userService.findByUser(authentication.getName());

        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            // Get the custom greeting by time
            CustomTimeMessage customTimeMessage = new CustomTimeMessage();
            model.addAttribute("timeGreeting", customTimeMessage.getMessage());

            // Update all attributes
            userService.updateUserAttributes(userLogged, req);
        }
        // JSON to Object mapper
        RandomQuote randomQuote = new RandomQuote("https://talaikis.com/api/quotes/random/");
        model.addAttribute("quote", randomQuote.getQuote(randomQuote.getUrl()));

        // Get user rank
        Rank userRank = rankService.getUserRank(userLogged);
        model.addAttribute("userRank", userRank);
        return "index";
    }
}
