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
            dataFilter: 0,
            quotas: 0,
            account: "",
            amount: "",
            totalPay: 0,
            accountType: "",
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get('http://localhost:8080/api/clients/current')
                .then(response => {
                    this.data = response.data
                    console.log(this.data);
                    this.loans = this.data.loans.filter(loan => loan.finalAmount > 0);
                    this.accounts = this.data.accounts.filter(account => account.active);

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
        createAccount(){
            Swal.fire({
                title: 'When you create an account, you accept our privacy policy',
                text: 'Remember that you only can manage three accounts',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                confirmButtonColor: "#7c601893",
                footer: '<p data-bs-toggle="modal" data-bs-target="#exampleModal">See our Privacy Policy</p>',
                preConfirm: () => {
                    return axios.post('/api/clients/current/accounts',`accountType=${this.accountType}`)
                        .then(response => window.location.href="/web/pages/accounts.html")
                        .catch(error => {
                            Swal.fire({
                                icon: 'error',
                                text: error.response.data,
                                confirmButtonColor: "#7c601893",
                            })
                        })
                },
                allowOutsideClick: () => !Swal.isLoading()
            })
        },
        filterLoan(event){
            this.dataFilter = this.loans.filter( loan => {
                return event.target.alt.includes(loan.name)
            })[0]
            this.quotas = this.dataFilter.finalAmount / this.dataFilter.payments;
            this.totalPay = this.dataFilter.finalAmount;
        },
        payLoan(){
            Swal.fire({
                title: 'Are you sure that you want to pay the loan?',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                confirmButtonColor: "#7c601893",
                preConfirm: () => {
                    return axios.post('/api/current/loans', `idLoan=${this.dataFilter.id}&account=${this.account}&amount=${this.amount}`)
                    .then(response => {
                            Swal.fire({
                                icon: 'success',
                                text: 'Payment Success',
                                showConfirmButton: false,
                                timer: 2000,
                            }).then( () => window.location.href="/web/pages/accounts.html")
                        })
                        .catch(error => {
                            Swal.fire({
                                icon: 'error',
                                text: error.response.data,
                                confirmButtonColor: "#7c601893",
                            })
                        })
                },
                allowOutsideClick: () => !Swal.isLoading()
            })
        },
        deleteAccount(id){
            Swal.fire({
                title: 'Are you sure that you want to delete this account?',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                confirmButtonColor: "#7c601893",
                preConfirm: () => {
                    return axios.put('/api/clients/current/accounts', `idAccount=${id}`)
                    .then(response => {
                            Swal.fire({
                                icon: 'success',
                                text: 'account deleted',
                                showConfirmButton: false,
                                timer: 2000,
                            }).then( () => window.location.href="/web/pages/accounts.html")
                        })
                        .catch(error => {
                            Swal.fire({
                                icon: 'error',
                                text: error.response.data,
                                confirmButtonColor: "#7c601893",
                            })
                        })
                },
                allowOutsideClick: () => !Swal.isLoading()
            })
        },
    }
}).mount("#app");

window.onload = function(){
    $('#onload').fadeOut();
    $('body').removeClass("hidden");
}