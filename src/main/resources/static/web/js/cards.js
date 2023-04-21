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
            axios.get('http://localhost:8080/api/clients/current')
                .then(response => {
                    this.data = response.data
                    this.debitCards = response.data.cards.filter(card => card.type == "DEBIT");
                    this.creditCards = response.data.cards.filter(card => card.type == "CREDIT");      
                })
                .catch(error => console.log(error));
        },
        singOut() {
            axios.post('/api/logout')
            .then(response => window.location.href="/index.html")
        },
    }
}).mount("#app");

const swiper = new Swiper(".swiper", {
    pagination: {
        el: ".swiper-pagination",
        type: "bullets",
    },
});