const { createApp } = Vue

createApp({
    data() {
        return {
            // Inicializamos las variables
            firstName: "",
            secondName: "",
            emailAdress: "",
            password: ""
        }
    },
    methods: {
        signUp() {
            Swal.fire({
                title: 'When you create an account you agree to our policy, you want to create a new account?',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                confirmButtonColor: "#7c601893",
                preConfirm: () => {
                    return axios.post('/api/clients', `firstName=${this.firstName}&lastName=${this.secondName}&emailAdress=${this.emailAdress}&password=${this.password}`)
                        .then(response =>
                            axios.post('/api/login', `emailAdress=${this.emailAdress}&password=${this.password}`)
                            .then(response => window.location.href = "/web/pages/accounts.html")
                            .catch(error => { console.log(error)}))
                        .catch(error => {
                            Swal.fire({
                                icon: 'error',
                                title: 'Oops...',
                                text: 'This email is already in use',
                                confirmButtonColor: "#7c601893",
                            })
                        })
                },
                allowOutsideClick: () => !Swal.isLoading()
            })
            .catch(error => {console.log(error)})
        }
    },
    computed: {
        mayus(){
            this.firstName = this.firstName.charAt(0).toUpperCase() + this.firstName.slice(1);
            this.secondName = this.secondName.charAt(0).toUpperCase() + this.secondName.slice(1)
        }      
    }
}).mount("#app");