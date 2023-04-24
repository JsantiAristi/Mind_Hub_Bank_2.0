const { createApp } = Vue;

createApp({
    data() {
        return {
            // Inicializamos las variables
            data: [],
            loans: [],
            accounts: [],
            totalBalance: 0,
            params: "",
            id: "",
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get('http://localhost:8080/api/clients/current')
                .then(response => {
                    console.log(response.data);

                    this.data = response.data
                    this.loans = this.data.loans
                    this.accounts = this.data.accounts

                    for (account of this.data.accounts) {
                        this.totalBalance += account.balance;
                    }
                })
                .catch(error => console.log(error));
        },
        singOut() {
            Swal.fire({
                title: 'Are you sure that you want to log out?',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                confirmButtonColor: "#7c601893",
                preConfirm: () => {
                    return axios.post('/api/logout')
                        .then(response => {
                            window.location.href="/index.html"
                        })
                        .catch(error => {
                            Swal.showValidationMessage(
                                `Request failed: ${error}`
                            )
                        })
                },
                allowOutsideClick: () => !Swal.isLoading()
            })
        },
    }
}).mount("#app");