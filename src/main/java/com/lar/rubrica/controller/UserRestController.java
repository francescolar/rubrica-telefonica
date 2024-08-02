package com.lar.rubrica.controller;

import org.springframework.web.bind.annotation.*;

import com.lar.rubrica.module.User;
import com.lar.rubrica.util.DbUtility;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserRestController {
    @GetMapping
  public List<User> getAllContacts() throws SQLException {
    List<User> list = new ArrayList<>();
    Connection c = DbUtility.createConnection();
    try {
      list = DbUtility.viewUser(c);
      c.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }
}
