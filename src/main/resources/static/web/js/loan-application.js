const { createApp } = Vue

createApp({
    data() {
        return {
            // Inicializamos las variables
            data: [],
            selectInput: "",
            checked : "",
            amount: 0,
            dataFilter : "",
            idLoan : "",
            data2 : "",
            account : "",
            interestDay : "",
            amountInterest : "",
            quotas : "",
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
                console.log(this.data);
            })
            .catch(error => console.log(error))
        },
        loadData() {
            axios.get('http://localhost:8080/api/clients/current')
                .then(response => {
                    console.log(response.data);
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
            
            console.log(this.dataFilter[0]);
        },
        createLoan(){
            this.idLoan = this.dataFilter.id
            Swal.fire({
                title: 'Are you sure that you want to apply to this loan?',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                confirmButtonColor: "#7c601893",
                footer: '<p data-bs-toggle="modal" data-bs-target="#exampleModal" class="fs-3 fw-bold" @click="interestRatio">See the interst!</p>',
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
            this.interestDay = 0.2 / 365;
            this.amountInterest = this.amount * ( this.interestDay );
            this.quotas = this.amountInterest / this.selectInput;
            console.log(this.interestDay);
            console.log(this.amountInterest);
            console.log(this.quotas);
        },
    },
}).mount("#app");

window.onload = function(){
    $('#onload').fadeOut();
    $('body').removeClass("hidden");
}