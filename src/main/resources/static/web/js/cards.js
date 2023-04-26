const { createApp } = Vue

createApp({
    data() {
        return {
            // Inicializamos las variables
            data: [],
            debitCards: [],
            creditCards: [],
            loans: [],
            params: "",
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get('http://localhost:8080/api/clients/current/cards')
                .then(response => {
                    this.data = response.data
                    console.log(this.data);
                    this.debitCards = response.data.filter(card => card.type == "DEBIT");
                    this.creditCards = response.data.filter(card => card.type == "CREDIT");      
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

const swiper = new Swiper(".swiper", {
    pagination: {
        el: ".swiper-pagination",
        type: "bullets",
    },
});