function togglePsw() {
    var password = document.getElementById("password");
    var pswButton = document.getElementById("show-password");
    if (password.type === "password") {
        password.type = "text";
        pswButton.innerHTML = "key";
    } else {
        password.type = "password";
        pswButton.innerHTML = "key_off";

    }
  }

  function toggleConfirmPsw() {
    var password = document.getElementById("confirmPassword");
    var pswButton = document.getElementById("show-password-confirm");
    if (password.type === "password") {
        password.type = "text";
        pswButton.innerHTML = "key";
    } else {
        password.type = "password";
        pswButton.innerHTML = "key_off";

    }
  }