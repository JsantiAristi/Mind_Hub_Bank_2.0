const { createApp } = Vue

createApp({
    data() {
        return {
            // Inicializamos las variables
            destinateAccount: "",
            account : "",
            amount : "",
            description : "",
            data: [],
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
        createTransactions(){
            Swal.fire({
                title: 'Are you sure that you want to transfer this amount to this account',
                inputAttributes: {autocapitalize: 'off'},
                showCancelButton: true,
                confirmButtonText: 'Sure',
                confirmButtonColor: "#7c601893",
                preConfirm: () => {
                    return axios.post('/api/clients/current/transactions', `amount=${this.amount}&description=${this.description}&initialAccount=${this.account}&destinateAccount=${this.destinateAccount}`)
                        .then(response =>
                            Swal.fire({
                                icon: 'success',
                                text: 'Transaction succesfully',
                                showConfirmButton: false,
                                timer: 2000,
                            })
                            .then( () => window.location.href="/web/pages/accounts.html")
                            .catch(error => console.log(error)))
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
            .catch(error => {console.log(error)})
        },
    },
    computed: {
        upperCase(){
            this.account = this.account.toUpperCase();
            this.destinateAccount = this.destinateAccount.toUpperCase();
        }
        
    }
}).mount("#app");

window.onload = function(){
    $('#onload').fadeOut();
    $('body').removeClass("hidden");
}