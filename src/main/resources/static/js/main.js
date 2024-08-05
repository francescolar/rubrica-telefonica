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

  function toggleOldPsw() {
    var password = document.getElementById("old-password");
    var pswButton = document.getElementById("show-old-password");
    if (password.type === "password") {
        password.type = "text";
        pswButton.innerHTML = "key";
    } else {
        password.type = "password";
        pswButton.innerHTML = "key_off";

    }
  }

  function toggleNewPsw() {
    var password = document.getElementById("newPassword");
    var pswButton = document.getElementById("show-new-password");
    if (password.type === "password") {
        password.type = "text";
        pswButton.innerHTML = "key";
    } else {
        password.type = "password";
        pswButton.innerHTML = "key_off";

    }
  }

  new Vue({
    el: '#editApp',
    data: {
        isEditing: false,
    },
    methods: {
        submitForm() {
            this.$refs.editContactForm.submit();
        },
        submitDeleteForm() {
            this.$refs.deleteContactForm.submit();
        },
        toggleView() {
            this.isEditing = !this.isEditing;
        },
        cancel() {
            this.$refs.cancel.submit();
        }
    }
});