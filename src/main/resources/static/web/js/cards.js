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
            date: "",
            actualDate: "",
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
                    this.debitCards = response.data.filter(card => card.type == "DEBIT" && card.active);
                    this.creditCards = response.data.filter(card => card.type == "CREDIT" && card.active);   
                    
                    // this.date = Date.now();
                    this.actualDate = new Date().toLocaleDateString().split(",")[0].split("/").reverse().join("-");
                    console.log(this.actualDate);
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
        deleteCard(id){
            Swal.fire({
                title: 'Are you sure that you want to delete this card?',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                confirmButtonColor: "#7c601893",
                preConfirm: () => {
                    return axios.put('/api/clients/current/cards',`idCard=${id}`)
                        .then(response => {
                            window.location.href="/web/pages/cards.html"
                        })
                        .catch(error => {
                            Swal.showValidationMessage(
                                `Request failed: ${error.response.data}`
                            )
                        })
                },
                allowOutsideClick: () => !Swal.isLoading()
            })
        }
    }
}).mount("#app");

const swiper = new Swiper(".swiper", {
    pagination: {
        el: ".swiper-pagination",
        type: "bullets",
    },
});

window.onload = function(){
    $('#onload').fadeOut();
    $('body').removeClass("hidden");
}