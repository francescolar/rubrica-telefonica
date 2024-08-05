document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#app',
        data: {
            users: [],
            contacts: [],
            searchUser: '',
            searchContacts: '',
            authId: null
        },
        mounted() {
            this.fetchUsers(),
            this.fetchContacts(),
            this.authId = this.$el.getAttribute('data-auth-id');
        },
        computed: {
            filteredUsers() {
                if (!this.searchUser) return this.users;
                return this.users.filter(user =>
                    Object.keys(user).some(key =>
                        String(user[key]).toLowerCase().includes(this.searchUser.toLowerCase())
                    )
                );
            },
            filteredContacts() {
                if (!this.searchContacts) return this.contacts;
                return this.contacts.filter(contact =>
                    Object.keys(contact).some(key =>
                        String(contact[key]).toLowerCase().includes(this.searchContacts.toLowerCase())
                    )
                );
            }
        },    
        methods: {
            fetchUsers() {
                axios.get('/api/users')
                .then(response => {
                    this.users = response.data;
                })
                .catch(error => {
                    console.error('Error fetching users:', error);
                });
            },
            fetchContacts() {
                axios.get('/api/contacts/all')
                .then(response => {
                    this.contacts = response.data;
                })
                .catch(error => {
                    console.error('Error fetching owned contacts:', error);
                });
            }
        }
    });
});   