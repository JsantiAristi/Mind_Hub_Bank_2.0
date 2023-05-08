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
            totalPay: 0,
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
                    return axios.post('/api/clients/current/accounts')
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
            console.log(this.dataFilter);
            this.quotas = this.dataFilter.finalAmount / this.dataFilter.payments;
            this.totalPay = this.dataFilter.finalAmount;
        },
    }
}).mount("#app");

window.onload = function(){
    $('#onload').fadeOut();
    $('body').removeClass("hidden");
}