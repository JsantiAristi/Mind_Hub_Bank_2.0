const { createApp } = Vue

createApp({
    data() {
        return {
            // Inicializamos las variables
            data: [],
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
                    this.loans = this.data.loans
                })
                .catch(error => console.log(error));
        },
    }
}).mount("#app");