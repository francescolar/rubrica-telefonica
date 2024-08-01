package com.lar.rubrica.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lar.rubrica.module.Contact;
import com.lar.rubrica.module.ContactDetails;
import com.lar.rubrica.module.User;
import com.lar.rubrica.service.UserService;
import com.lar.rubrica.util.DbUtility;

import jakarta.validation.Valid;

@Controller
public class WebController implements WebMvcConfigurer {

    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        registry.addViewController("/homepage").setViewName("homepage");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/createcontact").setViewName("createcontact");
        registry.addViewController("/editcontact").setViewName("editcontact");
        registry.addViewController("/view").setViewName("view");
        registry.addViewController("/error").setViewName("error");
    }

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String showHome(Model model) {
        try {
            User user = DbUtility.getUserDetails();
            model.addAttribute("user", user);
            return "homepage";
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    @GetMapping("/homepage")
    public String showHome2(Model model) {
        try {
            User user = DbUtility.getUserDetails();
            model.addAttribute("user", user);
            return "homepage";
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    
    @GetMapping("/admin/dashboard")
    public String adminDash() {
        return "admin/dashboard";
    }
    
    @GetMapping("/error")
    public String errorPage() {
        return "error";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerGetMapping(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerPostController(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
        boolean registrazioneEffettuata = userService.registerUser(user, bindingResult);

        if (registrazioneEffettuata) {
            model.addAttribute("user", user);
            return "logout-success";
        } else {
            model.addAttribute("ex", !registrazioneEffettuata);
            return "register";
        }
    }

    @GetMapping("/logout-success")
    public String showLogoutSuccess() {
        return "logout-success";
    }
    
    
    @GetMapping("/createcontact")
    public String createContactPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("contact", new Contact());
        model.addAttribute("contactDetails", new ContactDetails());
        return "createcontact";
    }

    @PostMapping("/createcontact")
    public String createContact(@Valid @ModelAttribute Contact contact, @ModelAttribute ContactDetails contactDetails, @RequestParam("avatar") MultipartFile avatar,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "createcontact";
        }
        try {
            int ownerId = DbUtility.getAuthenticatedUserId();
            Connection c = DbUtility.createConnection();
            int contactId = DbUtility.insertContactPreparedStatement(c, contact.getFname(), contact.getLname(), contact.getEmail(), contact.getTel(), ownerId);
            DbUtility.setContactInitials(contactId, contact.getFname(), contact.getLname());
            DbUtility.saveUploadedFile(avatar, ownerId, contactId);
            DbUtility.closeConnection(c);
            return "redirect:/view";
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }

    @GetMapping("/view")
	public String viewContacts(Model model) {
		try {
            int userId = DbUtility.getAuthenticatedUserId();
			Connection c = DbUtility.createConnection();
			List<Contact> ownedContactList = DbUtility.viewContact(c, -1, userId, true);
			List<Contact> allContactList = DbUtility.viewContact(c, -1, userId, false);
			DbUtility.closeConnection(c);
			model.addAttribute("ownedContacts", ownedContactList);
			model.addAttribute("allContacts", allContactList);
            model.addAttribute("userId", userId);
            return "view";
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
            return "redirect:/error";
		}
	}

    @GetMapping("/editcontact")
    public String editcontact(@RequestParam("id") Integer contactId, Model model) {
        try {
            Connection c = DbUtility.createConnection();
            Contact contact = DbUtility.viewContact(c, contactId, -1, false).get(0);
            model.addAttribute("contact", contact);
            DbUtility.closeConnection(c);
            return "editcontact";
        } catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
            return "redirect:/view";
		}
    }

    @PostMapping("/editcontact")
    public String editContactSave(@Valid @ModelAttribute Contact contact, Model model, @RequestParam("avatar") MultipartFile avatar, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            return "login";
        }
        try {
            //fname, lname, email, tel, ownerId
            Connection c = DbUtility.createConnection();
            int userId = DbUtility.getAuthenticatedUserId();
            DbUtility.updateContactPreparedStatement(c,
                        contact.getId(),
                        contact.getFname(),
                        contact.getLname(),
                        contact.getEmail(),
                        contact.getTel());
            DbUtility.updateContactInitials(contact.getId(), contact.getFname(), contact.getLname());
            DbUtility.saveUploadedFile(avatar, userId, contact.getId());
            DbUtility.closeConnection(c);
            System.out.println("Contact updated");
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "redirect:/view";
    }

    @PostMapping("/delete")
    public String deleteContact(@RequestParam("id") Integer contactId) {
        try {
            Connection c = DbUtility.createConnection();
            DbUtility.deleteContactPreparedStatement(c, contactId);
            DbUtility.closeConnection(c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "redirect:/view";
    }
}