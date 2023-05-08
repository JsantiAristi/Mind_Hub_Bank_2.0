const { createApp } = Vue

createApp({
    data() {
        return {
            // Inicializamos las variables
            data: [],
            selectInput: "",
            checked : "",
            amount: "",
            amountModal: 0,
            dataFilter : "",
            idLoan : "",
            data2 : "",
            account : "",
            interestDay : "",
            amountInterest : 0,
            quotas : 0,
        }
    },
    created() {
        this.dataLoan();
        this.loadData();
    },
    methods: {
        dataLoan(){
            axios.get("/api/loans")
            .then(response => {
                this.data = response.data;
            })
            .catch(error => console.log(error))
        },
        loadData() {
            axios.get('http://localhost:8080/api/clients/current')
                .then(response => {
                    this.data2 = response.data
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
        filterSelector(){
            this.dataFilter = this.data.filter( loan => {
                return this.checked.includes(loan.name)
            })[0]
        },
        createLoan(){
            this.interestRatio()
            this.idLoan = this.dataFilter.id;
            Swal.fire({
                title: 'Are you sure that you want to apply to this loan?',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                confirmButtonColor: "#7c601893",
                footer: '<p data-bs-toggle="modal" data-bs-target="#exampleModal" class="fs-3 fw-bold">See the interst!</p>',
                preConfirm: () => {
                    return axios.post('/api/loans' , {
                        "id" : this.idLoan,
                        "amount" : this.amount,
                        "payments" : this.selectInput,
                        "account" : this.account
                        })
                        .then(response => {
                            Swal.fire({
                                icon: 'success',
                                text: 'Your application was succesfully',
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
        interestRatio(){
            this.amountModal = this.amount;
            this.amountInterest = this.amount * 1.2;
            this.quotas = this.amountInterest / this.selectInput;
        },
    },
}).mount("#app");

window.onload = function(){
    $('#onload').fadeOut();
    $('body').removeClass("hidden");
}

