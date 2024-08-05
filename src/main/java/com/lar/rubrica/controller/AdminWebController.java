package com.lar.rubrica.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
import com.lar.rubrica.module.User;
import com.lar.rubrica.service.UserService;
import com.lar.rubrica.service.WebSecurityConfig;
import com.lar.rubrica.util.CryptoPassword;
import com.lar.rubrica.util.DbUtility;
import com.lar.rubrica.util.Generator;

import jakarta.validation.Valid;

@Controller
public class AdminWebController implements WebMvcConfigurer {
    
    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        registry.addViewController("/admin/dashboard").setViewName("admin-dashboard");
        registry.addViewController("/admin/editcontact").setViewName("admin-editcontact");
        registry.addViewController("/admin/edituser").setViewName("admin-edituser");
    }

    @Autowired
    private UserService userService;

    @GetMapping("/admin/dashboard")
    public String adminDash(Model model) {
        try {
            Connection c = DbUtility.createConnection();
            int authId = DbUtility.getAuthenticatedUserId();
            List<User> list = DbUtility.viewUser(c);
            model.addAttribute("authId", authId);
            model.addAttribute("users", list);
            c.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "admin/dashboard";
    }

    @GetMapping("/admin/editcontact")
    public String adminEditContact(@RequestParam("id") Integer contactId, Model model) {
        try {
            Connection c = DbUtility.createConnection();
            List<User> list = DbUtility.viewUser(c);
            Contact contact = DbUtility.viewContact(c, contactId, -1, false).get(0);
            model.addAttribute("users", list);
            model.addAttribute("contact", contact);
            DbUtility.closeConnection(c);
            return "admin/editcontact";
        } catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
            return "redirect:/admin/dashboard";
		}
    }

     @PostMapping("/admin/editcontact")
    public String admEditContactSave(@Valid @ModelAttribute Contact contact, Model model, @RequestParam("avatar") MultipartFile avatar, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            return "redirect:/error";
        }
        try {
            //fname, lname, email, tel, ownerId
            Connection c = DbUtility.createConnection();
            DbUtility.admUpdateContactPreparedStatement(c,
                        contact.getId(),
                        contact.getFname(),
                        contact.getLname(),
                        contact.getEmail(),
                        contact.getTel(),
                        contact.getOwnerId());
            DbUtility.updateContactInitials(contact.getId(), contact.getFname(), contact.getLname());
            DbUtility.saveUploadedFile(avatar, contact.getOwnerId(), contact.getId());
            DbUtility.closeConnection(c);
            System.out.println("Contact updated");
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam("id") Integer userId, @RequestParam("username") String username) {
        try {
            Connection c = DbUtility.createConnection();
            DbUtility.deleteUserPreparedStatement(c, userId, username);
            DbUtility.closeConnection(c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/admdelete")
    public String adminDeleteContact(@RequestParam("id") Integer contactId) {
        try {
            Connection c = DbUtility.createConnection();
            DbUtility.deleteContactPreparedStatement(c, contactId);
            DbUtility.closeConnection(c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/enable")
    public String enableUser(@RequestParam("id") Integer userId, @RequestParam("enabled") boolean enabled) {
        try {
            Connection c = DbUtility.createConnection();
            DbUtility.toggleEnableUser(c, userId, enabled);
            DbUtility.closeConnection(c);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/toggle-admin")
    public String toggleAdmin(@RequestParam("username") String username, @RequestParam("role") String role) {
        try {
            Connection c = DbUtility.createConnection();
            DbUtility.toggleAdminUser(c, username, role);
            DbUtility.closeConnection(c);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/edituser")
    public String adminEditProfileGet(@RequestParam("id") Integer userId, Model model) {
        try {
            User user = DbUtility.getUserDetails(userId);
            model.addAttribute("user", user);
            return "admin/edituser";
        } catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
            return "redirect:/homepage";
		}
    }

    @PostMapping("/admin/edituser")
    public String adminEditProfilePost(@ModelAttribute("user") User user, @RequestParam(name = "reset-psw", defaultValue = "false") boolean resetPsw, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "redirect:/error";
        }
        try {
            //String fname, String lname, String username, String password, int id
            Connection c = DbUtility.createConnection();
            User dbUser = DbUtility.getUserDetails(user.getId());
            if (!user.getUsername().equals(dbUser.getUsername())) {
                boolean userExist = DbUtility.checkUsername(c, user.getUsername());
                if (!userExist) {
                    model.addAttribute("usernameCheck", true);
                    DbUtility.closeConnection(c);
                    return "admin/edituser";
                }
            }
            if (resetPsw) {
                String salt = BCrypt.gensalt();
                String newPsw = Generator.getRandomPassword();
                String newCryptedPassword = CryptoPassword.cryptoPasswordwithSalt(newPsw, salt);
                System.out.println(newPsw);
                // metodo per inviare via mail la psw non cifrata all'utente Send.emailTo(user.getEmail(), newPsw);
                UserService.updateExistingUser(user.getFname(), user.getLname(), user.getUsername(), newCryptedPassword, user.getId());
            } else {
                UserService.updateExistingUser(user.getFname(), user.getLname(), user.getUsername(), dbUser.getPassword(), user.getId());
            }
            DbUtility.closeConnection(c);
            return "redirect:/admin/dashboard";
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }
}