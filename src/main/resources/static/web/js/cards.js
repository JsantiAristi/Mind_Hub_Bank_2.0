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
            id: "",
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            this.params = new URLSearchParams(location.search);
            this.id = this.params.get("id");
            axios.get('http://localhost:8080/api/clients/' + this.id)
                .then(response => {
                    this.data = response.data
                    this.debitCards = response.data.cards.filter(card => card.type == "DEBIT");
                    this.creditCards = response.data.cards.filter(card => card.type == "CREDIT");
                    console.log(this.debitCards);
                    console.log(this.creditCards);
                    
                })
                .catch(error => console.log(error));
        },
    }
}).mount("#app");

var swiper = new Swiper(".mySwiper", {
    pagination: {
    el: ".swiper-pagination",
    dynamicBullets: true,
    },
});