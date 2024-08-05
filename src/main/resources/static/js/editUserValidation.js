document.addEventListener('DOMContentLoaded', function() {
new Vue({
    el: '#editForm',
    data: {
        username: document.getElementById('username').value,
        newPassword: '',
        confirmPassword: '',
        oldPassword: '',
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        errors: {
            username: false,
            newPassword: false,
            confirmPassword: false,
            firstName: false,
            lastName: false,
            oldPassword: false
        }
    },
    methods: {
        validateForm() {
            this.errors.username = this.username.length < 4;
            this.errors.newPassword = this.newPassword && !this.validatePassword(this.newPassword);
            this.errors.confirmPassword = this.newPassword && (this.newPassword !== this.confirmPassword);
            this.errors.firstName = !this.firstName;
            this.errors.lastName = !this.lastName;
            this.errors.oldPassword = this.oldPassword.length < 2;
        
            if (!this.errors.username && !this.errors.newPassword && !this.errors.confirmPassword && !this.errors.firstName && !this.errors.lastName && !this.errors.oldPassword) {
                this.$refs.editContactForm.submit();
            }
        },        
        validatePassword(newPassword) {
            const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
            return passwordRegex.test(newPassword);
        },
        cancel() {
            this.$refs.cancel.submit();
        }
    }
});
});