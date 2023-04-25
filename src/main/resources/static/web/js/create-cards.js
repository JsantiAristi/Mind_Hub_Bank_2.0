const { createApp } = Vue

createApp({
    data() {
        return {
            // Inicializamos las variables
            selectInput: "Open this select menu",
            checked : "",
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
        createCard() {
            Swal.fire({
                title: 'When you create a card you agree to our policy, you want to create a new card?',
                inputAttributes: {autocapitalize: 'off'},
                showCancelButton: true,
                confirmButtonText: 'Sure',
                confirmButtonColor: "#7c601893",
                footer: '<p data-bs-toggle="modal" data-bs-target="#exampleModal">See our Privacy Policy</p>',
                preConfirm: () => {
                    return axios.post('/api/clients/current/cards', `type=${this.checked}&color=${this.selectInput}`)
                        .then(response =>
                            Swal.fire({
                                icon: 'success',
                                text: 'Card created',
                                confirmButtonColor: "#7c601893",
                                timer: 2000,
                            })
                            .then(response => window.location.href = "/web/pages/cards.html")
                            .catch(error => console.log(error)))
                        .catch(error => {
                            Swal.fire({
                                icon: 'error',
                                text: error.response.data,
                                confirmButtonColor: "#7c601893",
                            })
                            console.log(error);
                        })
                },
                allowOutsideClick: () => !Swal.isLoading()
            })
            .catch(error => {console.log(error)})
        }
    },
}).mount("#app");