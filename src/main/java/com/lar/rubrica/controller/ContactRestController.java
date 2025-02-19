package com.lar.rubrica.controller;

import org.springframework.web.bind.annotation.*;

import com.lar.rubrica.module.Contact;
import com.lar.rubrica.util.DbUtility;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactRestController {

  @GetMapping("/all")
  public List<Contact> getAllContacts() throws SQLException {
    try (Connection c = DbUtility.createConnection()) {
      return DbUtility.viewContact(c, -1, -1, false);
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  @GetMapping("/owned")
  public List<Contact> getOwnedContacts() throws SQLException {
    try (Connection c = DbUtility.createConnection()) {
      int userId = DbUtility.getAuthenticatedUserId();
      return DbUtility.viewContact(c, -1, userId, true);
    } catch (Exception e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  @GetMapping("/non-owned")
  public List<Contact> getNonOwnedContacts() throws SQLException {
    try (Connection c = DbUtility.createConnection()) {
      int userId = DbUtility.getAuthenticatedUserId();
      return DbUtility.viewContact(c, -1, userId, false);
    } catch (Exception e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }
}