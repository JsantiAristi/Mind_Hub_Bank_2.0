const { createApp } = Vue;

createApp({
    data() {
        return {
            name: "",
            amount: "",
            checked: [],
            description: "",
            interest: "",
        }
    },
    methods: {
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
        createLoan(){
            Swal.fire({
                title: 'Are you sure that you want to create this loan?',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                confirmButtonColor: "#7c601893",
                preConfirm: () => {
                    return axios.post('/api/loans/manager' , {
                        "name" : this.name,
                        "maxAmount" : this.amount,
                        "payments" : this.checked,
                        "descriptionLoan" : this.description,
                        "interest" : this.interest
                        })
                        .then(response => {
                            Swal.fire({
                                icon: 'success',
                                text: 'You create a new Loan',
                                showConfirmButton: false,
                                timer: 2000,
                            }).then( () => window.location.href="/management/pages/managerLoan.html")
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
    },
}).mount("#app");