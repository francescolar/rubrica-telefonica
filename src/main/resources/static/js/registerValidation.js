new Vue({
    el: '#registerForm',
    data: {
        username: '',
        password: '',
        confirmPassword: '',
        firstName: '',
        lastName: '',
        errors: {
            username: false,
            password: false,
            confirmPassword: false,
            firstName: false,
            lastName: false
        }
    },
    methods: {
        validateForm() {
            this.errors.username = this.username.length < 4;
            this.errors.password = !this.validatePassword(this.password);
            this.errors.confirmPassword = this.password !== this.confirmPassword;
            this.errors.firstName = !this.firstName;
            this.errors.lastName = !this.lastName;

            if (!this.errors.username && !this.errors.password && !this.errors.confirmPassword && !this.errors.firstName && !this.errors.lastName) {
                this.$refs.registerForm.submit();
            }
        },
        validatePassword(password) {
            const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
            return passwordRegex.test(password);
        }
    }
});
