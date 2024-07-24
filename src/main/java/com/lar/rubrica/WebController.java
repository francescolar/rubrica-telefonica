package com.lar.rubrica;

import static java.lang.Integer.parseInt;
import java.sql.Connection;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.validation.Valid;

@Controller
public class WebController implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/homepage").setViewName("homepage");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/login").setViewName("login");

    }

    @GetMapping("/")
    public String showHome() {
        return "homepage";
    }

    @GetMapping("/login")
    public String showForm(Model model) {
        model.addAttribute("users", new Users());
        return "login";
    }

    @PostMapping("/login")
    public String checkPersonInfo(@Valid @ModelAttribute Users user, Model model, BindingResult bindingResult) {

        boolean userRegistered = false;
        if (bindingResult.hasErrors()) {
            return "login";
        }
        try {
            Connection c = DbUtility.createConnection();
            List<Users> list = DbUtility.viewUsers(c);
            DbUtility.closeConnection(c);
            for (Users elem : list) {

                if (elem.getUsername().equals(user.getUsername()) && CryptoPassword
                        .cryptoPasswordwithSalt(user.getPassword(), elem.getSalt()).equals(elem.getPassword())) {
                    userRegistered = true;
                    user.setSalt(elem.getSalt());
                    user.setFname(elem.getFname());
                    user.setLname(elem.getLname());
                    System.out.println("Login successful");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("registered", userRegistered);

        if (userRegistered) {
            model.addAttribute("users", user);
            return "success";
        } else
            return "login";

    }

    
    @GetMapping("/createcontact")
    public String createContactPage(Model model) {
        model.addAttribute("users", new Users());
        model.addAttribute("contacts", new Contacts());
        return "createcontact";
    }

    @PostMapping("/createcontact")
    public String createContact(@ModelAttribute Users user, @Valid @ModelAttribute Contacts contact, Model model, BindingResult bindingResult) {
        
        boolean contactRegistered = false;
        if (bindingResult.hasErrors()) {
            return "login";
        }
        try {
            //fname, lname, email, tel, ownerId
            Connection c = DbUtility.createConnection();
            DbUtility.insertContactPreparedStatement(c,
                        contact.getFname(),
                        contact.getLname(),
                        contact.getEmail(),
                        contact.getTel());
            DbUtility.closeConnection(c);
            System.out.println("Contact added");
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("registered", contactRegistered);

        if (contactRegistered) {
            model.addAttribute("contacts", contact);
            return "success";
        } else
            model.addAttribute("ex", !contactRegistered);
        return "createcontact";
    }

    @GetMapping("/register")
    public String showForm2(Model model) {
        model.addAttribute("users", new Users());
        return "register";
    }

    @PostMapping("/register")
    public String checkPersonInfo2(@Valid @ModelAttribute Users user, Model model, BindingResult bindingResult) {

        boolean registrazioneEffettuata = true;
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            String salt = BCrypt.gensalt();
            Connection c = DbUtility.createConnection();
            // Aggiungere controllo tra account esistenti

            if (DbUtility.checkUsername(c, user.getUsername())) {
                DbUtility.insertUserPreparedStatement(c,
                        user.getUsername(),
                        CryptoPassword.cryptoPasswordwithSalt(user.getPassword(), salt),
                        salt,
                        user.getFname(),
                        user.getLname());

                registrazioneEffettuata = true;
            } else {
                registrazioneEffettuata = false;
            }

            DbUtility.closeConnection(c);
        } catch (Exception e) {
            e.printStackTrace();

        }

        if (registrazioneEffettuata) {
            model.addAttribute("users", user);
            return "success";
        } else
            model.addAttribute("ex", !registrazioneEffettuata);
        return "register";

    }

    @GetMapping("/view")
	public String viewUser(Model model) {
		try {
			Connection c = DbUtility.createConnection();
			List<Contacts> list = DbUtility.viewContacts(c, -1);
			DbUtility.closeConnection(c);
			model.addAttribute("contacts", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "view";
	}

    @GetMapping("/editcontact")
    public String editcontact(@RequestParam("id") Integer contactId,Model model) {
        try {
            Connection c = DbUtility.createConnection();
            Contacts contact = DbUtility.viewContacts(c, contactId).get(0);
            model.addAttribute("contacts", contact);
            DbUtility.closeConnection(c);
            return "editcontact";
        } catch (Exception e) {
			e.printStackTrace();
		}
		return "editcontact";
    }

    @PostMapping("/editcontact")
    public String editContactSave(@Valid @ModelAttribute Contacts contact, Model model, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            return "login";
        }
        try {
            //fname, lname, email, tel, ownerId
            Connection c = DbUtility.createConnection();
            DbUtility.updateContactPreparedStatement(c,
                        Integer.toString(contact.getId()),
                        contact.getFname(),
                        contact.getLname(),
                        contact.getEmail(),
                        contact.getTel());
            DbUtility.closeConnection(c);
            System.out.println("Contact updated");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/view";
    }
}