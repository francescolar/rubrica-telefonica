document.addEventListener('DOMContentLoaded', function() {
    new Vue({
        el: '#app',
        data: {
            ownedContacts: [],
            nonOwnedContacts: [],
            searchOwned: '',
            searchNonOwned: '',
            showNonOwnedContacts: false,
            toggleText: 'Show'
        },
        mounted() {
            this.fetchOwnedContacts();
            this.fetchNonOwnedContacts();
        },
        computed: {
            filteredOwnedContacts() {
                if (!this.searchOwned) return this.ownedContacts;
                return this.ownedContacts.filter(contact =>
                    Object.keys(contact).some(key =>
                        String(contact[key]).toLowerCase().includes(this.searchOwned.toLowerCase())
                    )
                );
            },
            filteredNonOwnedContacts() {
                if (!this.searchNonOwned) return this.nonOwnedContacts;
                return this.nonOwnedContacts.filter(contact =>
                    Object.keys(contact).some(key =>
                        String(contact[key]).toLowerCase().includes(this.searchNonOwned.toLowerCase())
                    )
                );
            }
        },    
        methods: {
            fetchOwnedContacts() {
                axios.get('/api/contacts/owned')
                .then(response => {
                    this.ownedContacts = response.data;
                })
                .catch(error => {
                    console.error('Error fetching owned contacts:', error);
                });
            },
            fetchNonOwnedContacts() {
                axios.get('/api/contacts/non-owned')
                .then(response => {
                    this.nonOwnedContacts = response.data;
                })
                .catch(error => {
                    console.error('Error fetching non-owned contacts:', error);
                });
            },
            toggleContacts() {
                this.showNonOwnedContacts = !this.showNonOwnedContacts;
                this.toggleText = this.showNonOwnedContacts ? 'Hide' : 'Show';
            }
        }
    });
});      

document.addEventListener('DOMContentLoaded', function() {
    var scrollableDiv = document.querySelector('.owned-contacts');
    if (scrollableDiv) {
        scrollableDiv.addEventListener('wheel', function(event) {
        event.preventDefault();
    
        var scrollAmount = 500;
        var currentScroll = this.scrollTop;
    
        if (event.deltaY > 0) {
            this.scrollTo({
            top: currentScroll + scrollAmount,
            behavior: 'smooth'
            });
        } else {
            this.scrollTo({
            top: currentScroll - scrollAmount,
            behavior: 'smooth'
            });
        }
        });
    }
});



