document.addEventListener('DOMContentLoaded', function() {
new Vue({
    el: '#editForm',
    data: {
        username: document.getElementById('username').value,
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        errors: {
            username: false,
            firstName: false,
            lastName: false
        }
    },
    methods: {
        validateForm() {
            this.errors.username = this.username.length < 4;
            this.errors.firstName = !this.firstName;
            this.errors.lastName = !this.lastName;
        
            if (!this.errors.username && !this.errors.firstName && !this.errors.lastName) {
                this.$refs.editContactForm.submit();
            }
        },
        cancel() {
            this.$refs.cancel.submit();
        },
        submitForm() {
            this.$refs.editContactForm.submit();
        },
        submitDeleteForm() {
            this.$refs.deleteContactForm.submit();
        }
    }
});
});